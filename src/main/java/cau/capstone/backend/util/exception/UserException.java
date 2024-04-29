package cau.capstone.backend.util.exception;

import cau.capstone.backend.util.api.ResponseCode;

public class UserException extends BaseException {
    public UserException(ResponseCode responseCode) {
        super(responseCode);
    }
}