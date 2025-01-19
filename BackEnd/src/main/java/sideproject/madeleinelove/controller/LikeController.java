package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.TokenDTO;
import sideproject.madeleinelove.service.BlackLikeService;
import sideproject.madeleinelove.service.TokenServiceImpl;
import sideproject.madeleinelove.service.UserService;
import sideproject.madeleinelove.service.WhiteLikeService;

@RestController
@RequestMapping("/api/v1/like")
public class LikeController {
    @Autowired
    private WhiteLikeService whiteLikeService;

    @Autowired
    private BlackLikeService blackLikeService;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @Autowired
    private UserService userService;

    @PostMapping("/{postId}/white")
    public ResponseEntity<ApiResponse<Object>> whiteToggleLike(HttpServletRequest request, HttpServletResponse response,
                                                                  @RequestHeader("Authorization") String authorizationHeader,
                                                                  @PathVariable String postId) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        whiteLikeService.addLike(request, response, accessTokenToUse.getAccessToken(), postId);

        return ApiResponse.onSuccess(SuccessStatus._LIKE, accessTokenToUse);
    }

    @DeleteMapping("/{postId}/white")
    public ResponseEntity<ApiResponse<Object>> whiteToggleUnLike(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestHeader("Authorization") String authorizationHeader,
                                                    @PathVariable String postId) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        whiteLikeService.removeLike(request, response, accessTokenToUse.getAccessToken(), postId);

        return ApiResponse.onSuccess(SuccessStatus._UNLIKE, accessTokenToUse);
    }

    @PostMapping("/{postId}/black")
    public ResponseEntity<ApiResponse<Object>> blackToggleLike(HttpServletRequest request, HttpServletResponse response,
                                                                  @RequestHeader("Authorization") String authorizationHeader,
                                                                  @PathVariable String postId) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        blackLikeService.addLike(request, response, accessTokenToUse.getAccessToken(), postId);

        return ApiResponse.onSuccess(SuccessStatus._LIKE, accessTokenToUse);
    }

    @DeleteMapping("/{postId}/black")
    public ResponseEntity<ApiResponse<Object>> blackToggleUnLike(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestHeader("Authorization") String authorizationHeader,
                                                    @PathVariable String postId) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        blackLikeService.removeLike(request, response, accessTokenToUse.getAccessToken(), postId);
        return ApiResponse.onSuccess(SuccessStatus._UNLIKE, accessTokenToUse);
    }
}
