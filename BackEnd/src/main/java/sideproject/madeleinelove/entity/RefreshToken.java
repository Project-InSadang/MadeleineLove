package sideproject.madeleinelove.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class RefreshToken {

    @Id
    private ObjectId userId;

    private String refreshToken;

}