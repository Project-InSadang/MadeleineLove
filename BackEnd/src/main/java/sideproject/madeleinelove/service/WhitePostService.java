package sideproject.madeleinelove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WhitePostService {
    @Autowired
    private WhitePostRepository whitePostRepository;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    public WhitePost saveWhitePost(String userId, WhiteRequestDto whiteRequestDto) {

        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(ID_GENERATOR.getAndIncrement());
        whitePost.setNickName(Optional.ofNullable(whiteRequestDto.getNickName()).orElse("레니"));
        whitePost.setContent(whiteRequestDto.getContent());
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);

        return whitePostRepository.save(whitePost);
    }
}