package sideproject.madeleinelove.dto;

import lombok.Data;

@Data
public class WhitePostDto {

    private String postId;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private boolean likedByUser;
    private Integer likesCount;

}
