package sideproject.madeleinelove.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "WhitePost")
public class WhitePost {
    @Id
    private Integer postId;
    private String userId;
    private String nickName;
    private String content;
    private Integer fillMethod;
    private Integer likeCount;
}