package sideproject.madeleinelove.entity;

import lombok.Builder;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "Likes")
public class Likes {
    @Id
    private ObjectId id;
    private String userId;
    private ObjectId postId;
}
