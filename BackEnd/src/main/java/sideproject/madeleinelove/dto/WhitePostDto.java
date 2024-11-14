package sideproject.madeleinelove.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class WhitePostDto {

    private ObjectId postId;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private boolean likedByUser;
    private Integer likesCount;
    // private Double hotScore;

}
