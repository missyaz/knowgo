package com.fw.know.go.document.domain.service;

import com.fw.know.go.datasource.VectorDatasourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

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
        // 4. 返回模型生成的结果
        return chatClient.prompt().user(prompt).call().content();
    }
}
