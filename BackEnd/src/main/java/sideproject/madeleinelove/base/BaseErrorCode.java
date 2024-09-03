package sideproject.madeleinelove.base;

import sideproject.madeleinelove.dto.ErrorReasonDTO;

public interface BaseErrorCode {
    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}