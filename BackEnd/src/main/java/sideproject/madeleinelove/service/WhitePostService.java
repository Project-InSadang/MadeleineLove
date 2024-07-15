package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
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

    public WhitePost saveWhitePost(String userId, @Valid WhiteRequestDto whiteRequestDto) {
        validateWhiteRequestDto(whiteRequestDto);

        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);

        return whitePostRepository.save(whitePost);
    }

    private void validateWhiteRequestDto(WhiteRequestDto whiteRequestDto) {
        if (whiteRequestDto.getNickName() == null) {
            whiteRequestDto.setNickName("레니");
        } else if (whiteRequestDto.getNickName().length() > 20) {
            throw new IllegalArgumentException("닉네임은 20자 이하이어야 합니다.");
        }

        if (whiteRequestDto.getFillMethod() == null) {
            throw new IllegalArgumentException("내용 작성은 필수입니다.");
        } else if (whiteRequestDto.getContent().length() > 500) {
            throw new IllegalArgumentException("내용은 500자 이하이어야 합니다.");
        }

        if (whiteRequestDto.getFillMethod() == null) {
            throw new IllegalArgumentException("채우기 방법은 필수입니다.");
        }
    }

    private WhitePost createWhitePost(String userId, WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(ID_GENERATOR.getAndIncrement());
        whitePost.setNickName(whiteRequestDto.getNickName());
        whitePost.setContent(whiteRequestDto.getContent());
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);
        return whitePost;
    }
}