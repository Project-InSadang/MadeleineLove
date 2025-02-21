package sideproject.madeleinelove.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;

@Data
public class BlackRequestDto {
    private static final String DEFAULT_NICKNAME = "레니";

    @Size(max = 12, message = "닉네임은 12자 이하이어야 합니다.")
    private String nickName;

    @NotBlank(message = "내용 작성은 필수입니다.")
    @Size(max = 500, message = "내용은 500자 이하이어야 합니다.")
    private String content;

    @NotNull(message = "채우기 방법은 필수입니다.")
    private Integer methodNumber;

    public String getNickName() {
        return Optional.ofNullable(nickName).filter(n -> !n.trim().isEmpty()).orElse(DEFAULT_NICKNAME);
    }
}
