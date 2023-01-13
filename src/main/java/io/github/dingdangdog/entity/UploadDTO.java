package io.github.dingdangdog.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class UploadDTO {
    private String key;
    private MultipartFile file;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
                ", files=" + file +
                '}';
    }
}
