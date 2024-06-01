package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class VoiceServerException extends BaseException{

        public VoiceServerException(ResponseCode responseCode) {
            super(responseCode);
        }
}
