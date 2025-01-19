package sideproject.madeleinelove.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import sideproject.madeleinelove.base.BaseErrorCode;
import sideproject.madeleinelove.dto.ErrorReasonDTO;

@Getter
@RequiredArgsConstructor
public enum PostErrorResult implements BaseErrorCode {

    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "404", "존재하지 않는 포스트입니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "400", "이미 좋아요를 눌렀습니다."),
    ALREADY_UNLIKED(HttpStatus.BAD_REQUEST, "400", "취소할 좋아요가 없습니다.");

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
