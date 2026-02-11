package com.fw.know.go.document.domain.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 24/11/2025 上午9:47
 * @Author Leo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RagChatService {

    private final EmbeddingModel embeddingModel;

    private final VectorDatasourceService vectorDatasourceService;

    private final ChatClient chatClient;

    private final CacheManager cacheManager;

    /**
     * LLM 缓存，缓存键为问题字符串，值为模型生成的结果
     */
    private Cache<String, String> llmCache;

    @PostConstruct
    public void init(){
        QuickConfig llmQc = QuickConfig.newBuilder(":llm:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(24))
                .localExpire(Duration.ofHours(1))
                .localLimit(5000)
                .syncLocal(true)
                .build();
        llmCache = cacheManager.getOrCreateCache(llmQc);
    }

    public String chatWithKnowledgeBase(String question, String model){
        // 1. 从向量数据库中查询相关文档(最相似的3个文档)
        List<Document> similarDocuments = vectorDatasourceService.similaritySearch(question, 3, 0.75f);
        // 2. 构建提示模板
        String context = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        // 3. 调用 LLM 模型
        String prompt = String.format("""
            请根据以下上下文信息回答用户的问题。如果上下文没有相关信息，直接说"我不知道"。
            
            上下文：
            %s
            
            用户问题：%s
            """, context, question);
        // 4. 构建缓存Key
        ChatOptions options = ChatOptions.builder().model(model).build();
        String cacheKey = buildCacheKey(question, similarDocuments, options);
        // 5. 从缓存中获取模型生成的结果
        String cachedResult = llmCache.get(cacheKey);
        if (cachedResult != null) {
            log.info("从缓存中获取模型生成的结果: {}", cachedResult);
            return cachedResult;
        }
        // 6. 返回模型生成的结果
        String content = chatClient.prompt().user(prompt).options(options).call().content();
        // 7. 缓存模型生成的结果
        llmCache.put(cacheKey, content);
        return content;
    }

    /**
     * 构建缓存Key
     * @param question 用户问题
     * @param documents 相关文档
     * @param chatOptions 聊天选项
     * @return 缓存Key
     */
    private String buildCacheKey(String question, List<Document> documents, ChatOptions chatOptions){
        // 1. 查询归一化
        String normalizedQuestion = question.trim().toLowerCase().replaceAll("\\s+", "");
        // 2. 生成上下文指纹
        String contextFingerprint = this.generateContextFingerprint(documents);
        // 3. 生成LLM参数签名
        String llmSignature = this.generateLlmSignature(chatOptions);
        // 4. 拼接哈希
        return DigestUtils.md5DigestAsHex((normalizedQuestion + "|" + contextFingerprint + "|" + llmSignature).getBytes());
    }

    /**
     * 生成上下文指纹
     * @param retrievedDocs 检索到的文档
     * @return 上下文指纹
     */
    private String generateContextFingerprint(List<Document> retrievedDocs) {
        StringBuilder sb = new StringBuilder();
        for (Document doc : retrievedDocs) {
            String contentSub = doc.getFormattedContent().length() > 200 ? doc.getFormattedContent().substring(0,
                    200) : doc.getFormattedContent();
            sb.append(doc.getId()).append("_").append(contentSub).append("|");
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }

    /**
     * 生成LLM参数签名
     * @param options LLM参数
     * @return LLM参数签名
     */
    private String generateLlmSignature(ChatOptions options) {
        return String.format("%s|%.1f|%d",
                options.getModel(),
                options.getTemperature(),
                options.getMaxTokens());
    }

}
