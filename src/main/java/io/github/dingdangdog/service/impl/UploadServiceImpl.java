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

    @Value("${fileType}")
    private List<String> fileType;

    @Override
    public ResultDTO upload(UploadDTO uploadDTO) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        if (!keyList.contains(uploadDTO.getKey())) {
            resultDTO.setMessage("no permission !");
            return resultDTO;
        }
        StringBuilder fileUrl = new StringBuilder();
        try {
            MultipartFile file = uploadDTO.getFile();
            String fileName = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            if (null != originalFilename) {
                String[] split = originalFilename.split("\\.");
                String type = split[split.length - 1];
                if (!fileType.contains(type)) {
                    resultDTO.setMessage("Unsupported file format !");
                    return resultDTO;
                }
                fileName = fileName + "." + type;
            }
            fileUrl.append(imageUrl + uploadDTO.getKey() + "/");
            fileUrl.append(fileName);
            MultipartFileUtils.saveFile(imagePath + uploadDTO.getKey() + "/", fileName, file);
        } catch (IOException e) {
            throw new IOException(e);
        }
        String url = fileUrl.toString();
        resultDTO.setUrl(url);
        return resultDTO;
    }
}
