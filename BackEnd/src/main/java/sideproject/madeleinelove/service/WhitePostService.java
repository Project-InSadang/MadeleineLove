package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;

@Service
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;

    public WhitePostService(WhitePostRepository whitePostRepository) {
        this.whitePostRepository = whitePostRepository;
    }

    public WhitePost saveWhitePost(String userId, @Valid WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);
        return whitePostRepository.save(whitePost);
    }

    private WhitePost createWhitePost(String userId, WhiteRequestDto whiteRequestDto) {
        return WhitePost.builder()
                .postId(new ObjectId())
                .userId(userId)
                .nickName(whiteRequestDto.getNickName())
                .content(whiteRequestDto.getContent())
                .methodNumber(whiteRequestDto.getMethodNumber())
                .likeCount(0)
                .build();
    }
}