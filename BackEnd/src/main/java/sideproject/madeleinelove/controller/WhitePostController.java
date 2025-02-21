package sideproject.madeleinelove.controller;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.service.WhitePostService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class WhitePostController {

    private final WhitePostService whitePostService;

    public WhitePostController(WhitePostService whitePostService) {
        this.whitePostService = whitePostService;
    }

    @GetMapping("/white/latest")
    public List<WhitePostDto> getAllPosts(
            @RequestParam(required = false) String userId
    ) {
        List<WhitePost> posts = whitePostService.getAllPosts();
        Set<ObjectId> likedPostIds = whitePostService.getUserLikedPostIds(userId);

        return posts.stream()
                .map(post -> convertToDto(post, likedPostIds))
                .collect(Collectors.toList());
    }

    private WhitePostDto convertToDto(WhitePost post, Set<ObjectId> likedPostIds) {
        WhitePostDto dto = new WhitePostDto();
        dto.setPostId(post.getPostId().toHexString());
        dto.setNickName(post.getNickName());
        dto.setContent(post.getContent());
        dto.setMethodNumber(post.getMethodNumber());
        dto.setLikeCount(post.getLikeCount());
        dto.setLikedByUser(likedPostIds.contains(post.getPostId()));
        return dto;
    }

}
