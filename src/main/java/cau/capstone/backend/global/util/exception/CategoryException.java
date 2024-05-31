package cau.capstone.backend.global.util.exception;

import cau.capstone.backend.global.util.api.ResponseCode;

public class CategoryException extends BaseException{

        public CategoryException (ResponseCode responseCode) {
            super(responseCode);
        }
}
