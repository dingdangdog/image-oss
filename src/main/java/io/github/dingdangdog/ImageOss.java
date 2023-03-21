package io.github.dingdangdog;

import io.github.dingdangdog.config.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 图床服务
 *
 * @author DingDangDog
 * @since 2023-01-11
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({UserProperties.class})
public class ImageOss {
    public static void main(String[] args) {
        SpringApplication.run(ImageOss.class, args);
    }
}