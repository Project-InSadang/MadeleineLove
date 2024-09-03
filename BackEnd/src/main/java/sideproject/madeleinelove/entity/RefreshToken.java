package sideproject.madeleinelove.entity;


import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Field("refresh_tokens_id")
    private Long id;

    @Field("users_uuid")
    private ObjectId userId;

    @Field("token")
    private String token;
}