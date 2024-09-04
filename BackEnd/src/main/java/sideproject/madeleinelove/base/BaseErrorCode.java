package sideproject.madeleinelove.base;

import sideproject.madeleinelove.dto.ErrorReasonDto;

public interface BaseErrorCode {

    public ErrorReasonDto getReason();
    public ErrorReasonDto getReasonHttpStatus();

}
