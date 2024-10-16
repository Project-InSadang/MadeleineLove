package sideproject.madeleinelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhitePostDTO {
    private String nickName;
    private String content;
    private Integer fillMethod;
    private Integer likeCount;
    private boolean likedByUser;
}
