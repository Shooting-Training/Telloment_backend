package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class MomentException extends BaseException{
    public MomentException(ResponseCode responseCode) {
        super(responseCode);
    }
}
