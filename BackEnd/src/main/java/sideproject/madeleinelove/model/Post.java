package sideproject.madeleinelove.model;

import org.bson.types.ObjectId;

public interface Post {
    ObjectId getPostId();
    ObjectId getUserId();
    String getNickName();
    String getContent();
    Integer getMethodNumber();
    Integer getLikeCount();

    void setLikeCount(Integer likeCount);
}
