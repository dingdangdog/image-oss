package io.github.dingdangdog.controller;

import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.entity.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图库相关接口
 *
 * @author dingdangdog
 * @date 2023/3/21 11:42
 */
@RequestMapping("/store")
@RestController
@CrossOrigin
public class StoreController {
    @Autowired
    private UserProperties userProperties;
    @Value("${image.url}")
    private String imageUrl;

    /**
     * 获取图库地址
     *
     * @param key 个人密钥
     * @return 结果封装
     */
    @GetMapping("/getStoreUrl")
    public ResultDTO getStoreUrl(String key) {
        ResultDTO resultDTO = new ResultDTO();
        if (userProperties.keyMap.containsKey(key)) {
            resultDTO.setCode(200);
            resultDTO.setUrl(imageUrl + userProperties.keyMap.get(key) + "/");
            return resultDTO;
        }
        resultDTO.setCode(500);
        resultDTO.setMessage("No Permission!");
        return resultDTO;
    }
}
