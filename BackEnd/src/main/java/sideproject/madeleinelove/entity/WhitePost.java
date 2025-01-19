package sideproject.madeleinelove.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;
import sideproject.madeleinelove.model.Post;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "WhitePosts")
public class WhitePost implements Post {

    @Id
    private ObjectId postId;
    private ObjectId userId;
    private String nickName;
    private String content;
    private Integer methodNumber;
    private Integer likeCount;
    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

}