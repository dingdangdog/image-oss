package io.github.dingdangdog.entity;

/**
 * 上传结果响应实体
 *
 * @author dingdangdog
 * @date 2023/3/21 14:33
 */
public class StoreDTO extends ResultDTO{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
