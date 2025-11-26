package com.fw.know.go.ai.example;

import com.fw.know.go.ai.multimodal.MultimodalContent;
import com.fw.know.go.ai.multimodal.MultimodalContentManager;
import com.fw.know.go.ai.multimodal.MultimodalContent.ContentType;
import com.fw.know.go.ai.multimodal.MultimodalContent.ContentFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 多模态内容处理示例
 */
public class MultimodalExample {
    
    private final MultimodalContentManager contentManager;
    
    public MultimodalExample(MultimodalContentManager contentManager) {
        this.contentManager = contentManager;
    }
    
    /**
     * 示例1：文本内容处理
     */
    public void textContentExample() {
        System.out.println("=== 文本内容处理示例 ===");
        
        String textContent = "这是一个关于人工智能的文本内容。"
                + "人工智能正在改变我们的生活方式，"
                + "从智能助手到自动驾驶，AI技术无处不在。";
        
        // 处理纯文本
        MultimodalContent textResult = contentManager.processContent(textContent, "text/plain");
        System.out.println("文本处理结果:");
        System.out.println("内容类型: " + textResult.getContentType());
        System.out.println("内容格式: " + textResult.getContentFormat());
        System.out.println("内容大小: " + textResult.getSize() + " 字节");
        System.out.println("元数据: " + textResult.getMetadata());
        System.out.println();
        
        // 处理Markdown文本
        String markdownContent = "# AI技术发展\n\n"
                + "## 机器学习\n"
                + "机器学习是AI的核心技术之一。\n\n"
                + "## 深度学习\n"
                + "深度学习在图像识别等领域表现出色。\n\n"
                + "```python\n"
                + "import tensorflow as tf\n"
                + "model = tf.keras.Sequential()\n"
                + "```";
        
        MultimodalContent markdownResult = contentManager.processContent(markdownContent, "text/markdown");
        System.out.println("Markdown处理结果:");
        System.out.println("内容类型: " + markdownResult.getContentType());
        System.out.println("内容格式: " + markdownResult.getContentFormat());
        System.out.println("内容大小: " + markdownResult.getSize() + " 字节");
        System.out.println();
    }
    
    /**
     * 示例2：图片内容处理
     */
    public void imageContentExample() {
        System.out.println("=== 图片内容处理示例 ===");
        
        // 模拟Base64编码的图片数据
        String base64Image = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAA8A/9k=";
        
        try {
            // 处理JPEG图片
            MultimodalContent imageResult = contentManager.processContent(base64Image, "image/jpeg");
            System.out.println("图片处理结果:");
            System.out.println("内容类型: " + imageResult.getContentType());
            System.out.println("内容格式: " + imageResult.getContentFormat());
            System.out.println("内容大小: " + imageResult.getSize() + " 字节");
            System.out.println("元数据: " + imageResult.getMetadata());
            System.out.println();
            
            // 格式转换 - JPEG转PNG
            MultimodalContent pngResult = contentManager.convertContent(imageResult, "image/png");
            System.out.println("格式转换结果 (JPEG -> PNG):");
            System.out.println("新内容类型: " + pngResult.getContentType());
            System.out.println("新内容格式: " + pngResult.getContentFormat());
            System.out.println("新内容大小: " + pngResult.getSize() + " 字节");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("图片处理示例需要真实的图片数据: " + e.getMessage());
            System.out.println();
        }
    }
    
    /**
     * 示例3：音频内容处理
     */
    public void audioContentExample() {
        System.out.println("=== 音频内容处理示例 ===");
        
        // 模拟音频文件路径
        String audioFilePath = "/path/to/audio/sample.mp3";
        
        try {
            // 处理MP3音频
            MultimodalContent audioResult = contentManager.processContent(audioFilePath, "audio/mp3");
            System.out.println("音频处理结果:");
            System.out.println("内容类型: " + audioResult.getContentType());
            System.out.println("内容格式: " + audioResult.getContentFormat());
            System.out.println("内容大小: " + audioResult.getSize() + " 字节");
            System.out.println("元数据: " + audioResult.getMetadata());
            System.out.println();
            
            // 格式转换 - MP3转WAV
            MultimodalContent wavResult = contentManager.convertContent(audioResult, "audio/wav");
            System.out.println("格式转换结果 (MP3 -> WAV):");
            System.out.println("新内容类型: " + wavResult.getContentType());
            System.out.println("新内容格式: " + wavResult.getContentFormat());
            System.out.println("新内容大小: " + wavResult.getSize() + " 字节");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("音频处理示例需要真实的音频文件: " + e.getMessage());
            System.out.println();
        }
    }
    
