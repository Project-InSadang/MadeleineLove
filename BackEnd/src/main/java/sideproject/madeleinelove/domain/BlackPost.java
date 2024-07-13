package sideproject.madeleinelove.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BlackPost")
public class BlackPost {

    @Id
    private Integer postId;
    private Integer userId;
    private String nickName;
    private String content;
    private Integer cleanMethod;
    private Integer likeCount;

    // Constructors
    public BlackPost(Integer postId, Integer userId, String nickName, String content, Integer cleanMethod, Integer likeCount) {
        this.postId = postId;
        this.userId = userId;
        this.nickName = nickName;
        this.content = content;
        this.cleanMethod = cleanMethod;
        this.likeCount = likeCount;
    }

    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCleanMethod() {
        return cleanMethod;
    }

    public void setCleanMethod(Integer cleanMethod) {
        this.cleanMethod = cleanMethod;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

}
