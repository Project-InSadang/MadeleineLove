package sideproject.madeleinelove.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import sideproject.madeleinelove.service.BlackPostService;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BlackPostController {
    private final BlackPostService blackPostService;

    public BlackPostController(BlackPostService blackPostService) {
        this.blackPostService = blackPostService;
    }

    @DeleteMapping("/black/{postId}")
    public ResponseEntity<?> deleteBlackPost(
            @PathVariable String postId,
            @RequestHeader(value = "userId") String userId
    ) {
        try{
            blackPostService.deleteWhitePost(postId, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

}