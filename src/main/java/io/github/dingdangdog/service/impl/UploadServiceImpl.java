package io.github.dingdangdog.service.impl;

import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.entity.FileInfo;
import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.UploadDTO;
import io.github.dingdangdog.service.UploadService;
import io.github.dingdangdog.utils.ImageUtils;
import io.github.dingdangdog.utils.MultipartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上传
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private UserProperties userProperties;
    @Value("${image.url}")
    private String imageUrl;
    @Value("${image.path}")
    private String imagePath;

    /**
     * 支持上传的文件类型
     */
    @Value("${fileType.up}")
    private List<String> fileType;
    /**
     * 支持加水印的文件类型
     */
    @Value("${fileType.wm}")
    private List<String> wmFileType;

    @Override
    public ResultDTO upload(UploadDTO uploadDTO) {
        ResultDTO resultDTO = new ResultDTO();
        if (!userProperties.keyMap.containsKey(uploadDTO.getKey())) {
            resultDTO.setCode(500);
            resultDTO.setMessage("No Permission!");
            return resultDTO;
        }
        String key = uploadDTO.getKey();
        // 用于组装图片URL
        StringBuilder fileUrl = new StringBuilder();
        StringBuilder backupFileUrl = new StringBuilder();
        // 用于储存文件基本信息
        FileInfo fileInfo = new FileInfo();
        try {
            MultipartFile file = uploadDTO.getFile();
            // 重新生成文件名
            String fileName = userProperties.keyMap.get(key) + System.currentTimeMillis();
            String originalFilename = file.getOriginalFilename();
            String type = null;
            if (null != originalFilename) {
                String[] split = originalFilename.split("\\.");
                type = split[split.length - 1];
                // 校验文件类型
                if (!fileType.contains(type)) {
                    resultDTO.setCode(500);
                    resultDTO.setMessage("Uploaded file type is not supported!");
                    return resultDTO;
                }
                fileInfo.setFileType(type);
                fileInfo.setFileName(fileName + "." + type);
            }
            fileInfo.setFilePath(imagePath + userProperties.keyMap.get(key) + "/");
            // 组装加水印后图片地址
            fileUrl.append(imageUrl)
                    .append(userProperties.keyMap.get(key))
                    .append("/")
                    .append(fileName)
                    .append(".")
                    .append(type);
            // 保存图片
            MultipartFileUtils.saveFile(fileInfo, uploadDTO.getWaterMark(), file);
            String url = fileUrl.toString();
            fileInfo.setFileUrl(url);
            resultDTO.setUrl(url);

            // 添加水印
            if (!StringUtils.isEmpty(uploadDTO.getWaterMark())) {
                if (!wmFileType.contains(type)) {
                    resultDTO.setCode(500);
                    resultDTO.setMessage("The file type with watermark is not supported!");
                    return resultDTO;
                }
                ImageUtils.addWatermark(fileInfo, uploadDTO.getWaterMark());
                // 组装原图备份地址
                backupFileUrl.append(imageUrl)
                        .append(userProperties.keyMap.get(key))
                        .append("/backup/")
                        .append(fileName)
                        .append(".")
                        .append(type);
                String backupUrl = backupFileUrl.toString();
                fileInfo.setBackupFileUrl(backupUrl);
                resultDTO.setBackupUrl(backupUrl);
            }
        } catch (IOException e) {
            resultDTO.setCode(500);
            resultDTO.setMessage("Unknown Exception!");
            return resultDTO;
//            throw new IOException(e);
        }
        resultDTO.setCode(200);
        return resultDTO;
    }
}