    /**
     * 示例4：视频内容处理
     */
    public void videoContentExample() {
        System.out.println("=== 视频内容处理示例 ===");
        
        // 模拟视频文件路径
        String videoFilePath = "/path/to/video/sample.mp4";
        
        try {
            // 处理MP4视频
            MultimodalContent videoResult = contentManager.processContent(videoFilePath, "video/mp4");
            System.out.println("视频处理结果:");
            System.out.println("内容类型: " + videoResult.getContentType());
            System.out.println("内容格式: " + videoResult.getContentFormat());
            System.out.println("内容大小: " + videoResult.getSize() + " 字节");
            System.out.println("元数据: " + videoResult.getMetadata());
            System.out.println();
            
            // 格式转换 - MP4转AVI
            MultimodalContent aviResult = contentManager.convertContent(videoResult, "video/avi");
            System.out.println("格式转换结果 (MP4 -> AVI):");
            System.out.println("新内容类型: " + aviResult.getContentType());
            System.out.println("新内容格式: " + aviResult.getContentFormat());
            System.out.println("新内容大小: " + aviResult.getSize() + " 字节");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("视频处理示例需要真实的视频文件: " + e.getMessage());
            System.out.println();
        }
    }
    
    /**
     * 示例5：PDF文档处理
     */
    public void pdfContentExample() {
        System.out.println("=== PDF文档处理示例 ===");
        
        // 模拟PDF文件路径
        String pdfFilePath = "/path/to/document/report.pdf";
        
        try {
            // 处理PDF文档
            MultimodalContent pdfResult = contentManager.processContent(pdfFilePath, "application/pdf");
            System.out.println("PDF处理结果:");
            System.out.println("内容类型: " + pdfResult.getContentType());
            System.out.println("内容格式: " + pdfResult.getContentFormat());
            System.out.println("内容大小: " + pdfResult.getSize() + " 字节");
            System.out.println("元数据: " + pdfResult.getMetadata());
            System.out.println();
            
            // 压缩PDF
            MultimodalContent compressedPdf = contentManager.compressContent(pdfResult);
            System.out.println("PDF压缩结果:");
            System.out.println("原始大小: " + pdfResult.getSize() + " 字节");
            System.out.println("压缩后大小: " + compressedPdf.getSize() + " 字节");
            System.out.println("压缩率: " + String.format("%.2f%%", 
                (1.0 - (double) compressedPdf.getSize() / pdfResult.getSize()) * 100));
            System.out.println();
            
            // 解压缩
            MultimodalContent decompressedPdf = contentManager.decompressContent(compressedPdf);
            System.out.println("PDF解压缩结果:");
            System.out.println("解压缩后大小: " + decompressedPdf.getSize() + " 字节");
            System.out.println("是否与原始一致: " + (decompressedPdf.getSize() == pdfResult.getSize()));
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("PDF处理示例需要真实的PDF文件: " + e.getMessage());
            System.out.println();
        }
    }
    
    /**
     * 示例6：文件内容处理
     */
    public void fileContentExample() {
        System.out.println("=== 文件内容处理示例 ===");
        
        // 处理JSON文件内容
        String jsonContent = "{\n"
                + "  \"name\": \"AI技术文档\",\n"
                + "  \"version\": \"1.0\",\n"
                + "  \"sections\": [\n"
                + "    {\"title\": \"机器学习\", \"content\": \"ML基础\"},\n"
                + "    {\"title\": \"深度学习\", \"content\": \"DL进阶\"}\n"
                + "  ]\n"
                + "}";
        
        MultimodalContent jsonResult = contentManager.processContent(jsonContent, "application/json");
        System.out.println("JSON文件处理结果:");
        System.out.println("内容类型: " + jsonResult.getContentType());
        System.out.println("内容格式: " + jsonResult.getContentFormat());
        System.out.println("内容大小: " + jsonResult.getSize() + " 字节");
        System.out.println("元数据: " + jsonResult.getMetadata());
        System.out.println();
        
        // 处理XML文件内容
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<configuration>\n"
                + "  <database>\n"
                + "    <host>localhost</host>\n"
                + "    <port>3306</port>\n"
                + "    <name>ai_db</name>\n"
                + "  </database>\n"
                + "  <ai>\n"
                + "    <model>gpt-4</model>\n"
                + "    <maxTokens>2000</maxTokens>\n"
                + "  </ai>\n"
                + "</configuration>";
        
        MultimodalContent xmlResult = contentManager.processContent(xmlContent, "application/xml");
        System.out.println("XML文件处理结果:");
        System.out.println("内容类型: " + xmlResult.getContentType());
        System.out.println("内容格式: " + xmlResult.getContentFormat());
        System.out.println("内容大小: " + xmlResult.getSize() + " 字节");
        System.out.println();
    }
    
