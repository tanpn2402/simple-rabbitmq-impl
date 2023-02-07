package dev.tanpn.utils.enums;

public enum DbOperationType {
    Q("Query"),
    I("Insert"),
    D("Delete"),
    U("Update");

    private String value;

    private DbOperationType(String pValue) {
        this.value = pValue;
    }

    public String getValue() {
        return value;
    }
}
