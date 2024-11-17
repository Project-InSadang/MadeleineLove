package sideproject.madeleinelove.model;

import org.bson.types.ObjectId;

public interface Post {
    ObjectId getPostId();
    String getUserId();
    String getNickName();
    String getContent();
    Integer getFillMethod();
    Integer getLikeCount();

    void setLikeCount(Integer likeCount);
}
