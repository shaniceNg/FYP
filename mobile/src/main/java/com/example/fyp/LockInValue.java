package com.example.fyp;

public class LockInValue {
    String activity, duration, cTime;

    public LockInValue() {
    }

    public LockInValue(String activity, String duration, String cTime) {
        this.activity = activity;
        this.duration = duration;
        this.cTime = cTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
}