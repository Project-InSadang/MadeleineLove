package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.TokenSuccessStatus;
import sideproject.madeleinelove.service.TokenService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService authService;

    // 액세스 토큰을 재발행하는 API
    @GetMapping("/reissue/access-token")
    public ResponseEntity<ApiResponse<Object>> reissueAccessToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        String accessToken = authService.reissueAccessToken(request, response);
        return ApiResponse.onSuccess(TokenSuccessStatus.CREATED_ACCESS_TOKEN, accessToken);
    }
}