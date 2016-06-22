package com.plugnplay.dailyplanner;

import android.app.AlarmManager;

/**
 * Created by Andread on 17.06.2016.
 */
public class DPConst {
    public static final int MIN_MILLIS = 60 * 1000;
    public static final int HRS_MILLIS = 60 * MIN_MILLIS;
    public static final int DAY_MILLIS = 24 * HRS_MILLIS;
    public static final long WEEK = DAY_MILLIS * 7;
    public static int ALARM_MODE = AlarmManager.RTC_WAKEUP;
}
