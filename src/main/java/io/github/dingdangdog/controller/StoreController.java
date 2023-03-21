package io.github.dingdangdog.controller;

import io.github.dingdangdog.config.ServerProperties;
import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ServerProperties serverProperties;

    /**
     * 获取图库地址
     *
     * @param key 个人密钥
     * @return 结果封装
     */
    @GetMapping("/getStoreUrl")
    public ResultDTO getStoreUrl(String key) {
        StoreDTO storeDTO = new StoreDTO();
        if (userProperties.keyMap.containsKey(key)) {
            storeDTO.setCode(200);
            storeDTO.setUrl(serverProperties.baseImageUrl + userProperties.keyMap.get(key) + "/");
            return storeDTO;
        }
        storeDTO.setCode(500);
        storeDTO.setMessage("No Permission!");
        return storeDTO;
    }
}
