package sideproject.madeleinelove.service;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableMongoRepositories(basePackageClasses = {WhitePostRepository.class})
public class WhitePostServiceTest {

    @Autowired
    private WhitePostService whitePostService;

    @Autowired
    private WhitePostRepository whitePostRepository;

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
    public void testSaveWhitePost() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "This is a white post test", 1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        assertWhitePost(savedWhitePost, "test1", "testNickName", "This is a white post test", 1, 0);
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - null 닉네임 설정 성공")
    public void testSaveWhitePostWithNullNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto(null, "This is a test content with null nickname", 1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        assertWhitePost(savedWhitePost, "test1", "레니", "This is a test content with null nickname", 1, 0);
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - empty 닉네임 설정 성공")
    public void testSaveWhitePostWithEmptyNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("", "This is a test content with empty nickname", 1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        assertWhitePost(savedWhitePost, "test1", "레니", "This is a test content with empty nickname", 1, 0);
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - blank 닉네임 설정 성공")
    public void testSaveWhitePostWithBlankNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto(" ", "This is a test content with blank nickname", 1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        assertWhitePost(savedWhitePost, "test1", "레니", "This is a test content with blank nickname", 1, 0);
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - long 닉네임 예외처리")
    public void testSaveWhitePostWithLongNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("NickNameExceedsTwentyCharacters", "This is a test content", 1);

        // When
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("닉네임은 20자 이하이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - null content 예외처리")
    public void testSaveWhitePostWithNullContent() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", null, 1);

        // When
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("내용 작성은 필수입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - empty content 예외처리 성공")
    public void testSaveWhitePostWithEmptyContent() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "", 1);

        // When
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("내용 작성은 필수입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - blank content 예외처리 성공")
    public void testSaveWhitePostWithBlankContent() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", " ", 1);

        // When
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("내용 작성은 필수입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - long content 예외처리")
    public void testSaveWhitePostWithLongContent() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "a".repeat(501), 1);

        // When
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("내용은 500자 이하이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - null method 예외처리")
    public void testSaveWhitePostWithNullFillMethod() {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "This is a test content", null);

        // When
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            whitePostService.saveWhitePost("test1", whiteRequestDto);
        });

        // Then
        assertEquals("채우기 방법은 필수입니다.", exception.getMessage());
    }
}
