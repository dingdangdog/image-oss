package io.github.dingdangdog.service.impl;

import io.github.dingdangdog.entity.FileInfo;
import io.github.dingdangdog.entity.ResultDTO;
import io.github.dingdangdog.entity.UploadDTO;
import io.github.dingdangdog.service.UploadService;
import io.github.dingdangdog.utils.ImageUtils;
import io.github.dingdangdog.utils.MultipartFileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    public ResultDTO upload(UploadDTO uploadDTO) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        if (!keyList.contains(uploadDTO.getKey())) {
            resultDTO.setMessage("no permission !");
            return resultDTO;
        }
        // 用于组装图片URL
        StringBuilder fileUrl = new StringBuilder();
        StringBuilder backupFileUrl = new StringBuilder();
        // 用于储存文件基本信息
        FileInfo fileInfo = new FileInfo();
        try {
            MultipartFile file = uploadDTO.getFile();
            // 重新生成文件名
            String fileName = uploadDTO.getKey() + System.currentTimeMillis();
            String originalFilename = file.getOriginalFilename();
            String type = null;
            if (null != originalFilename) {
                String[] split = originalFilename.split("\\.");
                type = split[split.length - 1];
                // 校验文件类型
                if (!fileType.contains(type)) {
                    resultDTO.setMessage("Uploaded file type is not supported !");
                    return resultDTO;
                }
                fileInfo.setFileType(type);
                fileInfo.setFileName(fileName + "." + type);
            }
            fileInfo.setFilePath(imagePath + uploadDTO.getKey() + "/");
            // 组装加水印后图片地址
            fileUrl.append(imageUrl)
                    .append(uploadDTO.getKey())
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
                    resultDTO.setMessage("The file type with watermark is not supported !");
                    return resultDTO;
                }
                ImageUtils.addWatermark(fileInfo, uploadDTO.getWaterMark());
                // 组装原图备份地址
                backupFileUrl.append(imageUrl)
                        .append(uploadDTO.getKey())
                        .append("/backup/")
                        .append(fileName)
                        .append(".")
                        .append(type);
                String backupUrl = backupFileUrl.toString();
                fileInfo.setBackupFileUrl(backupUrl);
                resultDTO.setBackupUrl(backupUrl);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        return resultDTO;
    }
}
