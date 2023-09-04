package io.github.dingdangdog.service.impl;

import io.github.dingdangdog.config.UserProperties;
import io.github.dingdangdog.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图库相关操作
 *
 * @author dingdangdog
 * @since 1.0
 */
@Service
public class StoreServiceImpl implements StoreService {
    /**
     * 图片存储路径
     */
    @Value("${image.path}")
    private String imagePath;
    @Autowired
    private UserProperties userProperties;
    @Override
    public List<String> getFileList(String key) {
        // Specify the path to the folder you want to read files from
        String folderPath = imagePath + userProperties.keyMap.get(key);

        // Create a File object representing the folder
        File folder = new File(folderPath);

        List<String> imageList = new ArrayList<>();

        // Check if the folder exists
        if (folder.exists() && folder.isDirectory()) {
            // Get an array of File objects representing the files in the folder
            File[] files = folder.listFiles();

            // Loop through the array and print the names of the files
            for (File file : files) {
                if (file.isFile()) {
                    imageList.add(file.getName());
                }
            }
        }
        return imageList;
    }
}
