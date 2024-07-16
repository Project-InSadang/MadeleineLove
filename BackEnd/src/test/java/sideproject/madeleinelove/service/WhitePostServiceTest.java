package sideproject.madeleinelove.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableMongoRepositories(basePackageClasses = {WhitePostRepository.class})
public class WhitePostServiceTest {

    @Autowired
    private WhitePostService whitePostService;

    @Autowired
    private WhitePostRepository whitePostRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        whitePostRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        whitePostRepository.deleteAll();
    }

    private WhiteRequestDto createWhiteRequestDto(String nickName, String content, Integer fillMethod) {
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setNickName(nickName);
        whiteRequestDto.setContent(content);
        whiteRequestDto.setFillMethod(fillMethod);
        return whiteRequestDto;
    }

    private void assertWhitePost(WhitePost savedWhitePost, String userId, String nickName, String content, Integer fillMethod, Integer likeCount) {
        Optional<WhitePost> retrievedPost = whitePostRepository.findById(savedWhitePost.getPostId());
        assertThat(retrievedPost.isPresent()).isTrue();
        WhitePost post = retrievedPost.get();
        assertThat(post.getUserId()).isEqualTo(userId);
        assertThat(post.getNickName()).isEqualTo(nickName);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getFillMethod()).isEqualTo(fillMethod);
        assertThat(post.getLikeCount()).isEqualTo(likeCount);
    }

    private void assertErrorResponse(ResponseEntity<Map> response, HttpStatus expectedStatus, String expectedMessage, String field) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody().get(field));
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - 성공")
    public void testSaveWhitePost() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "This is a white post test", 1);

        // When & Then
        mockMvc.perform(post("/white")
                        .header("userId", "test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(whiteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("test1"))
                .andExpect(jsonPath("$.nickName").value("testNickName"))
                .andExpect(jsonPath("$.content").value("This is a white post test"))
                .andExpect(jsonPath("$.fillMethod").value(1))
                .andExpect(jsonPath("$.likeCount").value(0));
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - null 닉네임 설정 성공")
    public void testSaveWhitePostWithNullNickName() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto(null, "This is a test content with null nickname", 1);

        // When & Then
        mockMvc.perform(post("/white")
                        .header("userId", "test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(whiteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("test1"))
                .andExpect(jsonPath("$.nickName").value("레니"))
                .andExpect(jsonPath("$.content").value("This is a test content with null nickname"))
                .andExpect(jsonPath("$.fillMethod").value(1))
                .andExpect(jsonPath("$.likeCount").value(0));
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - empty 닉네임 설정 성공")
    public void testSaveWhitePostWithEmptyNickName() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("", "This is a test content with empty nickname", 1);

        // When & Then
        mockMvc.perform(post("/white")
                        .header("userId", "test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(whiteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("test1"))
                .andExpect(jsonPath("$.nickName").value("레니"))
                .andExpect(jsonPath("$.content").value("This is a test content with empty nickname"))
                .andExpect(jsonPath("$.fillMethod").value(1))
                .andExpect(jsonPath("$.likeCount").value(0));
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - blank 닉네임 설정 성공")
    public void testSaveWhitePostWithBlankNickName() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("  ", "This is a test content with blank nickname", 1);

        // When & Then
        mockMvc.perform(post("/white")
                        .header("userId", "test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(whiteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("test1"))
                .andExpect(jsonPath("$.nickName").value("레니"))
                .andExpect(jsonPath("$.content").value("This is a test content with blank nickname"))
                .andExpect(jsonPath("$.fillMethod").value(1))
                .andExpect(jsonPath("$.likeCount").value(0));
    }

}