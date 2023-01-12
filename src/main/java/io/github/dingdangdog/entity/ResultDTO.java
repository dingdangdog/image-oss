package io.github.dingdangdog.entity;

import java.util.Arrays;

/**
 * 返回值
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class ResultDTO {
    private String message;
    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "message='" + message + '\'' +
                ", urls=" + Arrays.toString(urls) +
                '}';
    }
}
