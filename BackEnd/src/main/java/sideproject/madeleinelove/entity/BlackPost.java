package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import sideproject.madeleinelove.model.Post;

import java.time.LocalDateTime;

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
    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

}