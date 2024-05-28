package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class LikeException extends BaseException{

    public LikeException(ResponseCode responseCode) {
        super(responseCode);
    }
}
