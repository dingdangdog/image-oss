package io.github.dingdangdog.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传图片
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class UploadDTO {
    private String key;
    private List<MultipartFile> files;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "UploadDTO{" +
                "key='" + key + '\'' +
                ", files=" + files +
                '}';
    }
}
