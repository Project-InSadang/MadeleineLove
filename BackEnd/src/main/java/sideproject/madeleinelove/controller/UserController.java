package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.TokenServiceImpl;
import sideproject.madeleinelove.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Object>> withdraw(HttpServletRequest request, HttpServletResponse response,
                                                        @Valid @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        userService.withdraw(request, response, accessTokenToUse.getAccessToken());

        return ApiResponse.onSuccess(SuccessStatus._WITHDRAW, null);
    }
}