package sideproject.madeleinelove.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.repository.BlackPostRepository;
import sideproject.madeleinelove.entity.BlackPost;

@Service
public class BlackPostService {

    @Autowired
    private BlackPostRepository blackPostRepository;

    public BlackPost save(String userId, BlackPostDto blackPostDto) {
        // DTO to Entity
        BlackPost blackPost = new BlackPost();
        blackPost.setPostId(new ObjectId());
        blackPost.setUserId(userId);

        String nickName = blackPostDto.getNickName();
        if (nickName == null || nickName.trim().isEmpty()) {
            nickName = "레니";
        }
        blackPost.setNickName(nickName);

        blackPost.setContent(blackPostDto.getContent());
        blackPost.setCleanMethod(blackPostDto.getCleanMethod());
        blackPost.setLikeCount(0);

        return blackPostRepository.save(blackPost);
    }

}
