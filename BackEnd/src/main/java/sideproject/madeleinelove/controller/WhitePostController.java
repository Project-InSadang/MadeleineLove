package sideproject.madeleinelove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.base.ApiResponse;
import sideproject.madeleinelove.base.SuccessStatus;
import sideproject.madeleinelove.dto.PagedResponse;
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.exception.GlobalExceptionHandler;
import sideproject.madeleinelove.service.WhitePostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WhitePostController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final WhitePostService whitePostService;

    public WhitePostController(WhitePostService whitePostService) {
        this.whitePostService = whitePostService;
    }

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
    public ResponseEntity<ApiResponse<WhitePost>> createWhitePost(
            HttpServletRequest request, HttpServletResponse response,
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody WhiteRequestDto whiteRequestDto) {

        WhitePost savedWhitePost = whitePostService.saveWhitePost(request, response, authorizationHeader, whiteRequestDto);
        return ApiResponse.onSuccess(SuccessStatus._CREATED);
    }

}