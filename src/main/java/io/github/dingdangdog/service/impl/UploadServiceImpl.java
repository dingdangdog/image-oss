package io.github.dingdangdog.service.impl;

import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.UploadDTO;
import io.github.dingdangdog.service.UploadService;
import io.github.dingdangdog.utils.MultipartFileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 上传
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Value("${image.url}")
    private String imageUrl;

    @Value("${keyList}")
    private List<String> keyList;

    @Value("${image.path}")
    private String imagePath;

    @Override
    public ResultDTO upload(UploadDTO uploadDTO) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        if (!keyList.contains(uploadDTO.getKey())) {
            resultDTO.setMessage("no permission !");
            return resultDTO;
        }
        StringBuilder fileUrls = new StringBuilder();
        for (MultipartFile file : uploadDTO.getFiles()) {
            try {
                String fileName = UUID.randomUUID().toString();
                String originalFilename = file.getOriginalFilename();
                if (null != originalFilename) {
                    String[] split = originalFilename.split("\\.");
                    fileName = fileName + "." + split[split.length - 1];
                }
                fileUrls.append(imageUrl + uploadDTO.getKey() + "/");
                fileUrls.append(fileName);
                fileUrls.append(",");
                MultipartFileUtils.saveFile(imagePath + uploadDTO.getKey() + "/", fileName, file);
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        String names = fileUrls.toString();
        resultDTO.setUrls(names.substring(0, names.length() - 1).split(","));
        return resultDTO;
    }
}
