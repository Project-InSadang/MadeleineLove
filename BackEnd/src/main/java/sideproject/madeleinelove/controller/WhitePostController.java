package sideproject.madeleinelove.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.PagedResponse;
import sideproject.madeleinelove.dto.WhitePostDto;
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
    public ResponseEntity<?> createWhitePost(
            @RequestHeader("userId") String userId,
            @Valid @RequestBody WhiteRequestDto whiteRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            WhitePost savedWhitePost = whitePostService.saveWhitePost(userId, whiteRequestDto);
            return new ResponseEntity<>(savedWhitePost, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

}