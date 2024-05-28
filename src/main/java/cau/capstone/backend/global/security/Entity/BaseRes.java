package cau.capstone.backend.global.security.Entity;

import cau.capstone.backend.global.util.api.ResponseCode;
import org.springframework.http.HttpStatus;

public class BaseRes {
    private HttpStatus status;
    private  boolean success;
    private String message;

    // Private constructor to force using the factory methods
    private BaseRes(HttpStatus status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    // Factory method for success
    public static BaseRes success(boolean success) {
        return new BaseRes(HttpStatus.OK, success, "SUCCESS");
    }

    // Factory method for failure
    public static BaseRes failure(ResponseCode resCode) {
        return new BaseRes(resCode.getHttpStatus(), resCode.getSuccess(), resCode.getMessage());
    }

    // Getters
    public HttpStatus getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Setters - if mutable fields are necessary
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
