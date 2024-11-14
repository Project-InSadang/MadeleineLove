package sideproject.madeleinelove.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.service.WhiteLikeService;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private WhiteLikeService whiteLikeService;

    @PostMapping("/{postId}/white/like")
    public ResponseEntity<String> toggleLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        whiteLikeService.likePost(userId, postObjectId); //좋아요 추가
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/white/unlike")
    public ResponseEntity<String> toggleUnLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        whiteLikeService.unlikePost(userId, postObjectId); //좋아요 취소
        return ResponseEntity.ok().build();
    }
}
