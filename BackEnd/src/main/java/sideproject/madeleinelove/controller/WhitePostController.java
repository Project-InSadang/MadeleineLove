package sideproject.madeleinelove.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sideproject.madeleinelove.dto.WhitePostDto;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.service.WhitePostService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/white/latest")
public class WhitePostController {

    private final WhitePostService whitePostService;

    public WhitePostController(WhitePostService whitePostService) {
        this.whitePostService = whitePostService;
    }

    @GetMapping
    public List<WhitePostDto> getAllPosts() {
        List<WhitePost> posts = whitePostService.getAllPosts();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private WhitePostDto convertToDto(WhitePost post) {
        WhitePostDto dto = new WhitePostDto();
        dto.setNickName(post.getNickName());
        dto.setContent(post.getContent());
        dto.setFillMethod(post.getFillMethod());
        dto.setLikesCount(post.getLikesCount());
        return dto;
    }

}
