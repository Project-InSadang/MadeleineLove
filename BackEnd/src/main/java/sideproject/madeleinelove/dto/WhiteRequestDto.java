package sideproject.madeleinelove.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WhiteRequestDto {
    @Size(max = 20)
    private String nickName;
    @NotNull
    @Size(max = 500)
    private String content;
    @NotNull
    private Integer fillMethod;
}
