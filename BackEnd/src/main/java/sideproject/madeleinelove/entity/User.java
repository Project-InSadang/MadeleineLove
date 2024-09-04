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
    private ObjectId userId;
    private String email;
    private UserRole role;
    private String provider;
    private String providerId;

}
