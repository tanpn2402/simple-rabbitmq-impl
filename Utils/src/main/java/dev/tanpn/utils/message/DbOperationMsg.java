package dev.tanpn.utils.message;

import java.util.List;
import java.util.Map;

import dev.tanpn.utils.enums.DbOperationType;

public class DbOperationMsg {
    protected DbOperationType operation;
    protected String[] headers;
    protected List<Map<String, String>> data;

    public DbOperationMsg() {
    }

    public DbOperationMsg(DbOperationType pOperation, String[] pHeaders) {
        this.operation = pOperation;
        this.headers = pHeaders;
    }

    public DbOperationMsg(DbOperationType pOperation, String[] pHeaders, List<Map<String, String>> pData) {
        this.operation = pOperation;
        this.headers = pHeaders;
        this.data = pData;
    }

    public DbOperationType getOperation() {
        return operation;
    }

    public void setOperation(DbOperationType operation) {
        this.operation = operation;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

}
