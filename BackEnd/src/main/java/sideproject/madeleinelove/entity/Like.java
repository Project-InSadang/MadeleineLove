package sideproject.madeleinelove.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "WhiteLikes")
public class Like {

    @Id
    private ObjectId id;

    private String userId;
    private ObjectId postId;

}
