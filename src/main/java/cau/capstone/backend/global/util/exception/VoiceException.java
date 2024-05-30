package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class VoiceException extends BaseException {
    public VoiceException(ResponseCode responseCode) {
        super(responseCode);
    }
}
