package sideproject.madeleinelove.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.User;
import sideproject.madeleinelove.exception.UserErrorResult;
import sideproject.madeleinelove.exception.UserException;
import sideproject.madeleinelove.repository.BlackPostRepository;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BlackPostService {

    @Autowired
    private BlackPostRepository blackPostRepository;
    private final TokenServiceImpl tokenServiceImpl;
    private final UserRepository userRepository;
    private final UserService userService;

    /*public BlackPost save(String userId, BlackPostDto blackPostDto) {
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
        blackPost.setMethodNumber(blackPostDto.getMethodNumber());
        blackPost.setLikeCount(0);

        return blackPostRepository.save(blackPost);
    }
     */

    public BlackPost saveBlackPost(HttpServletRequest request, HttpServletResponse response,
                                   String authorizationHeader, BlackPostDto blackRequestDto) {

        ObjectId userId = userService.validateUser(request, response, authorizationHeader);
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.NOT_FOUND_USER));

        BlackPost blackPost = createBlackPost(userId, blackRequestDto);
        return blackPostRepository.save(blackPost);
    }

    private BlackPost createBlackPost(ObjectId userId, BlackPostDto blackRequestDto) {
        return BlackPost.builder()
                .postId(new ObjectId())
                .userId(userId)
                .nickName(blackRequestDto.getNickName())
                .content(blackRequestDto.getContent())
                .methodNumber(blackRequestDto.getMethodNumber())
                .likeCount(0)
                .build();
    }

}
