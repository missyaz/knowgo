package com.fw.know.go.ai.multimodal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 多模态内容测试类
 */
@DisplayName("多模态内容测试")
class MultimodalContentTest {
    
    private MultimodalContent multimodalContent;
    
    @BeforeEach
    void setUp() {
        multimodalContent = new MultimodalContent();
    }
    
    @Nested
    @DisplayName("构建器测试")
    class BuilderTest {
        
        @Test
        @DisplayName("应该成功构建空的多模态内容")
        void shouldBuildEmptyMultimodalContent() {
            // When
            MultimodalContent content = MultimodalContent.builder().build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).isEmpty();
            assertThat(content.hasText()).isFalse();
            assertThat(content.hasImage()).isFalse();
            assertThat(content.hasAudio()).isFalse();
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isFalse();
        }
        
        @Test
        @DisplayName("应该成功构建包含文本的内容")
        void shouldBuildContentWithText() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addText("Hello, world!")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(1);
            assertThat(content.hasText()).isTrue();
            assertThat(content.getText()).isEqualTo("Hello, world!");
            assertThat(content.hasImage()).isFalse();
            assertThat(content.hasAudio()).isFalse();
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isFalse();
        }
        
        @Test
        @DisplayName("应该成功构建包含图片的内容")
        void shouldBuildContentWithImage() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addImage("base64:image/png:abcdef123456")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(1);
            assertThat(content.hasText()).isFalse();
            assertThat(content.hasImage()).isTrue();
            assertThat(content.getImages()).hasSize(1);
            assertThat(content.getImages().get(0)).isEqualTo("base64:image/png:abcdef123456");
            assertThat(content.hasAudio()).isFalse();
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isFalse();
        }
        
        @Test
        @DisplayName("应该成功构建包含音频的内容")
        void shouldBuildContentWithAudio() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addAudio("base64:audio/mp3:abcdef123456")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(1);
            assertThat(content.hasText()).isFalse();
            assertThat(content.hasImage()).isFalse();
            assertThat(content.hasAudio()).isTrue();
            assertThat(content.getAudios()).hasSize(1);
            assertThat(content.getAudios().get(0)).isEqualTo("base64:audio/mp3:abcdef123456");
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isFalse();
        }
        
        @Test
        @DisplayName("应该成功构建包含视频的内容")
        void shouldBuildContentWithVideo() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addVideo("base64:video/mp4:abcdef123456")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(1);
            assertThat(content.hasText()).isFalse();
            assertThat(content.hasImage()).isFalse();
            assertThat(content.hasAudio()).isFalse();
            assertThat(content.hasVideo()).isTrue();
            assertThat(content.getVideos()).hasSize(1);
            assertThat(content.getVideos().get(0)).isEqualTo("base64:video/mp4:abcdef123456");
            assertThat(content.hasFile()).isFalse();
        }
        
        @Test
        @DisplayName("应该成功构建包含文件的内容")
        void shouldBuildContentWithFile() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addFile("base64:application/pdf:abcdef123456", "document.pdf")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(1);
            assertThat(content.hasText()).isFalse();
            assertThat(content.hasImage()).isFalse();
            assertThat(content.hasAudio()).isFalse();
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isTrue();
            assertThat(content.getFiles()).hasSize(1);
            assertThat(content.getFiles().get(0).getContent()).isEqualTo("base64:application/pdf:abcdef123456");
            assertThat(content.getFiles().get(0).getFileName()).isEqualTo("document.pdf");
            assertThat(content.getFiles().get(0).getFileType()).isEqualTo("application/pdf");
        }
        
        @Test
        @DisplayName("应该成功构建包含多种内容类型的内容")
        void shouldBuildContentWithMultipleTypes() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addText("Analyze this image:")
                .addImage("base64:image/png:abcdef123456")
                .addAudio("base64:audio/wav:abcdef123456")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getContents()).hasSize(3);
            assertThat(content.hasText()).isTrue();
            assertThat(content.hasImage()).isTrue();
            assertThat(content.hasAudio()).isTrue();
            assertThat(content.hasVideo()).isFalse();
            assertThat(content.hasFile()).isFalse();
            
            assertThat(content.getText()).isEqualTo("Analyze this image:");
            assertThat(content.getImages()).hasSize(1);
            assertThat(content.getAudios()).hasSize(1);
        }
        
        @Test
        @DisplayName("应该成功构建包含元数据的内容")
        void shouldBuildContentWithMetadata() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addText("Hello, world!")
                .addMetadata("author", "John Doe")
                .addMetadata("created", "2024-01-01")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getMetadata()).isNotNull();
            assertThat(content.getMetadata()).hasSize(2);
            assertThat(content.getMetadata()).containsEntry("author", "John Doe");
            assertThat(content.getMetadata()).containsEntry("created", "2024-01-01");
        }
        
        @Test
        @DisplayName("应该成功构建包含自定义属性的内容")
        void shouldBuildContentWithCustomProperties() {
            // When
            MultimodalContent content = MultimodalContent.builder()
                .addText("Hello, world!")
                .addCustomProperty("language", "en")
                .addCustomProperty("priority", "high")
                .build();
            
            // Then
            assertThat(content).isNotNull();
            assertThat(content.getCustomProperties()).isNotNull();
            assertThat(content.getCustomProperties()).hasSize(2);
            assertThat(content.getCustomProperties()).containsEntry("language", "en");
            assertThat(content.getCustomProperties()).containsEntry("priority", "high");
        }
    }
    
    @Nested
    @DisplayName("内容添加测试")
    class ContentAdditionTest {
        
        @Test
        @DisplayName("应该成功添加文本内容")
        void shouldAddTextContent() {
            // When
            multimodalContent.addText("First text");
            multimodalContent.addText("Second text");
            
            // Then
            assertThat(multimodalContent.hasText()).isTrue();
            assertThat(multimodalContent.getText()).isEqualTo("First text\nSecond text");
            assertThat(multimodalContent.getContents()).hasSize(2);
        }
        
        @Test
        @DisplayName("应该成功添加图片内容")
        void shouldAddImageContent() {
            // When
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addImage("base64:image/jpeg:image2");
            
            // Then
            assertThat(multimodalContent.hasImage()).isTrue();
            assertThat(multimodalContent.getImages()).hasSize(2);
            assertThat(multimodalContent.getImages()).containsExactly("base64:image/png:image1", "base64:image/jpeg:image2");
        }
        
        @Test
        @DisplayName("应该成功添加音频内容")
        void shouldAddAudioContent() {
            // When
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            multimodalContent.addAudio("base64:audio/wav:audio2");
            
            // Then
            assertThat(multimodalContent.hasAudio()).isTrue();
            assertThat(multimodalContent.getAudios()).hasSize(2);
            assertThat(multimodalContent.getAudios()).containsExactly("base64:audio/mp3:audio1", "base64:audio/wav:audio2");
        }
        
        @Test
        @DisplayName("应该成功添加视频内容")
        void shouldAddVideoContent() {
            // When
            multimodalContent.addVideo("base64:video/mp4:video1");
            multimodalContent.addVideo("base64:video/avi:video2");
            
            // Then
            assertThat(multimodalContent.hasVideo()).isTrue();
            assertThat(multimodalContent.getVideos()).hasSize(2);
            assertThat(multimodalContent.getVideos()).containsExactly("base64:video/mp4:video1", "base64:video/avi:video2");
        }
        
        @Test
        @DisplayName("应该成功添加文件内容")
        void shouldAddFileContent() {
            // When
            multimodalContent.addFile("base64:application/pdf:file1", "document.pdf");
            multimodalContent.addFile("base64:text/plain:file2", "notes.txt");
            
            // Then
            assertThat(multimodalContent.hasFile()).isTrue();
            assertThat(multimodalContent.getFiles()).hasSize(2);
            assertThat(multimodalContent.getFiles().get(0).getContent()).isEqualTo("base64:application/pdf:file1");
            assertThat(multimodalContent.getFiles().get(0).getFileName()).isEqualTo("document.pdf");
            assertThat(multimodalContent.getFiles().get(1).getContent()).isEqualTo("base64:text/plain:file2");
            assertThat(multimodalContent.getFiles().get(1).getFileName()).isEqualTo("notes.txt");
        }
        
        @Test
        @DisplayName("应该成功批量添加内容")
        void shouldAddContentInBatch() {
            // When
            multimodalContent.addText("Text content");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            multimodalContent.addVideo("base64:video/mp4:video1");
            multimodalContent.addFile("base64:application/pdf:file1", "document.pdf");
            
            // Then
            assertThat(multimodalContent.getContents()).hasSize(5);
            assertThat(multimodalContent.hasText()).isTrue();
            assertThat(multimodalContent.hasImage()).isTrue();
            assertThat(multimodalContent.hasAudio()).isTrue();
            assertThat(multimodalContent.hasVideo()).isTrue();
            assertThat(multimodalContent.hasFile()).isTrue();
        }
    }
    
    @Nested
    @DisplayName("内容获取测试")
    class ContentRetrievalTest {
        
        @Test
        @DisplayName("应该正确获取文本内容")
        void shouldGetTextContent() {
            // Given
            multimodalContent.addText("Hello, world!");
            
            // When & Then
            assertThat(multimodalContent.getText()).isEqualTo("Hello, world!");
            assertThat(multimodalContent.getTextContent()).isEqualTo("Hello, world!");
        }
        
        @Test
        @DisplayName("应该正确获取所有文本内容当存在多个文本")
        void shouldGetAllTextContentWhenMultipleTextsExist() {
            // Given
            multimodalContent.addText("First line");
            multimodalContent.addText("Second line");
            multimodalContent.addText("Third line");
            
            // When & Then
            assertThat(multimodalContent.getText()).isEqualTo("First line\nSecond line\nThird line");
            assertThat(multimodalContent.getTextContent()).isEqualTo("First line\nSecond line\nThird line");
        }
        
        @Test
        @DisplayName("应该正确获取图片内容")
        void shouldGetImageContent() {
            // Given
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addImage("base64:image/jpeg:image2");
            
            // When & Then
            assertThat(multimodalContent.getImages()).hasSize(2);
            assertThat(multimodalContent.getImages()).containsExactly("base64:image/png:image1", "base64:image/jpeg:image2");
            assertThat(multimodalContent.getImageContent()).isEqualTo(multimodalContent.getImages());
        }
        
        @Test
        @DisplayName("应该正确获取音频内容")
        void shouldGetAudioContent() {
            // Given
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            multimodalContent.addAudio("base64:audio/wav:audio2");
            
            // When & Then
            assertThat(multimodalContent.getAudios()).hasSize(2);
            assertThat(multimodalContent.getAudios()).containsExactly("base64:audio/mp3:audio1", "base64:audio/wav:audio2");
            assertThat(multimodalContent.getAudioContent()).isEqualTo(multimodalContent.getAudios());
        }
        
        @Test
        @DisplayName("应该正确获取视频内容")
        void shouldGetVideoContent() {
            // Given
            multimodalContent.addVideo("base64:video/mp4:video1");
            multimodalContent.addVideo("base64:video/avi:video2");
            
            // When & Then
            assertThat(multimodalContent.getVideos()).hasSize(2);
            assertThat(multimodalContent.getVideos()).containsExactly("base64:video/mp4:video1", "base64:video/avi:video2");
            assertThat(multimodalContent.getVideoContent()).isEqualTo(multimodalContent.getVideos());
        }
        
        @Test
        @DisplayName("应该正确获取文件内容")
        void shouldGetFileContent() {
            // Given
            multimodalContent.addFile("base64:application/pdf:file1", "document.pdf");
            multimodalContent.addFile("base64:text/plain:file2", "notes.txt");
            
            // When & Then
            assertThat(multimodalContent.getFiles()).hasSize(2);
            assertThat(multimodalContent.getFiles().get(0).getContent()).isEqualTo("base64:application/pdf:file1");
            assertThat(multimodalContent.getFiles().get(0).getFileName()).isEqualTo("document.pdf");
            assertThat(multimodalContent.getFiles().get(1).getContent()).isEqualTo("base64:text/plain:file2");
            assertThat(multimodalContent.getFiles().get(1).getFileName()).isEqualTo("notes.txt");
            assertThat(multimodalContent.getFileContent()).isEqualTo(multimodalContent.getFiles());
        }
        
        @Test
        @DisplayName("应该返回空列表当内容类型不存在")
        void shouldReturnEmptyListWhenContentTypeNotExists() {
            // When & Then
            assertThat(multimodalContent.getImages()).isEmpty();
            assertThat(multimodalContent.getAudios()).isEmpty();
            assertThat(multimodalContent.getVideos()).isEmpty();
            assertThat(multimodalContent.getFiles()).isEmpty();
            assertThat(multimodalContent.getText()).isEmpty();
            assertThat(multimodalContent.getTextContent()).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("内容检查测试")
    class ContentCheckTest {
        
        @Test
        @DisplayName("应该正确检查是否存在文本内容")
        void shouldCheckIfHasText() {
            // When & Then
            assertThat(multimodalContent.hasText()).isFalse();
            
            multimodalContent.addText("Text content");
            assertThat(multimodalContent.hasText()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否存在图片内容")
        void shouldCheckIfHasImage() {
            // When & Then
            assertThat(multimodalContent.hasImage()).isFalse();
            
            multimodalContent.addImage("base64:image/png:image1");
            assertThat(multimodalContent.hasImage()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否存在音频内容")
        void shouldCheckIfHasAudio() {
            // When & Then
            assertThat(multimodalContent.hasAudio()).isFalse();
            
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            assertThat(multimodalContent.hasAudio()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否存在视频内容")
        void shouldCheckIfHasVideo() {
            // When & Then
            assertThat(multimodalContent.hasVideo()).isFalse();
            
            multimodalContent.addVideo("base64:video/mp4:video1");
            assertThat(multimodalContent.hasVideo()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否存在文件内容")
        void shouldCheckIfHasFile() {
            // When & Then
            assertThat(multimodalContent.hasFile()).isFalse();
            
            multimodalContent.addFile("base64:application/pdf:file1", "document.pdf");
            assertThat(multimodalContent.hasFile()).isTrue();
        }
        
        @Test
        @DisplayName("应该正确检查是否为空")
        void shouldCheckIfIsEmpty() {
            // When & Then
            assertThat(multimodalContent.isEmpty()).isTrue();
            
            multimodalContent.addText("Text content");
            assertThat(multimodalContent.isEmpty()).isFalse();
        }
        
        @Test
        @DisplayName("应该正确获取内容数量")
        void shouldGetContentCount() {
            // When & Then
            assertThat(multimodalContent.getContentCount()).isEqualTo(0);
            
            multimodalContent.addText("Text content");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            
            assertThat(multimodalContent.getContentCount()).isEqualTo(3);
        }
    }
    
    @Nested
    @DisplayName("元数据测试")
    class MetadataTest {
        
        @Test
        @DisplayName("应该成功添加元数据")
        void shouldAddMetadata() {
            // When
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addMetadata("created", "2024-01-01");
            
            // Then
            assertThat(multimodalContent.getMetadata()).hasSize(2);
            assertThat(multimodalContent.getMetadata()).containsEntry("author", "John Doe");
            assertThat(multimodalContent.getMetadata()).containsEntry("created", "2024-01-01");
        }
        
        @Test
        @DisplayName("应该成功获取元数据")
        void shouldGetMetadata() {
            // Given
            multimodalContent.addMetadata("author", "John Doe");
            
            // When & Then
            assertThat(multimodalContent.getMetadata("author")).isEqualTo("John Doe");
            assertThat(multimodalContent.getMetadata("nonexistent")).isNull();
        }
        
        @Test
        @DisplayName("应该成功检查元数据是否存在")
        void shouldCheckIfMetadataExists() {
            // Given
            multimodalContent.addMetadata("author", "John Doe");
            
            // When & Then
            assertThat(multimodalContent.hasMetadata("author")).isTrue();
            assertThat(multimodalContent.hasMetadata("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该成功移除元数据")
        void shouldRemoveMetadata() {
            // Given
            multimodalContent.addMetadata("author", "John Doe");
            
            // When
            multimodalContent.removeMetadata("author");
            
            // Then
            assertThat(multimodalContent.hasMetadata("author")).isFalse();
            assertThat(multimodalContent.getMetadata()).isEmpty();
        }
        
        @Test
        @DisplayName("应该成功批量添加元数据")
        void shouldAddMetadataInBatch() {
            // Given
            Map<String, String> metadata = Map.of(
                "author", "John Doe",
                "created", "2024-01-01",
                "version", "1.0"
            );
            
            // When
            multimodalContent.addMetadata(metadata);
            
            // Then
            assertThat(multimodalContent.getMetadata()).hasSize(3);
            assertThat(multimodalContent.getMetadata()).containsAllEntriesOf(metadata);
        }
        
        @Test
        @DisplayName("应该成功清空元数据")
        void shouldClearMetadata() {
            // Given
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addMetadata("created", "2024-01-01");
            
            // When
            multimodalContent.clearMetadata();
            
            // Then
            assertThat(multimodalContent.getMetadata()).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("自定义属性测试")
    class CustomPropertiesTest {
        
        @Test
        @DisplayName("应该成功添加自定义属性")
        void shouldAddCustomProperty() {
            // When
            multimodalContent.addCustomProperty("language", "en");
            multimodalContent.addCustomProperty("priority", "high");
            
            // Then
            assertThat(multimodalContent.getCustomProperties()).hasSize(2);
            assertThat(multimodalContent.getCustomProperties()).containsEntry("language", "en");
            assertThat(multimodalContent.getCustomProperties()).containsEntry("priority", "high");
        }
        
        @Test
        @DisplayName("应该成功获取自定义属性")
        void shouldGetCustomProperty() {
            // Given
            multimodalContent.addCustomProperty("language", "en");
            
            // When & Then
            assertThat(multimodalContent.getCustomProperty("language")).isEqualTo("en");
            assertThat(multimodalContent.getCustomProperty("nonexistent")).isNull();
        }
        
        @Test
        @DisplayName("应该成功检查自定义属性是否存在")
        void shouldCheckIfCustomPropertyExists() {
            // Given
            multimodalContent.addCustomProperty("language", "en");
            
            // When & Then
            assertThat(multimodalContent.hasCustomProperty("language")).isTrue();
            assertThat(multimodalContent.hasCustomProperty("nonexistent")).isFalse();
        }
        
        @Test
        @DisplayName("应该成功移除自定义属性")
        void shouldRemoveCustomProperty() {
            // Given
            multimodalContent.addCustomProperty("language", "en");
            
            // When
            multimodalContent.removeCustomProperty("language");
            
            // Then
            assertThat(multimodalContent.hasCustomProperty("language")).isFalse();
            assertThat(multimodalContent.getCustomProperties()).isEmpty();
        }
        
        @Test
        @DisplayName("应该成功批量添加自定义属性")
        void shouldAddCustomPropertiesInBatch() {
            // Given
            Map<String, Object> customProperties = Map.of(
                "language", "en",
                "priority", "high",
                "source", "api"
            );
            
            // When
            multimodalContent.addCustomProperties(customProperties);
            
            // Then
            assertThat(multimodalContent.getCustomProperties()).hasSize(3);
            assertThat(multimodalContent.getCustomProperties()).containsAllEntriesOf(customProperties);
        }
        
        @Test
        @DisplayName("应该成功清空自定义属性")
        void shouldClearCustomProperties() {
            // Given
            multimodalContent.addCustomProperty("language", "en");
            multimodalContent.addCustomProperty("priority", "high");
            
            // When
            multimodalContent.clearCustomProperties();
            
            // Then
            assertThat(multimodalContent.getCustomProperties()).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("内容验证测试")
    class ContentValidationTest {
        
        @Test
        @DisplayName("应该成功验证有效的内容")
        void shouldValidateValidContent() {
            // Given
            multimodalContent.addText("Valid text content");
            multimodalContent.addImage("base64:image/png:validimage");
            
            // When & Then
            assertThatCode(() -> multimodalContent.validate())
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该成功验证空内容")
        void shouldValidateEmptyContent() {
            // When & Then
            assertThatCode(() -> multimodalContent.validate())
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该成功验证包含所有内容类型的内容")
        void shouldValidateContentWithAllTypes() {
            // Given
            multimodalContent.addText("Text content");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            multimodalContent.addVideo("base64:video/mp4:video1");
            multimodalContent.addFile("base64:application/pdf:file1", "document.pdf");
            
            // When & Then
            assertThatCode(() -> multimodalContent.validate())
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该成功验证文件内容")
        void shouldValidateFileContent() {
            // Given
            multimodalContent.addFile("base64:application/pdf:validfile", "document.pdf");
            
            // When & Then
            assertThatCode(() -> multimodalContent.validate())
                .doesNotThrowAnyException();
        }
    }
    
    @Nested
    @DisplayName("内容转换测试")
    class ContentConversionTest {
        
        @Test
        @DisplayName("应该成功转换为字符串")
        void shouldConvertToString() {
            // Given
            multimodalContent.addText("Hello, world!");
            multimodalContent.addImage("base64:image/png:image1");
            
            // When
            String result = multimodalContent.toString();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Hello, world!");
            assertThat(result).contains("base64:image/png:image1");
        }
        
        @Test
        @DisplayName("应该成功转换为摘要字符串")
        void shouldConvertToSummaryString() {
            // Given
            multimodalContent.addText("This is a long text that should be truncated in the summary...");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addAudio("base64:audio/mp3:audio1");
            
            // When
            String result = multimodalContent.toSummaryString();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("Text: This is a long text");
            assertThat(result).contains("Images: 1");
            assertThat(result).contains("Audio: 1");
            assertThat(result).doesNotContain("base64:image/png:image1");
            assertThat(result).doesNotContain("base64:audio/mp3:audio1");
        }
        
        @Test
        @DisplayName("应该成功转换为JSON字符串")
        void shouldConvertToJsonString() {
            // Given
            multimodalContent.addText("Hello, world!");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addCustomProperty("language", "en");
            
            // When
            String result = multimodalContent.toJsonString();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).contains("\"text\":\"Hello, world!\"");
            assertThat(result).contains("\"images\":[\"base64:image/png:image1\"]");
            assertThat(result).contains("\"author\":\"John Doe\"");
            assertThat(result).contains("\"language\":\"en\"");
        }
        
        @Test
        @DisplayName("应该成功转换为Map")
        void shouldConvertToMap() {
            // Given
            multimodalContent.addText("Hello, world!");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addCustomProperty("language", "en");
            
            // When
            Map<String, Object> result = multimodalContent.toMap();
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).containsEntry("text", "Hello, world!");
            assertThat(result).containsEntry("images", List.of("base64:image/png:image1"));
            assertThat(result).containsEntry("author", "John Doe");
            assertThat(result).containsEntry("language", "en");
        }
    }
    
    @Nested
    @DisplayName("内容复制测试")
    class ContentCopyTest {
        
        @Test
        @DisplayName("应该成功创建副本")
        void shouldCreateCopy() {
            // Given
            multimodalContent.addText("Hello, world!");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addCustomProperty("language", "en");
            
            // When
            MultimodalContent copy = multimodalContent.copy();
            
            // Then
            assertThat(copy).isNotNull();
            assertThat(copy).isNotSameAs(multimodalContent);
            assertThat(copy.getText()).isEqualTo("Hello, world!");
            assertThat(copy.getImages()).containsExactly("base64:image/png:image1");
            assertThat(copy.getMetadata()).containsEntry("author", "John Doe");
            assertThat(copy.getCustomProperties()).containsEntry("language", "en");
        }
        
        @Test
        @DisplayName("应该成功创建深副本")
        void shouldCreateDeepCopy() {
            // Given
            multimodalContent.addText("Hello, world!");
            multimodalContent.addImage("base64:image/png:image1");
            multimodalContent.addMetadata("author", "John Doe");
            multimodalContent.addCustomProperty("language", "en");
            
            // When
            MultimodalContent deepCopy = multimodalContent.deepCopy();
            
            // Then
            assertThat(deepCopy).isNotNull();
            assertThat(deepCopy).isNotSameAs(multimodalContent);
            assertThat(deepCopy.getText()).isEqualTo("Hello, world!");
            assertThat(deepCopy.getImages()).containsExactly("base64:image/png:image1");
            assertThat(deepCopy.getMetadata()).containsEntry("author", "John Doe");
            assertThat(deepCopy.getCustomProperties()).containsEntry("language", "en");
            
            // 验证深复制 - 修改原对象不应影响副本
            multimodalContent.addText("Modified text");
            multimodalContent.addMetadata("modified", "true");
            
            assertThat(deepCopy.getText()).isEqualTo("Hello, world!");
            assertThat(deepCopy.hasMetadata("modified")).isFalse();
        }
    }
    
    @Nested
    @DisplayName("内容合并测试")
    class ContentMergeTest {
        
        @Test
        @DisplayName("应该成功合并两个内容对象")
        void shouldMergeTwoContentObjects() {
            // Given
            MultimodalContent content1 = MultimodalContent.builder()
                .addText("First text")
                .addImage("base64:image/png:image1")
                .addMetadata("author", "John Doe")
                .build();
            
            MultimodalContent content2 = MultimodalContent.builder()
                .addText("Second text")
                .addAudio("base64:audio/mp3:audio1")
                .addMetadata("created", "2024-01-01")
                .build();
            
            // When
            MultimodalContent merged = content1.merge(content2);
            
            // Then
            assertThat(merged).isNotNull();
            assertThat(merged.getText()).isEqualTo("First text\nSecond text");
            assertThat(merged.getImages()).containsExactly("base64:image/png:image1");
            assertThat(merged.getAudios()).containsExactly("base64:audio/mp3:audio1");
            assertThat(merged.getMetadata()).containsEntry("author", "John Doe");
            assertThat(merged.getMetadata()).containsEntry("created", "2024-01-01");
        }
        
        @Test
        @DisplayName("应该成功合并多个内容对象")
        void shouldMergeMultipleContentObjects() {
            // Given
            MultimodalContent content1 = MultimodalContent.builder()
                .addText("First text")
                .addImage("base64:image/png:image1")
                .build();
            
            MultimodalContent content2 = MultimodalContent.builder()
                .addText("Second text")
                .addAudio("base64:audio/mp3:audio1")
                .build();
            
            MultimodalContent content3 = MultimodalContent.builder()
                .addText("Third text")
                .addVideo("base64:video/mp4:video1")
                .build();
            
            // When
            MultimodalContent merged = content1.merge(Arrays.asList(content2, content3));
            
            // Then
            assertThat(merged).isNotNull();
            assertThat(merged.getText()).isEqualTo("First text\nSecond text\nThird text");
            assertThat(merged.getImages()).containsExactly("base64:image/png:image1");
            assertThat(merged.getAudios()).containsExactly("base64:audio/mp3:audio1");
            assertThat(merged.getVideos()).containsExactly("base64:video/mp4:video1");
        }
        
        @Test
        @DisplayName("应该成功合并空内容对象")
        void shouldMergeEmptyContentObjects() {
            // Given
            MultimodalContent content1 = MultimodalContent.builder().build();
            MultimodalContent content2 = MultimodalContent.builder().build();
            
            // When
            MultimodalContent merged = content1.merge(content2);
            
            // Then
            assertThat(merged).isNotNull();
            assertThat(merged.isEmpty()).isTrue();
        }
    }
    
    @Nested
    @DisplayName("错误处理测试")
    class ErrorHandlingTest {
        
        @Test
        @DisplayName("应该正确处理空参数")
        void shouldHandleNullParameters() {
            // When & Then
            assertThatCode(() -> multimodalContent.addText(null))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addImage(null))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addAudio(null))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addVideo(null))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addFile(null, "file.txt"))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addFile("content", null))
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该正确处理无效文件格式")
        void shouldHandleInvalidFileFormat() {
            // When & Then
            assertThatCode(() -> multimodalContent.addFile("invalid content", "file.txt"))
                .doesNotThrowAnyException();
            
            assertThatCode(() -> multimodalContent.addFile("base64:invalid:format", "file.txt"))
                .doesNotThrowAnyException();
        }
        
        @Test
        @DisplayName("应该正确处理合并空参数")
        void shouldHandleMergeWithNullParameter() {
            // Given
            MultimodalContent content = MultimodalContent.builder()
                .addText("Text content")
                .build();
            
            // When & Then
            assertThatThrownBy(() -> content.merge(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("不能合并空内容");
            
            assertThatThrownBy(() -> content.merge((List<MultimodalContent>) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("不能合并空内容列表");
        }
    }
}