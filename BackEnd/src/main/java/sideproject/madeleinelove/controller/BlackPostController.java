package sideproject.madeleinelove.controller;

import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.service.BlackPostService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class BlackPostController {

    private final BlackPostService blackPostService;

    public BlackPostController(BlackPostService blackPostService) {
        this.blackPostService = blackPostService;
    }

    @GetMapping("/black/latest")
    public List<BlackPostDto> getAllPosts(
            @RequestParam(required = false) String userId
    ) {
        List<BlackPost> posts = blackPostService.getAllPosts();
        Set<ObjectId> likedPostIds = blackPostService.getUserLikedPostIds(userId);

        return posts.stream()
                .map(post -> convertToDto(post, likedPostIds))
                .collect(Collectors.toList());
    }

    private BlackPostDto convertToDto(BlackPost post, Set<ObjectId> likedPostIds) {
        BlackPostDto dto = new BlackPostDto();
        dto.setPostId(post.getPostId().toHexString());
        dto.setNickName(post.getNickName());
        dto.setContent(post.getContent());
        dto.setMethodNumber(post.getMethodNumber());
        dto.setLikeCount(post.getLikeCount());
        dto.setLikedByUser(likedPostIds.contains(post.getPostId()));
        return dto;
    }

}
