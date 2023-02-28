package io.github.dingdangdog.utils;

import io.github.dingdangdog.entity.FileInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 * MultipartFile工具类
 *
 * @author dingdangdog
 * @since 2022/8/22 16:24
 */
public class MultipartFileUtils {
    /**
     * 读取MultipartFile类型文件内容
     *
     * @param file 待读取文件
     * @return 文件内容
     * @author DingDangDog
     * @since 2022/11/2 14:40
     */
    public static String getInfo(MultipartFile file) {
        StringBuilder info = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF8"))) {
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    info.append(line);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info.toString();
    }

    public static void saveFile(FileInfo fileInfo, String waterMark, MultipartFile file) throws IOException {
        File targetFile = new File(fileInfo.getFilePath(), fileInfo.getFileName());

        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        file.transferTo(targetFile);
    }

    public static void backupFile(FileInfo fileInfo) throws IOException {
        File backFile = new File(fileInfo.getFilePath() + "backup/", fileInfo.getFileName());
        if (!backFile.getParentFile().exists()) {
            backFile.getParentFile().mkdirs();
        }
        Files.copy(new File(fileInfo.getFilePath(), fileInfo.getFileName()).toPath(), backFile.toPath());
    }

}
