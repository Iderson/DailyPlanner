package com.plugnplay.dailyplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Andread on 08.06.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static Ringtone ringtone;
    public static final String ALARM_ACTION_NAME = "com.plugnplay.dailyplanner.ALARM";

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Handle the alarm broadcast
        if (ALARM_ACTION_NAME.equals(intent.getAction()))
        {
            // Launch the alarm popup dialog
            Intent alarmIntent = new Intent("android.intent.action.MAIN");

            alarmIntent.setClass(context, AlarmPopUp.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Pass on the alarm ID as extra data
            int alarmID = intent.getIntExtra("AlarmID", -1);
            String alarmName = intent.getStringExtra("AlarmTask");
            String alarmComment = intent.getStringExtra("AlarmComment");

            alarmIntent.putExtra("AlarmID", alarmID);
            alarmIntent.putExtra("AlarmTask", alarmName);
            alarmIntent.putExtra("AlarmComment", alarmComment);

            // Start the popup activity
            context.startActivity(alarmIntent);
        }
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
    }

    public static void stopAlarm(){
        if(ringtone != null)   ringtone.stop();
    }
}
