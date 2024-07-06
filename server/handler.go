package main

import (
	"fmt"
	"net/http"
	"os"
	"path/filepath"
	"sort"
	"strconv"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
)

func UploadHandler(c *gin.Context) {
	var uploadDTO UploadDTO
	if err := c.ShouldBind(&uploadDTO); err != nil {
		c.JSON(http.StatusBadRequest, Result{Code: 400, Message: "Invalid request"})
		return
	}

	if _, ok := config.UserMap[uploadDTO.Key]; !ok {
		c.JSON(http.StatusForbidden, Result{Code: 403, Message: "No Permission!"})
		return
	}

	key := uploadDTO.Key
	var fileUrl strings.Builder
	var backupFileUrl strings.Builder
	var fileInfo FileInfo

	file, err := uploadDTO.File.Open()
	if err != nil {
		c.JSON(http.StatusInternalServerError, Result{Code: 500, Message: "Failed to open file!"})
		return
	}
	defer file.Close()

	fileName := config.UserMap[key] + fmt.Sprint(time.Now().Unix())
	originalFilename := uploadDTO.File.Filename
	var fileType string

	if originalFilename != "" {
		split := strings.Split(originalFilename, ".")
		fileType = split[len(split)-1]

		if !contains(config.AllowType, fileType) {
			c.JSON(http.StatusUnsupportedMediaType, Result{Code: 415, Message: "Uploaded file type is not supported!"})
			return
		}

		fileInfo.FileType = fileType
		fileInfo.FileName = fileName + "." + fileType
	}

	fileInfo.FilePath = filepath.Join(config.ImagePath, config.UserMap[key])

	fileUrl.WriteString(config.BaseImageUrl)
	fileUrl.WriteString(config.UserMap[key])
	fileUrl.WriteString("/")
	fileUrl.WriteString(fileName)
	fileUrl.WriteString(".")
	fileUrl.WriteString(fileType)

	err = saveFile(fileInfo, file)
	var resultDTO Result
	if err != nil {
		c.JSON(http.StatusInternalServerError, Result{Code: 500, Message: "Failed to save file!" + err.Error()})
		return
	}
	url := fileUrl.String()
	fileInfo.FileUrl = url
	resultDTO.Url = url

	// Generate thumbnail
	err = generateThumbnail(fileInfo.FilePath, fileInfo.FileName)
	thumbFileUrl := strings.Replace(url, config.UserMap[key]+"/", config.UserMap[key]+"/thumb/", 1)

	fileInfo.ThumFileUrl = thumbFileUrl
	resultDTO.ThumFileUrl = thumbFileUrl
	if err != nil {
		c.JSON(http.StatusInternalServerError, Result{Code: 500, Message: "Failed to generate thumbnail!" + err.Error()})
		return
	}

	if uploadDTO.WaterMark != "" {
		if !contains(config.AllowMarkType, fileType) {
			c.JSON(http.StatusUnsupportedMediaType, Result{Code: 415, Message: "The file type with watermark is not supported!"})
			return
		}
		err = addWatermark(fileInfo, uploadDTO.WaterMark)
		if err != nil {
			c.JSON(http.StatusInternalServerError, Result{Code: 500, Message: "Failed to add watermark!" + err.Error()})
			return
		}

		backupFileUrl.WriteString(config.BaseImageUrl)
		backupFileUrl.WriteString(config.UserMap[key])
		backupFileUrl.WriteString("/backup/")
		backupFileUrl.WriteString(fileName)
		backupFileUrl.WriteString(".")
		backupFileUrl.WriteString(fileType)
		backupUrl := backupFileUrl.String()

		fileInfo.BackupFileUrl = backupUrl
		resultDTO.BackupUrl = backupUrl
	}
	resultDTO.Code = 200
	c.JSON(http.StatusOK, resultDTO)
}

func GetStoreUrlHandler(c *gin.Context) {
	key := c.Query("key")
	storeDTO := StoreDTO{}

	if value, ok := config.UserMap[key]; ok {
		storeDTO.Code = 200
		storeDTO.Url = config.BaseImageUrl + value + "/"
	} else {
		storeDTO.Code = 500
		storeDTO.Message = "No Permission!"
	}

	c.JSON(http.StatusOK, storeDTO)
}

