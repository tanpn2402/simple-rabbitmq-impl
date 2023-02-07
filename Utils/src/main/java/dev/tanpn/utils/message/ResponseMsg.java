package dev.tanpn.utils.message;

import java.util.Map;

public class ResponseMsg<T> extends RequestMsg<T> {
    protected boolean success;
    protected String message;

    public ResponseMsg() {
    }

    public ResponseMsg(boolean pSuccess, String pMessage, String pType, Map<String, T> pData) {
        super(pType, pData);
        this.success = pSuccess;
        this.message = pMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
