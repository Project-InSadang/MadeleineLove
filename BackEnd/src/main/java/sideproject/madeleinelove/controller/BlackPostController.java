package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.BlackPostService;
import sideproject.madeleinelove.service.TokenServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class BlackPostController {

    @Autowired
    private BlackPostService blackPostService;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @PostMapping("/black")
    public ResponseEntity<ApiResponse<Object>> createBlackPost(HttpServletRequest request, HttpServletResponse response,
                                                    @Valid @RequestHeader("Authorization") String authorizationHeader,
                                                    @Valid @RequestBody BlackPostDto blackPostDto) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        blackPostService.saveBlackPost(request, response, accessTokenToUse.getAccessToken(), blackPostDto);

        return ApiResponse.onSuccess(SuccessStatus._CREATED, accessTokenToUse);
    }

}
