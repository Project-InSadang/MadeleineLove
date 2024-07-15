package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;

@Service
public class WhitePostService {

    @Autowired
    private WhitePostRepository whitePostRepository;
    private static final String DEFAULT_NICKNAME = "레니";

    public WhitePost saveWhitePost(String userId, @Valid WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);
        return whitePostRepository.save(whitePost);
    }

    private WhitePost createWhitePost(String userId, WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(new ObjectId());
        whitePost.setNickName(getValidNickName(whiteRequestDto.getNickName()));
        whitePost.setContent(whiteRequestDto.getContent());
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);
        return whitePost;
    }

    private String getValidNickName(String nickName) {
        if (nickName == null || nickName.trim().isEmpty()) {
            return DEFAULT_NICKNAME;
        }
        return nickName.trim();
    }
}