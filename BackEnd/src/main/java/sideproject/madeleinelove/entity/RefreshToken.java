package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "RefreshToken")
public class RefreshToken {

    @Id
    private ObjectId refreshTokenId;
    private ObjectId userId;
    private String token;

}
