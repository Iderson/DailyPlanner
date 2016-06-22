package com.plugnplay.dailyplanner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andread on 08.06.2016.
 */
public class TaskItemModel implements Serializable {
    private int id = 0;
    private String taskName;
    private String taskComment;
    private int hour;
    private int minute;
    private boolean sunday = false;
    private boolean monday = false;
    private boolean tuesday = false;
    private boolean wednesday = false;
    private boolean thursday = false;
    private boolean friday = false;
    private boolean saturday = false;
    private boolean mChecked;

    public TaskItemModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String _taskName) {
        taskName = _taskName;
    }

    public String getTaskComment() {
        return taskComment;
    }

    public void setTaskComment(String _taskComment) {
        taskComment = _taskComment;
    }

    public boolean getDayOfWeek(int day) {
        boolean isDay;
        switch (day){
            case 1:
                isDay = isSunday();
                break;
            case 2:
                isDay = isMonday();
                break;
            case 3:
                isDay = isTuesday();
                break;
            case 4:
                isDay = isWednesday();
                break;
            case 5:
                isDay = isThursday();
                break;
            case 6:
                isDay = isFriday();
                break;
            case 7:
                isDay = isSaturday();
                break;
            default:
                isDay =  true;
        }
        return isDay;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int _hour) {
        hour = _hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int _minute) {
        minute = _minute;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean _sunday) {
        sunday = _sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean _monday) {
        monday = _monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean _tuesday) {
        tuesday = _tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean _wednesday) {
        wednesday = _wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean _thursday) {
        thursday = _thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean _friday) {
        friday = _friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean _saturday) {
        saturday = _saturday;
    }

    public ArrayList<Integer> getDaysOfWeek(){
        ArrayList <Integer> days = new ArrayList<>();
        if (isSunday())     days.add(1);
        if (isMonday())     days.add(2);
        if (isTuesday())    days.add(3);
        if (isWednesday())  days.add(4);
        if (isThursday())   days.add(5);
        if (isFriday())     days.add(6);
        if (isSaturday())   days.add(7);
        return days;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean _checked) {
        mChecked = _checked;
    }
}
