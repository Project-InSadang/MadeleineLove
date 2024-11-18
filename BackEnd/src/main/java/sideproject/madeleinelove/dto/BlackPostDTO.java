package sideproject.madeleinelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackPostDTO {
    private String nickName;
    private String content;
    private Integer methodNumber;
    private Integer likeCount;
    private boolean likedByUser;
    private String postId;
}
