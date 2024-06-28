package main

import (
	"archive/zip"
	"fmt"
	"image"
	"image/color"
	"image/draw"
	"image/jpeg"
	"image/png"
	"io"
	"mime/multipart"
	"os"
	"path/filepath"

	"github.com/golang/freetype"
)

func addWatermark(fileInfo FileInfo, waterMark string) error {
	err := backupFile(fileInfo)
	if err != nil {
		return err
	}

	file, err := os.Open(filepath.Join(fileInfo.FilePath, fileInfo.FileName))
	if err != nil {
		return fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()

	img, imgType, err := image.Decode(file)
	if err != nil {
		return fmt.Errorf("failed to decode image: %w", err)
	}

	imgBounds := img.Bounds()
	imgWidth := imgBounds.Dx()
	imgHeight := imgBounds.Dy()
	fontSize := imgWidth / 40.0
	if fontSize < 20 {
		fontSize = 20
	}

	// 创建带水印的新图像
	rgba := image.NewRGBA(image.Rect(0, 0, imgWidth, imgHeight))
	draw.Draw(rgba, rgba.Bounds(), img, image.Point{}, draw.Src)

	fontBytes, err := os.ReadFile("./font/SmileySans-Oblique.ttf")
	if err != nil {
		return fmt.Errorf("failed to read font file: %w", err)
	}

	f, err := freetype.ParseFont(fontBytes)
	if err != nil {
		return fmt.Errorf("failed to parse font: %w", err)
	}

	c := freetype.NewContext()
	c.SetDPI(72)
	c.SetFont(f)
	c.SetFontSize(float64(fontSize))
	c.SetClip(rgba.Bounds())
	c.SetDst(rgba)
	c.SetSrc(image.NewUniform(color.RGBA{R: 160, G: 160, B: 160, A: 250}))

	pt := freetype.Pt((imgWidth-len(waterMark)*8)/2, imgHeight/2)
	_, err = c.DrawString(waterMark, pt)
	if err != nil {
		return fmt.Errorf("failed to add label: %w", err)
	}

	outputFile, err := os.Create(filepath.Join(fileInfo.FilePath, fileInfo.FileName))
	if err != nil {
		return fmt.Errorf("failed to create output file: %w", err)
	}
	defer outputFile.Close()

	switch imgType {
	case "jpeg", "jpg":
		err = jpeg.Encode(outputFile, rgba, nil)
	case "png":
		err = png.Encode(outputFile, rgba)
	default:
		return fmt.Errorf("unsupported file type: %s", fileInfo.FileType)
	}

	if err != nil {
		return fmt.Errorf("failed to encode image: %w", err)
	}

	return nil
}

func backupFile(fileInfo FileInfo) error {
	// 构建备份文件路径
	backupDir := filepath.Join(fileInfo.FilePath, "backup")
	// 创建备份目录
	err := os.MkdirAll(backupDir, os.ModePerm)
	if err != nil {
		return fmt.Errorf("failed to create backup directory: %w", err)
	}

	// 打开源文件
	sourceFile := filepath.Join(fileInfo.FilePath, fileInfo.FileName)
	src, err := os.Open(sourceFile)
	if err != nil {
		return fmt.Errorf("failed to open source file: %w", err)
	}
	defer src.Close()

	// 创建备份文件
	backupFile := filepath.Join(backupDir, fileInfo.FileName)
	dst, err := os.Create(backupFile)
	if err != nil {
		return fmt.Errorf("failed to create backup file: %w", err)
	}
	defer dst.Close()

	// 复制文件内容
	_, err = io.Copy(dst, src)
	if err != nil {
		return fmt.Errorf("failed to copy file content: %w", err)
	}

	return nil
}

func saveFile(fileInfo FileInfo, file multipart.File) error {
	_, err := os.Stat(fileInfo.FilePath)
	if err != nil {
		err = os.Mkdir(fileInfo.FilePath, os.ModePerm)
		if err != nil {
			return fmt.Errorf("File Path Create Error: %w"+fileInfo.FilePath, err)
		}
	}
	outputFile, err := os.Create(filepath.Join(fileInfo.FilePath, fileInfo.FileName))
	if err != nil {
		return fmt.Errorf("failed to create output file: %w", err)
	}
	defer outputFile.Close()

	_, err = file.Seek(0, 0) // Seek to the beginning of the file
	if err != nil {
		return fmt.Errorf("failed to seek file: %w", err)
	}

	_, err = io.Copy(outputFile, file)
	if err != nil {
		return fmt.Errorf("failed to copy file: %w", err)
	}

	return nil
}

// PathExistsOrCreate 校验文件夹是否存在，不存在则创建
func PathExistsOrCreate(path string) {
}

func contains(slice []string, item string) bool {
	for _, element := range slice {
		if element == item {
			return true
		}
	}
	return false
}

// zipFile recursively compresses files and directories
func zipFile(writer *zip.Writer, filePath string, baseInZip string) error {
	fileInfo, err := os.Stat(filePath)
	if err != nil {
		return err
	}

	if fileInfo.IsDir() {
		fileList, err := os.ReadDir(filePath)
		if err != nil {
			return err
		}

		for _, file := range fileList {
			newBase := filepath.Join(baseInZip, fileInfo.Name())
			if err := zipFile(writer, filepath.Join(filePath, file.Name()), newBase); err != nil {
				return err
			}
		}
	} else {
		file, err := os.Open(filePath)
		if err != nil {
			return err
		}
		defer file.Close()

		zipEntryWriter, err := writer.Create(filepath.Join(baseInZip, fileInfo.Name()))
		if err != nil {
			return err
		}

		_, err = io.Copy(zipEntryWriter, file)
		if err != nil {
			return err
		}
	}

	return nil
}

// CompressFile compresses the specified file or directory
func CompressFile(path string) (string, error) {
	fileInfo, err := os.Stat(path)
	if err != nil {
		return "", err
	}

	var generatePath string
	if fileInfo.IsDir() {
		generatePath = filepath.Join(filepath.Dir(path), fileInfo.Name()+".zip")
	} else {
		generatePath = filepath.Join(filepath.Dir(path), fileInfo.Name()[:len(fileInfo.Name())-len(filepath.Ext(fileInfo.Name()))]+".zip")
	}

	outputFile, err := os.Create(generatePath)
	if err != nil {
		return "", err
	}
	defer outputFile.Close()

	zipWriter := zip.NewWriter(outputFile)
	defer zipWriter.Close()

	err = zipFile(zipWriter, path, "")
	if err != nil {
		return "", err
	}

	return generatePath, nil
}
