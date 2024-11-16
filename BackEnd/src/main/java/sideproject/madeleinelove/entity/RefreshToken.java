package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private ObjectId userId;

    @Field("token")
    private String refreshToken;

}