func GetImageListHandler(c *gin.Context) {
	key := c.Query("key")
	find := c.Query("find")
	storeDTO := StoreDTO{}

	if value, ok := config.UserMap[key]; ok {
		fileList := getFileList(config.UserMap[key] + "/thumb/")
		sort.Sort(sort.Reverse(sort.StringSlice(fileList)))

		var nList []string
		for _, name := range fileList {
			if find != "" && !strings.Contains(name, find) {
				continue
			}
			nList = append(nList, config.BaseImageUrl+value+"/thumb/"+name)
		}

		storeDTO.Code = 200
		storeDTO.ImageList = nList
	} else {
		storeDTO.Code = 500
		storeDTO.Message = "No Permission!"
	}

	c.JSON(http.StatusOK, storeDTO)
}

func getFileList(dir string) []string {
	folderPath := filepath.Join(config.ImagePath, dir)
	var imageList []string

	files, err := os.ReadDir(folderPath)
	if err != nil {
		return imageList
	}

	for _, file := range files {
		if !file.IsDir() {
			imageList = append(imageList, file.Name())
		}
	}

	return imageList
}

func DeleteImageHandler(c *gin.Context) {
	imageName := c.Query("imageName")
	key := c.Query("key")
	resultDTO := Result{Code: 500, Message: "删除失败"}

	if imageName != "" && config.UserMap[key] != "" {
		filePath := filepath.Join(config.ImagePath, config.UserMap[key], imageName)
		err := os.Remove(filePath)
		if err == nil {
			resultDTO.Code = 200
			resultDTO.Message = "删除成功"
			c.JSON(http.StatusOK, resultDTO)
			return
		} else {
			resultDTO.Message += err.Error()
		}
		// 删除缩略图
		thumpPath := filepath.Join(config.ImagePath, config.UserMap[key], "thumb", imageName)
		os.Remove(thumpPath)
	}

	c.JSON(http.StatusOK, resultDTO)
}

func ExportHandler(c *gin.Context) {
	key := c.Query("key")
	if _, ok := config.UserMap[key]; !ok {
		c.JSON(http.StatusForbidden, gin.H{"message": "No Permission!"})
		return
	}

	zipFilePath, err := CompressFile(filepath.Join(config.ImagePath, config.UserMap[key]))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"message": "Error compressing files"})
		return
	}

	file, err := os.Open(zipFilePath)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"message": "Error opening zip file"})
		return
	}

	fileInfo, err := file.Stat()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"message": "Error getting file info"})
		return
	}
	defer file.Close()

	c.Header("Access-Control-Allow-Origin", "*")
	c.Header("Content-Type", "application/force-download")
	c.Header("Content-Disposition", fmt.Sprintf("attachment; filename=%s", time.Now().Format("20060102150405")+"_"+filepath.Base(zipFilePath)))
	c.Header("Content-Length", strconv.FormatInt(fileInfo.Size(), 10))

	http.ServeContent(c.Writer, c.Request, "", fileInfo.ModTime(), file)
}

func GenerateThumb(c *gin.Context) {
	key := c.Query("key")
	if _, ok := config.UserMap[key]; !ok {
		c.JSON(http.StatusForbidden, gin.H{"message": "No Permission!"})
		return
	}

	fileList := getFileList(config.UserMap[key])
	thumbList := getFileList(config.UserMap[key] + "/thumb/")

	needList := checkFilesNotInThumbList(fileList, thumbList)

	for _, fileName := range needList {
		generateThumbnail(filepath.Join(config.ImagePath, config.UserMap[key]), fileName)
	}
	resultDTO := Result{Code: 200, Message: strconv.Itoa(len(needList))}
	c.JSON(http.StatusOK, resultDTO) // needList)
}

// checkFilesNotInThumbList 检查 fileList 中的文件名是否不存在于 thumbList 中
func checkFilesNotInThumbList(fileList, thumbList []string) []string {
	// 创建一个 map 来存储 thumbList 中的文件名
	thumbMap := make(map[string]bool)
	for _, thumb := range thumbList {
		thumbMap[thumb] = true
	}

	// 检查 fileList 中的文件名是否在 thumbMap 中不存在
	var result []string
	for _, file := range fileList {
		if _, exists := thumbMap[file]; !exists {
			result = append(result, file)
		}
	}

	return result
}