    /**
     * 示例7：批量内容处理
     */
    public void batchContentProcessingExample() {
        System.out.println("=== 批量内容处理示例 ===");
        
        // 准备多种类型的内容
        List<Map<String, String>> contents = Arrays.asList(
            Map.of("content", "第一段文本内容", "mimeType", "text/plain"),
            Map.of("content", "# 标题\n\n第二段Markdown内容", "mimeType", "text/markdown"),
            Map.of("content", "{\"key\": \"value\"}", "mimeType", "application/json"),
            Map.of("content", "<root><item>XML内容</item></root>", "mimeType", "application/xml")
        );
        
        System.out.println("批量处理 " + contents.size() + " 个内容:");
        
        for (int i = 0; i < contents.size(); i++) {
            Map<String, String> contentInfo = contents.get(i);
            try {
                MultimodalContent result = contentManager.processContent(
                    contentInfo.get("content"), 
                    contentInfo.get("mimeType")
                );
                System.out.println("内容 " + (i + 1) + ":");
                System.out.println("  类型: " + result.getContentType());
                System.out.println("  格式: " + result.getContentFormat());
                System.out.println("  大小: " + result.getSize() + " 字节");
            } catch (Exception e) {
                System.out.println("内容 " + (i + 1) + " 处理失败: " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    /**
     * 示例8：内容验证
     */
    public void contentValidationExample() {
        System.out.println("=== 内容验证示例 ===");
        
        // 验证文本内容
        String validText = "这是一个有效的文本内容";
        boolean isTextValid = contentManager.validateContent(validText, "text/plain");
        System.out.println("文本内容验证结果: " + isTextValid);
        
        // 验证过大的内容
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            largeContent.append("这是一个很长的文本内容，用于测试大小限制。");
        }
        boolean isLargeContentValid = contentManager.validateContent(largeContent.toString(), "text/plain");
        System.out.println("大内容验证结果: " + isLargeContentValid);
        
        // 验证不支持的格式
        boolean isUnsupportedValid = contentManager.validateContent("some content", "application/unsupported");
        System.out.println("不支持的格式验证结果: " + isUnsupportedValid);
        System.out.println();
    }
    
    /**
     * 示例9：获取处理器信息
     */
    public void handlerInformationExample() {
        System.out.println("=== 处理器信息示例 ===");
        
        // 获取所有支持的类型
        var supportedTypes = contentManager.getSupportedTypes();
        System.out.println("支持的内容类型:");
        supportedTypes.forEach(type -> System.out.println("- " + type));
        System.out.println();
        
        // 获取处理器信息
        var handlerInfo = contentManager.getHandlerInfo();
        System.out.println("处理器信息:");
        handlerInfo.forEach((type, info) -> {
            System.out.println("类型: " + type);
            System.out.println("  支持的格式: " + info.get("supportedFormats"));
            System.out.println("  最大大小: " + info.get("maxSize"));
            System.out.println("  处理器类: " + info.get("handlerClass"));
        });
        System.out.println();
    }
    
    /**
     * 运行所有示例
     */
    public void runAllExamples() {
        System.out.println("=== 开始运行多模态内容处理示例 ===\n");
        
        textContentExample();
        imageContentExample();
        audioContentExample();
        videoContentExample();
        pdfContentExample();
        fileContentExample();
        batchContentProcessingExample();
        contentValidationExample();
        handlerInformationExample();
        
        System.out.println("=== 所有多模态示例运行完成 ===");
    }
}