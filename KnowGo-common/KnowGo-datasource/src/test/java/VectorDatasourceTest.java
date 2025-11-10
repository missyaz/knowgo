import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.know.go.ai.configuration.AiConfiguration;
import com.fw.know.go.datasource.VectorDatasourceService;
import com.fw.know.go.datasource.VectorDatasourceServiceImpl;
import com.fw.know.go.datasource.configuration.DatasourceConfiguration;
import com.fw.know.go.datasource.configuration.VectorDatasourceProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.chroma.autoconfigure.ChromaApiProperties;
import org.springframework.ai.vectorstore.chroma.autoconfigure.ChromaVectorStoreAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Date 5/11/2025 下午4:40
 * @Author Leo
 */
@SpringBootTest(classes = {
        DatasourceConfiguration.class,
        AiConfiguration.class,
        ChromaVectorStoreAutoConfiguration.class
})
@ActiveProfiles("test")
public class VectorDatasourceTest {

    @Autowired
    private VectorDatasourceService vectorDatasourceService;

    private String testDocumentId;
    private String testTitle = "Test Document";
    private String testContent = "This is a test document.";
    private String testAuthor = "Test Author";
    private Date testDate;

    @BeforeEach
    public void setup() {
        // 清理之前的测试数据
        vectorDatasourceService.clear();
        testDate = new Date();
        testDocumentId = IdUtil.fastUUID();
    }

    @AfterEach
    public void teardown() {
        // 测试结束后清理数据
//        vectorDatasourceService.clear();
    }

    @Test
    public void testAddDocument() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", testTitle);
        metadata.put("author", testAuthor);
        metadata.put("date", testDate);
        metadata.put("type", "document");
        vectorDatasourceService.addDocument(testDocumentId, testContent, metadata);
    }

    @Test
    public void searchDocument() {
        String query = "Test Document";
        List<Document> documents = vectorDatasourceService.similaritySearch(query);
        Assert.isTrue(documents.size() == 1, "搜索结果应该包含1个文档");
        for (Document document : documents) {
            Assert.isTrue(document.getMetadata().get("title").equals("Test Document"), "文档标题应该是Test Document");
            Assert.isTrue(document.getMetadata().get("author").equals("Test Author"), "文档作者应该是Test Author");
            Assert.isTrue(document.getMetadata().get("type").equals("document"), "文档类型应该是document");
            assertNotNull(document.getText());
            Assert.isTrue(document.getText().equals("This is a test document."), "文档内容应该是This is a test document.");
        }
    }
}
