package sideproject.madeleinelove.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostException extends RuntimeException {
    private final PostErrorResult postErrorResult;

    @Override
    public String getMessage() {
        return postErrorResult.getMessage();
    }
}