package sideproject.madeleinelove.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import sideproject.madeleinelove.service.WhitePostService;
import java.util.HashMap;
import java.util.Map;

@RestController
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
        } catch (IllegalArgumentException e){
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

}