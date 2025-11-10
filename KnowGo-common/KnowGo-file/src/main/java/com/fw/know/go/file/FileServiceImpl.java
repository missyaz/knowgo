package com.fw.know.go.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname FileServiceImpl
 * @Description 文件服务默认实现
 * @Date 28/10/2025 下午5:11
 * @Author Leo
 */
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public boolean upload(String path, InputStream inputStream) {
        return false;
    }

    @Override
    public String extractText(InputStream inputStream) throws Exception{
        // 用于接收解析后的文本（BodyContentHandler会忽略非文本内容）
        ContentHandler contentHandler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();
        try (inputStream){
            parser.parse(inputStream, contentHandler, metadata, parseContext);
        }
        return contentHandler.toString();
    }

    @Override
    public Map<String, Object> extractMetadata(InputStream inputStream) throws Exception {
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();

        try (inputStream){
            // 仅提取元数据（忽略文本内容）
            parser.parse(inputStream, new BodyContentHandler(0), metadata, context);
            Map<String, Object> metadataMap = new HashMap<>();
            for (String name : metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }
            return metadataMap;
        }
    }
}
