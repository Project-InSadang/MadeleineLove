package sideproject.madeleinelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
public class PostIdDTO {
    private ObjectId postId;
}
