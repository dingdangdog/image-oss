package io.github.dingdangdog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 服务信息配置封装
 *
 * @author dingdangdog
 * @date 2023/3/21 14:44
 */
@ConfigurationProperties(prefix = "info")
public class ServerProperties {
    /**
     * 服务地址
     */
    private String serverUrl;
    /**
     * 服务接口基础前缀
     */
    private String baseApiUrl;
    /**
     * 图片地址基础前缀
     */
    private String baseImageUrl;

    /**
     * 默认水印内容
     */
    private String waterMark;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setBaseApiUrl(String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getBaseApiUrl() {
        return baseApiUrl;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public String getWaterMark() {
        return waterMark;
    }
}
