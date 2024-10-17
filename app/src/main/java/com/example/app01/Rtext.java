package com.example.app01;

public class Rtext {
    private String date;
    private String time;
    private String title;
    private String description;

    // Constructor
    public Rtext(String date, String time, String title, String description) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
