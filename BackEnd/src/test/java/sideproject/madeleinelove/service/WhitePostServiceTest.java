package sideproject.madeleinelove.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
public class WhitePostServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

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

    @Test
    @DisplayName("WhitePost 저장 테스트 - 성공")
    public void testSaveWhitePost() {
        // Given
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setNickName("testNickName");
        whiteRequestDto.setContent("This is a white post test");
        whiteRequestDto.setFillMethod(1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        Optional<WhitePost> retrievedPost = whitePostRepository.findById(savedWhitePost.getPostId());
        assertThat(retrievedPost.isPresent()).isTrue();
        assertThat(retrievedPost.get().getUserId()).isEqualTo("test1");
        assertThat(retrievedPost.get().getNickName()).isEqualTo("testNickName");
        assertThat(retrievedPost.get().getContent()).isEqualTo("This is a white post test");
        assertThat(retrievedPost.get().getFillMethod()).isEqualTo(1);
        assertThat(retrievedPost.get().getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - 기본 닉네임 설정 성공")
    public void testSaveWhitePostWithNullNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setContent("This is a test content with null nickname");
        whiteRequestDto.setFillMethod(1);

        // When
        WhitePost savedWhitePost = whitePostService.saveWhitePost("test1", whiteRequestDto);

        // Then
        Optional<WhitePost> retrievedPost = whitePostRepository.findById(savedWhitePost.getPostId());
        assertThat(retrievedPost.isPresent()).isTrue();
        assertThat(retrievedPost.get().getUserId()).isEqualTo("test1");
        assertThat(retrievedPost.get().getNickName()).isEqualTo("레니");
        assertThat(retrievedPost.get().getContent()).isEqualTo("This is a test content with null nickname");
        assertThat(retrievedPost.get().getFillMethod()).isEqualTo(1);
        assertThat(retrievedPost.get().getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - 긴 닉네임 예외처리 성공")
    public void testSaveWhitePostWithLongNickName() {
        // Given
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setNickName("NickNameExceedsTwentyCharacters");
        whiteRequestDto.setContent("This is a test content");
        whiteRequestDto.setFillMethod(1);

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity("/whitePost", whiteRequestDto, Map.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("닉네임은 20자 이하이어야 합니다.", response.getBody().get("nickName"));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - 긴 내용 예외처리 성공")
    public void testSaveWhitePostWithLongContent() {
        // Given
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        String longContent = "a".repeat(501);
        whiteRequestDto.setContent(longContent);
        whiteRequestDto.setNickName("testNickName");
        whiteRequestDto.setFillMethod(1);

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity("/whitePost", whiteRequestDto, Map.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("내용은 500자 이하이어야 합니다.", response.getBody().get("content"));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - null method 예외처리 성공")
    public void testSaveWhitePostWithNullFillMethod() {
        // Given
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setNickName("testNickName");
        whiteRequestDto.setContent("This is a test content");
        whiteRequestDto.setFillMethod(null);

        // When
        ResponseEntity<Map> response = restTemplate.postForEntity("/whitePost", whiteRequestDto, Map.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("채우기 방법은 필수입니다.", response.getBody().get("fillMethod"));
    }
}
