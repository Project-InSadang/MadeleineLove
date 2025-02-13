package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.TokenService;
import sideproject.madeleinelove.service.TokenServiceImpl;
import sideproject.madeleinelove.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    // 유저 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestHeader("Authorization") String authorizationHeader) {


        TokenDTO.TokenResponse accessTokenToUse = tokenService.validateAccessToken(request, response, authorizationHeader);
        userService.logout(request, response, accessTokenToUse.getAccessToken());

        return ApiResponse.onSuccess(SuccessStatus.SUCCESS_LOGOUT);
    }

}
