package io.github.dingdangdog.service;

import java.util.List;

/**
 * 图库相关操作
 *
 * @author dingdangdog
 * @since 2023-09-04
 */
public interface StoreService {

    List<String> getFileList(String key);
}
