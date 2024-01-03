package io.github.dingdangdog.controller;

import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.utils.CompressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 导出
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
@RestController
@RequestMapping("/export")
@CrossOrigin
public class ExportController {
    @Autowired
    private UserProperties userProperties;
    @Value("${image.path}")
    private String imagePath;


    @GetMapping()
    public void export(String key, HttpServletResponse response) throws Exception {
        if (!userProperties.keyMap.containsKey(key)) {
            return;
        }
        String zipFile = CompressUtils.zipFile(new File(imagePath + userProperties.keyMap.get(key) + "/"));
        File file = new File(zipFile);
        final FileInputStream fileInputStream = new FileInputStream(file);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getName() + System.currentTimeMillis(), "UTF-8"));

        byte[] buffer = new byte[1024 * 1024];
        try (BufferedInputStream bis = new BufferedInputStream(fileInputStream)) {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        }
    }
}
