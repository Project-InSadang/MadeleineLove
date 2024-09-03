package sideproject.madeleinelove.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "WhitePost")
public class WhitePost {

    @Id
    private ObjectId postId;
    private String userId;
    private String nickName;
    private String content;
    private Integer fillMethod;
    private Integer likesCount;

}
