package sideproject.madeleinelove.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlackPostDto {

    private String postId;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private boolean likedByUser;
    private Integer likeCount;
    // private Double hotScore;

}
