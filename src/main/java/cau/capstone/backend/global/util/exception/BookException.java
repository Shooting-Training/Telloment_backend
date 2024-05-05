package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class BookException extends BaseException{

    public BookException (ResponseCode responseCode) {
        super(responseCode);
    }
}
