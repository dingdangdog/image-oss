package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"os"

	"github.com/gin-gonic/gin"
)

var config Config

const ConfigFile = "./config/config.json"

func main() {
	// 加载系统配置
	err := loadConfig()
	if err != nil {
		fmt.Println(err)
		return
	}
	// 创建路由
	router := gin.Default()

	api := router.Group("/")

	api.POST("/upload", UploadHandler)
	api.POST("/store/deleteImage", DeleteImageHandler)
	api.POST("/store/initThumb", GenerateThumb)
	api.GET("/store/getStoreUrl", GetStoreUrlHandler)
	api.GET("/store/getImageList", GetImageListHandler)
	api.GET("/export", ExportHandler)

	router.Run(":" + config.Port)
}

func loadConfig() error {
	fileBytes, _ := os.ReadFile(ConfigFile)
	if len(fileBytes) != 0 {
		return json.Unmarshal(fileBytes, &config)
	} else {
		return errors.New("no config file")
	}
}

// 定义一个 JWT 的中间件
// func AllowCheck() gin.HandlerFunc {
// 	return func(c *gin.Context) {
// 		rawToken := c.Request.Header.Get("Authorization")
// 		if rawToken == "" {
// 			c.JSON(http.StatusUnauthorized, gin.H{
// 				"success":      false,
// 				"errorMessage": "未登录",
// 			})
// 			c.Abort()
// 			return
// 		}
// 		// 解析 token
// 		token, err := ParseJWT(rawToken)
// 		if err != nil {
// 			c.JSON(http.StatusUnauthorized, gin.H{
// 				"success":      false,
// 				"errorMessage": "未登录",
// 			})
// 			c.Abort()
// 			return
// 		}
// 		// 把名称加到上下文
// 		if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
// 			c.Set("username", claims["name"])
// 			c.Set("uid", claims["id"])
// 			c.Next()
// 		} else {
// 			c.JSON(http.StatusUnauthorized, gin.H{
// 				"success":      false,
// 				"errorMessage": "未登录",
// 			})
// 			c.Abort()
// 			return
// 		}
// 	}
// }
