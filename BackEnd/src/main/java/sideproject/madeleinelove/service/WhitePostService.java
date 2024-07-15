package sideproject.madeleinelove.service;

import jakarta.validation.ConstraintViolationException;
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
        validateWhiteRequestDto(whiteRequestDto); // 유효성 검사 수행

        WhitePost whitePost = createWhitePost(userId, whiteRequestDto);
        return whitePostRepository.save(whitePost);
    }

    private void validateWhiteRequestDto(WhiteRequestDto whiteRequestDto) {
        // 닉네임 처리
        if (whiteRequestDto.getNickName() == null || whiteRequestDto.getNickName().trim().isEmpty()) {
            whiteRequestDto.setNickName(DEFAULT_NICKNAME);
        } else if (whiteRequestDto.getNickName().length() > 20) {
            throw new ConstraintViolationException("닉네임은 20자 이하이어야 합니다.", null);
        }

        // 내용 처리
        if (whiteRequestDto.getContent() == null || whiteRequestDto.getContent().trim().isEmpty()) {
            throw new ConstraintViolationException("내용 작성은 필수입니다.", null);
        } else if (whiteRequestDto.getContent().length() > 500) {
            throw new ConstraintViolationException("내용은 500자 이하이어야 합니다.", null);
        }

        // 채우기 방법 처리
        if (whiteRequestDto.getFillMethod() == null) {
            throw new ConstraintViolationException("채우기 방법은 필수입니다.", null);
        }
    }

    private WhitePost createWhitePost(String userId, WhiteRequestDto whiteRequestDto) {
        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(new ObjectId());
        whitePost.setNickName(whiteRequestDto.getNickName());
        whitePost.setContent(whiteRequestDto.getContent());
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);
        return whitePost;
    }
}