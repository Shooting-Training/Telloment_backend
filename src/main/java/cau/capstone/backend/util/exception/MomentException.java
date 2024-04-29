package cau.capstone.backend.util.exception;

import cau.capstone.backend.util.api.ResponseCode;

public class MomentException extends BaseException{
    public MomentException(ResponseCode responseCode) {
        super(responseCode);
    }
}
