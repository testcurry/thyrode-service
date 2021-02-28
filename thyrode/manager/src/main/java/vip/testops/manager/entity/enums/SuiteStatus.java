package vip.testops.manager.entity.enums;

public enum SuiteStatus {
    Initial(0, "Initial"),
    PASS(1, "Pass"),
    FAIL(2, "Fail"),
    RUNNING(3, "Block");

    SuiteStatus(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    private Integer key;

    private String value;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static SuiteStatus getByKey(Integer key) {
        for (SuiteStatus item : SuiteStatus.values()) {
            if (item.getKey().equals(key)) {
                return item;
            }
        }
        return null;
    }
}
