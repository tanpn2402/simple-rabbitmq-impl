package dev.tanpn.utils.message;

import java.io.Serializable;
import java.util.List;

public class MqMessage implements Serializable {
	protected String type;
	protected String[] headers;
	protected List<String[]> body;

	public MqMessage() {
	}

	public MqMessage(String pType, String[] pHeaders, List<String[]> pBody) {
		this.type = pType;
		this.headers = pHeaders;
		this.body = pBody;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public List<String[]> getBody() {
		return body;
	}

	public void setBody(List<String[]> body) {
		this.body = body;
	}
}
