package sideproject.madeleinelove.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sideproject.madeleinelove.dto.ReasonDTO;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "201", "post 생성에 성공했습니다."),
    _LIKE(HttpStatus.CREATED, "201", "post에 좋아요가 눌렸습니다."),
    _UNLIKE(HttpStatus.OK, "200", "post에 좋아요가 취소되었습니다.");

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
