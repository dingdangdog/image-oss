package io.github.dingdangdog.controller;

import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.UploadDTO;
import io.github.dingdangdog.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 上传
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping()
    public ResultDTO upload(UploadDTO uploadDTO) {
        return uploadService.upload(uploadDTO);
    }
}
