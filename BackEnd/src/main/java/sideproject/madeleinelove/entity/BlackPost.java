package sideproject.madeleinelove.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "BlackPost")
public class BlackPost {

    @Id
    private ObjectId postId;
    private String userId;
    private String nickName;
    private String content;
    private Integer methodCount;
    private Integer likeCount;

}
