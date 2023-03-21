package io.github.dingdangdog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 用户密钥信息配置
 *
 * @author dingdangdog
 * @date 2023/3/21 10:41
 */
@ConfigurationProperties(prefix = "user")
public class UserProperties {
    /**
     * 密钥集合：
     * key：密钥
     * value：用户名&文件夹名
     */
    public Map<String, String> keyMap;

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }
}
