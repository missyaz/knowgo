package com.fw.know.go.file;

import java.io.InputStream;
import java.util.Map;

/**
 * @Classname FileService
 * @Description 文件服务接口
 * @Date 28/10/2025 下午5:05
 * @Author Leo
 */
public interface FileService {

    /**
     * 上传文件
     * @param path 文件路径
     * @param inputStream 文件输入流
     * @return 是否上传成功
     */
    public boolean upload(String path, InputStream inputStream);

    /**
     * 从文件输入流中提取文本
     * @param inputStream 文件输入流
     * @return 提取到的文本
     * @throws Exception 提取文本过程中可能抛出的异常
     */
    public String extractText(InputStream inputStream) throws Exception;

     /**
      * 从文件输入流中提取元数据
      * @param inputStream 文件输入流
      * @return 提取到的元数据键值对
      * @throws Exception 提取元数据过程中可能抛出的异常
      */
    public Map<String, String> extractMetadata(InputStream inputStream) throws Exception;
}
