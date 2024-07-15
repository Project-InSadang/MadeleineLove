package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WhitePostService {
    @Autowired
    private WhitePostRepository whitePostRepository;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    public WhitePost saveWhitePost(String userId, @Valid WhiteRequestDto whiteRequestDto) {

        WhitePost whitePost = new WhitePost();
        whitePost.setUserId(userId);
        whitePost.setPostId(ID_GENERATOR.getAndIncrement());
        if (whiteRequestDto.getNickName() == null) {
            whiteRequestDto.setNickName("레니");
        }
        if (whiteRequestDto.getNickName() != null && whiteRequestDto.getNickName().length() > 20) {
            throw new IllegalArgumentException("닉네임은 20자 이하이어야 합니다.");
        }
        whitePost.setNickName(whiteRequestDto.getNickName());
        if (whiteRequestDto.getFillMethod() == null){
            throw new IllegalArgumentException("내용 작성은 필수입니다.");
        }
        else if (whiteRequestDto.getContent().length() > 500) {
            throw new IllegalArgumentException("내용은 500자 이하이어야 합니다.");
        }
        whitePost.setContent(whiteRequestDto.getContent());
        if (whiteRequestDto.getFillMethod() == null) {
            throw new IllegalArgumentException("채우기 방법은 필수입니다.");
        }
        whitePost.setFillMethod(whiteRequestDto.getFillMethod());
        whitePost.setLikeCount(0);

        return whitePostRepository.save(whitePost);
    }
}
