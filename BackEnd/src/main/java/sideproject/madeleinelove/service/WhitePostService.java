package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
import org.bson.types.ObjectId;
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

    public WhitePost saveWhitePost(String userId, @Valid WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);
        return whitePostRepository.save(whitePost);
    }

    private WhitePost createWhitePost(String userId, WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(new ObjectId());
        whitePost.setNickName(whiteRequestDto.getNickName() != null ? whiteRequestDto.getNickName() : "레니");
        whitePost.setContent(whiteRequestDto.getContent());
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);
        return whitePost;
    }
}