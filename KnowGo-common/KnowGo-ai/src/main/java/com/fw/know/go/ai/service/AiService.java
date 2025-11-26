package com.fw.know.go.ai.service;

import com.fw.know.go.ai.service.model.*;
import java.util.List;
import java.util.Map;

/**
 * @Description AI服务抽象接口，提供统一的AI能力调用
 * @Date 24/11/2025 下午3:30
 * @Author Leo
 */
public interface AiService {

    /**
     * 文本生成
     * @param request 文本生成请求
     * @return 文本生成响应
     */
    TextGenerationResponse generateText(TextGenerationRequest request);

    /**
     * 聊天对话
     * @param request 聊天请求
     * @return 聊天响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 批量聊天对话
     * @param requests 批量聊天请求
     * @return 批量聊天响应
     */
    List<ChatResponse> chatBatch(List<ChatRequest> requests);

    /**
     * 文本嵌入
     * @param request 嵌入请求
     * @return 嵌入响应
     */
    EmbeddingResponse embed(EmbeddingRequest request);

    /**
     * 批量文本嵌入
     * @param requests 批量嵌入请求
     * @return 批量嵌入响应
     */
    List<EmbeddingResponse> embedBatch(List<EmbeddingRequest> requests);

    /**
     * 图像生成
     * @param request 图像生成请求
     * @return 图像生成响应
     */
    ImageGenerationResponse generateImage(ImageGenerationRequest request);

    /**
     * 图像理解（多模态）
     * @param request 图像理解请求
     * @return 图像理解响应
     */
    ImageUnderstandingResponse understandImage(ImageUnderstandingRequest request);

    /**
     * 音频转文本
     * @param request 音频转录请求
     * @return 音频转录响应
     */
    AudioTranscriptionResponse transcribeAudio(AudioTranscriptionRequest request);

    /**
     * 文本转音频
     * @param request 文本转语音请求
     * @return 文本转语音响应
     */
    TextToSpeechResponse textToSpeech(TextToSpeechRequest request);

    /**
     * 获取模型信息
     * @param modelName 模型名称
     * @return 模型信息
     */
    ModelInfo getModelInfo(String modelName);

    /**
     * 获取支持的模型列表
     * @return 模型列表
     */
    List<ModelInfo> getSupportedModels();

    /**
     * 健康检查
     * @return 健康状态
     */
    HealthStatus healthCheck();

    /**
     * 获取服务统计信息
     * @return 统计信息
     */
    ServiceStatistics getStatistics();
}