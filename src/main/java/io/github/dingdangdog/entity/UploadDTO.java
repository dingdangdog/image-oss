package io.github.dingdangdog.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class UploadDTO {
    /**
     * 个人ID，用于确定是否有权上传和文件分类
     */
    private String key;
    /**
     * 水印内容
     */
    private String waterMark;
    private MultipartFile file;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "UploadDTO{" +
                "key='" + key + '\'' +
                ", waterMark='" + waterMark + '\'' +
                ", files=" + file +
                '}';
    }
}
