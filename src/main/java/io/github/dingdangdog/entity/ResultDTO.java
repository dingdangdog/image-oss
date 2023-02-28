package io.github.dingdangdog.entity;

/**
 * 返回值
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class ResultDTO {
    private String message;
    private String url;

    private String backupUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBackupUrl() {
        return backupUrl;
    }

    public void setBackupUrl(String backupUrl) {
        this.backupUrl = backupUrl;
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
                ", url=" + url +
                '}';
    }
}
