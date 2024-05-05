package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class PageException extends BaseException{
    public PageException(ResponseCode responseCode) {
        super(responseCode);
    }
}
