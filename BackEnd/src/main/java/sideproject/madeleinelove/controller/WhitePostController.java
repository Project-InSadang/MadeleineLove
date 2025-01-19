package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.*;
import sideproject.madeleinelove.service.TokenServiceImpl;
import sideproject.madeleinelove.service.WhitePostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WhitePostController {

    @Autowired
    private WhitePostService whitePostService;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @DeleteMapping("/white/{postId}")
    public ResponseEntity<?> deleteWhitePost(
            @PathVariable String postId,
            @RequestHeader(value = "userId") String userId
    ) {
        try{
            whitePostService.deleteWhitePost(postId, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
          
    @GetMapping("/white/post")
    public ResponseEntity<PagedResponse<WhitePostDto>> getPosts(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String userId) {
        List<WhitePostDto> dtos = whitePostService.getPosts(sort, cursor, size, userId);

        String nextCursor = whitePostService.getNextCursor(dtos, sort);

        PagedResponse<WhitePostDto> response = new PagedResponse<>();
        response.setData(dtos);
        response.setNextCursor(nextCursor);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/white")
    public ResponseEntity<ApiResponse<Object>> createWhitePost(HttpServletRequest request, HttpServletResponse response,
                                                                  @Valid @RequestHeader("Authorization") String authorizationHeader,
                                                                  @Valid @RequestBody WhiteRequestDto whiteRequestDto) {

        TokenDTO.TokenResponse accessTokenToUse = tokenServiceImpl.validateAccessToken(request, response, authorizationHeader);
        whitePostService.saveWhitePost(request, response, accessTokenToUse.getAccessToken(), whiteRequestDto);

        return ApiResponse.onSuccess(SuccessStatus._CREATED, accessTokenToUse);
    }

}