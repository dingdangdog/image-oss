package io.github.dingdangdog.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static void saveFile(String filePath, String fileName, MultipartFile file) throws IOException {
        File targetFile = new File(filePath, fileName);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        file.transferTo(targetFile);
    }
}
