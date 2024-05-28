package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class ScrapException extends BaseException{

    public ScrapException(ResponseCode responseCode) {
        super(responseCode);
    }
}
