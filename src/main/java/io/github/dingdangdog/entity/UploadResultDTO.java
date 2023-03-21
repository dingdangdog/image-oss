package io.github.dingdangdog.entity;

/**
 * 上传结果响应实体
 *
 * @author dingdangdog
 * @date 2023/3/21 14:33
 */
public class UploadResultDTO extends ResultDTO{
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
}
