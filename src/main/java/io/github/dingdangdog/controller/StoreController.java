package io.github.dingdangdog.controller;

import io.github.dingdangdog.config.ServerProperties;
import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.StoreDTO;
import io.github.dingdangdog.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private StoreService storeService;

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
            storeDTO.setUrl(serverProperties.getBaseImageUrl() + userProperties.keyMap.get(key) + "/");
            return storeDTO;
        }
        storeDTO.setCode(500);
        storeDTO.setMessage("No Permission!");
        return storeDTO;
    }


    /**
     * 获取图库内文件
     *
     * @param key 个人密钥
     * @return 封装结果
     */
    @GetMapping("/getImageList")
    public ResultDTO getImageList(String key) {
        StoreDTO storeDTO = new StoreDTO();
        if (userProperties.keyMap.containsKey(key)) {
            List<String> fileList = storeService.getFileList(key);
            List<String> nList = new ArrayList<>();
            fileList.forEach(name -> {
                name = serverProperties.getBaseImageUrl() + userProperties.keyMap.get(key) + "/" + name;
                nList.add(name);
            });
            storeDTO.setCode(200);
            storeDTO.setImageList(nList);
        } else {
            storeDTO.setCode(500);
            storeDTO.setMessage("No Permission!");
        }
        return storeDTO;
    }


    /**
     * 删除图片
     *
     * @param imageName 图片名称
     * @return 删除结果
     */
    @PostMapping("/deleteImage")
    public ResultDTO deleteImage(String imageName, String key) {
        ResultDTO resultDTO = new ResultDTO();
        if (StringUtils.hasText(imageName) && userProperties.keyMap.containsKey(key)) {
            if(storeService.deleteImage(imageName, key)) {
                resultDTO.setMessage("删除成功");
                resultDTO.setCode(200);
            } else {
                resultDTO.setMessage("删除失败");
                resultDTO.setCode(500);
            }
            return resultDTO;
        }

        resultDTO.setCode(500);
        resultDTO.setMessage("删除失败");
        return resultDTO;
    }
}
