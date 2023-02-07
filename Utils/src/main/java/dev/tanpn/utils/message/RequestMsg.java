package dev.tanpn.utils.message;

import java.util.Map;

public class RequestMsg<T> {
    protected String type;
    protected Map<String, T> body;

    public RequestMsg() {}

    public RequestMsg(String pType, Map<String, T> pBody) {
        this.type = pType;
        this.body = pBody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, T> getBody() {
        return body;
    }

    public void setBody(Map<String, T> body) {
        this.body = body;
    }
}
