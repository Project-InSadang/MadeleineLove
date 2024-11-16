package sideproject.madeleinelove.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideproject.madeleinelove.dto.ReasonDTO;

@Getter
@AllArgsConstructor
public enum TokenSuccessStatus implements BaseCode {

    CREATED_ACCESS_TOKEN(HttpStatus.CREATED, "201", "액세스 토큰 재발행에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
