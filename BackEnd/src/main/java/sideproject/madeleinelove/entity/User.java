package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "User")
public class User {
    @Id
    private ObjectId userid;

    @Field("email")
    private String email;

    @Field("role")
    private UserRole role;

    @Field("provider")
    private String provider;

    @Field("providerId")
    private String providerId;
}
