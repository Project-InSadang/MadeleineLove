package sideproject.madeleinelove.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> toggleLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        likeService.toggleLike(userId, postObjectId);
        return ResponseEntity.ok().build();
    }
}
