package sideproject.madeleinelove.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.service.WhitePostService;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(WhitePostController.class)
public class WhitePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WhitePostService whitePostService;

    private WhiteRequestDto createWhiteRequestDto(String nickName, String content, Integer fillMethod) {
        WhiteRequestDto whiteRequestDto = new WhiteRequestDto();
        whiteRequestDto.setNickName(nickName);
        whiteRequestDto.setContent(content);
        whiteRequestDto.setFillMethod(fillMethod);
        return whiteRequestDto;
    }

    private ResultActions performPostRequest(String url, String userId, Object content) throws Exception {
        return mockMvc.perform(post(url)
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content)));
    }

    @Test
    @DisplayName("WhitePost 저장 테스트 - long 닉네임 예외처리")
    public void testSaveWhitePostWithLongNickName() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("NickNameExceedsTwentyCharacters", "This is a test content", 1);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("닉네임은 12자 이하이어야 합니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nickName").value("닉네임은 12자 이하이어야 합니다."));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - null content 예외처리")
    public void testSaveWhitePostWithNullContent() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", null, 1);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("내용 작성은 필수입니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("내용 작성은 필수입니다."));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - empty content 예외처리")
    public void testSaveWhitePostWithEmptyContent() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "", 1);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("내용 작성은 필수입니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("내용 작성은 필수입니다."));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - blank content 예외처리")
    public void testSaveWhitePostWithBlankContent() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", " ", 1);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("내용 작성은 필수입니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("내용 작성은 필수입니다."));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - long content 예외처리")
    public void testSaveWhitePostWithLongContent() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "a".repeat(501), 1);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("내용은 500자 이하이어야 합니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("내용은 500자 이하이어야 합니다."));
    }

    @Test
    @DisplayName("WhitePost 예외 테스트 - null method 예외처리")
    public void testSaveWhitePostWithNullFillMethod() throws Exception {
        // Given
        WhiteRequestDto whiteRequestDto = createWhiteRequestDto("testNickName", "This is a test content", null);

        given(whitePostService.saveWhitePost(anyString(), any(WhiteRequestDto.class)))
                .willThrow(new ConstraintViolationException("채우기 방법은 필수입니다.", null));

        // When & Then
        performPostRequest("/white", "test1", whiteRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fillMethod").value("채우기 방법은 필수입니다."));
    }
}
