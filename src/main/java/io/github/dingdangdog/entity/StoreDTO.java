package io.github.dingdangdog.entity;

import java.util.List;

/**
 * 图库响应实体
 *
 * @author dingdangdog
 * @date 2023/3/21 14:33
 */
public class StoreDTO extends ResultDTO{
    private String url;
    private List<String> imageList;

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
