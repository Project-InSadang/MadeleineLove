package sideproject.madeleinelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhitePostDTO {
    private String nickName;
    private String content;
    private Integer methodNumber;
    private Integer likeCount;
    private boolean likedByUser;
    private String postId;
}
