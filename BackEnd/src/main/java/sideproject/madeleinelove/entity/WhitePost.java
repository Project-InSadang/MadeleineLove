package sideproject.madeleinelove.entity;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import sideproject.madeleinelove.model.Post;

@Data
@Builder
@Document(collection = "WhitePosts")
public class WhitePost implements Post {
    @Id
    private ObjectId postId;
    private String userId;
    private String nickName;
    private String content;
    private Integer fillMethod;
    private Integer likeCount;

    @Override
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}