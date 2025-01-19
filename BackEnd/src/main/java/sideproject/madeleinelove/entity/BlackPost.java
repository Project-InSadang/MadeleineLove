package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sideproject.madeleinelove.model.Post;

@Data
@Builder
@Document(collection = "BlackPosts")
public class BlackPost implements Post {

    @Id
    private ObjectId postId;
    private ObjectId userId;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private Integer likeCount;

}