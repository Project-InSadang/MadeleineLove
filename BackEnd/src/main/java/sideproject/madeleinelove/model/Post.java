package sideproject.madeleinelove.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public interface Post {
    ObjectId getPostId();
    ObjectId getUserId();
    String getNickName();
    String getContent();
    Integer getMethodNumber();
    Integer getLikeCount();
    LocalDateTime getCreatedAt();

    void setLikeCount(Integer likeCount);
}
