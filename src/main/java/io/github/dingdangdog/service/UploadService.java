package io.github.dingdangdog.service;

import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.UploadDTO;

import java.io.IOException;

/**
 * 上传
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public interface UploadService {
    ResultDTO upload(UploadDTO uploadDTO) throws IOException;
}
