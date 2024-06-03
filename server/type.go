package main

import "mime/multipart"

type Config struct {
	Port          string            `json:"port"`
	ServerUrl     string            `json:"server_url"`
	BaseImageUrl  string            `json:"base_image_url"`
	ImagePath     string            `json:"image_path"`
	WaterMark     string            `json:"mark"`
	UserMap       map[string]string `json:"user_map"`
	AllowType     []string          `json:"allow_type"`
	AllowMarkType []string          `json:"allow_mark_type"`
}

type Result struct {
	Code      int    `json:"code"`
	Message   string `json:"message"`
	Url       string `json:"url"`
	BackupUrl string `json:"backupUrl"`
}

type FileInfo struct {
	FilePath      string `json:"filePath"`
	FileName      string `json:"fileName"`
	FileType      string `json:"fileType"`
	FileUrl       string `json:"fileUrl"`
	BackupFileUrl string `json:"backupFileUrl"`
}

// UploadDTO represents the data transfer object for uploading an image
type UploadDTO struct {
	Key       string                `form:"key" binding:"required"`
	WaterMark string                `form:"waterMark"`
	File      *multipart.FileHeader `form:"file" binding:"required"`
}

type StoreDTO struct {
	Code      int      `json:"code"`
	Message   string   `json:"message"`
	Url       string   `json:"url"`
	ImageList []string `json:"imageList"`
}
