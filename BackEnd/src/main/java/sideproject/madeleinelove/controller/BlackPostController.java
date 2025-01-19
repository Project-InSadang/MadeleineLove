package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.BlackPostService;
import sideproject.madeleinelove.service.TokenServiceImpl;

import java.util.HashMap;
import java.util.Map;

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

    @DeleteMapping("/black/{postId}")
    public ResponseEntity<?> deleteBlackPost(HttpServletRequest request, HttpServletResponse response,
                                             @Valid @RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable String postId) {
        try{
            TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
            blackPostService.deleteBlackPost(request, response, accessTokenToUse.getAccessToken(), postId);
            return ApiResponse.onSuccess(SuccessStatus._DELETED, accessTokenToUse);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

}
