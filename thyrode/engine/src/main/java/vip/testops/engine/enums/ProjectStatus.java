package vip.testops.engine.enums;

public enum ProjectStatus {
    READY(0, "Ready"),
    PASS(1, "Pass"),
    FAIL(2, "Fail"),
    RUNNING(3, "Running");

    ProjectStatus(Integer key, String value) {
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

    public static ProjectStatus getByKey(Integer key) {
        for (ProjectStatus item : ProjectStatus.values()) {
            if (item.getKey().equals(key)) {
                return item;
            }
        }
        return null;
    }
}
