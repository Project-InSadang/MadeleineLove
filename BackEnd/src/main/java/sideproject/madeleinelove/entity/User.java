package sideproject.madeleinelove.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "User")
public class User {
    @Id
    private ObjectId userId;

    @Field("email")
    private String email;

    @Field("role")
    private UserRole role;

    @Field("provider")
    private String provider;

    @Field("providerId")
    private String providerId;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;

    public ObjectId getUserId() {
        return userId;
    }

    public void update() {
        this.updatedAt = LocalDateTime.now();
    }
}
