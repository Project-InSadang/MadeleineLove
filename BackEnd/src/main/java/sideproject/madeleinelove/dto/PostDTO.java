package sideproject.madeleinelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String postId;
    private boolean likedByUser;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private Integer likeCount;
    private LocalDateTime createdAt;

}
