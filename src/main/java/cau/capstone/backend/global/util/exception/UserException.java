package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class UserException extends BaseException {
    public UserException(ResponseCode responseCode) {
        super(responseCode);
    }
}