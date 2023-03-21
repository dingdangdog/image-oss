package io.github.dingdangdog.controller;

import io.github.dingdangdog.config.ServerProperties;
import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.ServerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务相关接口
 *
 * @author dingdangdog
 * @date 2023/3/21 14:31
 */
@RequestMapping("/server")
@RestController
@CrossOrigin
public class ServerController {
    @Autowired
    private ServerProperties serverProperties;

    /**
     * 获取服务信息
     *
     * @return 服务信息
     */
    @GetMapping("/getServerInfo")
    public ResultDTO getServerInfo() {
        ServerDTO serverDTO = new ServerDTO();
        BeanUtils.copyProperties(serverProperties, serverDTO);
        return serverDTO;
    }
}

