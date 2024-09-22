package com.example.app01;

public class Horario {
    private String startTime;
    private String endTime;

    public Horario(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getFormattedTimeSlot() {
        return startTime + " - " + endTime;
    }
}
