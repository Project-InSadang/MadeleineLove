package sideproject.madeleinelove.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sideproject.madeleinelove.service.BlackLikeService;
import sideproject.madeleinelove.service.WhiteLikeService;

@RestController
@RequestMapping("/api")
public class LikeController {
    @Autowired
    private WhiteLikeService whiteLikeService;

    @Autowired
    private BlackLikeService blackLikeService;

    @PostMapping("/{postId}/white/like")
    public ResponseEntity<String> whiteToggleLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        whiteLikeService.addLike(postObjectId, userId);
        return ResponseEntity.ok("Liked white post successfully.");
    }

    @DeleteMapping("/{postId}/white/like")
    public ResponseEntity<String> whiteToggleUnLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        whiteLikeService.removeLike(postObjectId, userId);
        return ResponseEntity.ok("Unliked white post successfully.");
    }

    @PostMapping("/{postId}/black/like")
    public ResponseEntity<String> blackToggleLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        blackLikeService.addLike(postObjectId, userId);
        return ResponseEntity.ok("Liked black post successfully.");
    }

    @DeleteMapping("/{postId}/black/like")
    public ResponseEntity<String> blackToggleUnLike(@RequestParam String userId, @PathVariable String postId) {
        ObjectId postObjectId = new ObjectId(postId);
        blackLikeService.removeLike(postObjectId, userId);
        return ResponseEntity.ok("Unliked black post successfully.");
    }
}
