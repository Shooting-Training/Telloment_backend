package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class EmotionException extends BaseException{

        public EmotionException (ResponseCode responseCode) {
            super(responseCode);
        }
}
