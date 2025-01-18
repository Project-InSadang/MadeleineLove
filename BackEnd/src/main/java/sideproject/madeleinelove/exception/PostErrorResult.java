package sideproject.madeleinelove.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sideproject.madeleinelove.base.BaseErrorCode;
import sideproject.madeleinelove.dto.ErrorReasonDTO;

@Getter
@RequiredArgsConstructor
public enum PostErrorResult implements BaseErrorCode {

    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "404", "존재하지 않는 포스트입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
