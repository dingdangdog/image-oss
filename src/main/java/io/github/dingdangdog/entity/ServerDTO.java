package io.github.dingdangdog.entity;

/**
 * 服务信息返回值
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class ServerDTO extends ResultDTO {
    /**
     * 服务接口基础前缀
     */
    private String baseApiUrl;

    /**
     * 默认水印内容
     */
    private String waterMark;

    public String getBaseApiUrl() {
        return baseApiUrl;
    }

    public void setBaseApiUrl(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark;
    }
}
