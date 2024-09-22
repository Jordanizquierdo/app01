package com.example.app01;

public class ProfileItem {
    private String label;
    private String value;

    public ProfileItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
