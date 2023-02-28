package io.github.dingdangdog.entity;

/**
 * 存储文件基本信息
 *
 * @author DingDangDog
 */
public class FileInfo {
    private String filePath;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String backupFileUrl;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getBackupFileUrl() {
        return backupFileUrl;
    }

    public void setBackupFileUrl(String backupFileUrl) {
        this.backupFileUrl = backupFileUrl;
    }
}
