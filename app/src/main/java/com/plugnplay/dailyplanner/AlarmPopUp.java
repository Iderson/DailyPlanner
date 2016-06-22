package com.plugnplay.dailyplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Andread on 10.06.2016.
 */
public class AlarmPopUp extends Activity {
    private int mAlarmId;
    private AlarmPopUp mContext;
    private String mTaskName;
    private String mCommentName;
    private EditText mEtSnoozeHours;
    private EditText mEtSnoozeMins;
    private List<TaskItemModel> mItems = new ArrayList<>();
    private int dayNow;
    private int hourNow;
    private int minuteNow;

    public interface TaskDialogListener {
        void onFinishEditDialog(TaskItemModel _taskItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_popup);

        initViews();
    }
    private void initViews() {
        mContext = this;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mTaskName = "";
        mCommentName = "";

        Calendar calendar = Calendar.getInstance();
        dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        minuteNow = calendar.get(Calendar.MINUTE);
        if (extras != null) {
            mAlarmId = extras.getInt("AlarmID", -1);
            mTaskName = extras.getString("AlarmTask", "");
            mCommentName = extras.getString("AlarmComment", "");
        }
        if (extras.getSerializable("shiftItems") != null) {
            try {
                ArrayList<TaskItemModel> serializable = (ArrayList<TaskItemModel>) extras.getSerializable("shiftItems");

                for (int i = 0; i < serializable.size(); i++) {
                    if (!mItems.contains(serializable.get(i))) {
                        mItems.add(serializable.get(i));
                        mTaskName = mTaskName + serializable.get(i).getTaskName() + (i < serializable.size() - 1 ? ", " : "");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else                mAlarmId = -1;

        Log.v("ALARM", "mTaskName " + mTaskName);

        Button dismiss      = (Button) findViewById(R.id.btnDismiss_AP);
        final Button snoozeHours  = (Button) findViewById(R.id.btnSnoozeHours_AP);
        final Button snoozeDays   = (Button) findViewById(R.id.btnSnoozeMins_AP);
        mEtSnoozeHours = (EditText) findViewById(R.id.etHours_AP);
        mEtSnoozeMins = (EditText) findViewById(R.id.etMins_AP);
        ((TextView) findViewById(R.id.tvTaskName_AP)).setText(mTaskName);
        ((TextView) findViewById(R.id.tvCommentName_AP)).setText(mCommentName);

        (findViewById(R.id.snooze_hours_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEtSnoozeHours.getText().toString();
                Integer inc = Integer.valueOf(string);
                mEtSnoozeHours.setText(String.valueOf(inc +1));
            }
        });
        (findViewById(R.id.snooze_hours_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEtSnoozeHours.getText().toString();
                Integer dec = Integer.valueOf(string);
                dec = dec - 1;
                mEtSnoozeHours.setText(String.valueOf(dec));
            }
        });
        (findViewById(R.id.snooze_mins_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEtSnoozeMins.getText().toString();
                Integer inc = Integer.valueOf(string);
                mEtSnoozeMins.setText(String.valueOf(inc + 1));
            }
        });
        (findViewById(R.id.snooze_mins_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEtSnoozeMins.getText().toString();
                Integer dec = Integer.valueOf(string);
                dec = dec - 1;
                mEtSnoozeMins.setText(String.valueOf(dec));
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.stopAlarm();
                finish();
            }
        });
        snoozeHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeHoursAlarm();
                AlarmReceiver.stopAlarm();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        snoozeDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeMinsAlarm();
                AlarmReceiver.stopAlarm();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
    private void snoozeHoursAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        Integer hours = Integer.valueOf(mEtSnoozeHours.getText().toString());
        long snoozeInMillis = 0;
//        long snoozeInMillis = calendar.getTimeInMillis() + (hours * 60 * 1000);
        PendingIntent pendingIntent = null;
        Intent alarmIntent = new Intent("com.plugnplay.dailyplanner.ALARM");
        if(mItems == null) {
            alarmIntent.putExtra("AlarmID", mAlarmId);
            alarmIntent.putExtra("AlarmTask", mTaskName);
            alarmIntent.putExtra("AlarmComment", mCommentName);
            pendingIntent = PendingIntent.getBroadcast(AlarmPopUp.this, 0, alarmIntent, 0);
            snoozeInMillis = calendar.getTimeInMillis() + (hours * DPConst.HRS_MILLIS);
        } else {
            for (int i = 0; i < mItems.size(); i++){
                alarmIntent.putExtra("AlarmID", mItems.get(i).getId());
                alarmIntent.putExtra("AlarmTask", mItems.get(i).getTaskName());
                alarmIntent.putExtra("AlarmComment", mItems.get(i).getTaskComment());

                if (!mItems.get(i).getDayOfWeek(dayNow)){
                    Toast.makeText(this, "You can shift only this day", Toast.LENGTH_SHORT).show();
                    return;
                }

                snoozeInMillis = calendar.getTimeInMillis()
                        + DPConst.HRS_MILLIS * (hourNow + hours)
                        + DPConst.MIN_MILLIS * (minuteNow - calendar.get(Calendar.MINUTE));

                mItems.get(i).setHour(mItems.get(i).getHour() + hours);
                int id = mItems.get(i).getId() + Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                TodoListSQLHelper listSQLHelper = new TodoListSQLHelper(AlarmPopUp.this);
                SQLiteDatabase sqLiteDatabase = listSQLHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.clear();
                values.put(TodoListSQLHelper.COL3_TASK, mItems.get(i).getHour());
                sqLiteDatabase.update(TodoListSQLHelper.TABLE_LIST, values,
                        "_id="+ mItems.get(i).getId(), null);

                pendingIntent = PendingIntent.getBroadcast(AlarmPopUp.this,
                        id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);//// TODO: WHAT A FLAG?
                Log.v("ALARM_SNOOZE", "ID: " + id);
            }
        }
        alarmManager.set(DPConst.ALARM_MODE, snoozeInMillis, pendingIntent);
    }
    private void snoozeMinsAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        Integer mins = Integer.valueOf(mEtSnoozeMins.getText().toString());
        long snoozeInMillis = 0;
        PendingIntent pendingIntent = null;
        Intent alarmIntent = new Intent("com.plugnplay.dailyplanner.ALARM");
        if(mItems == null) {
            alarmIntent.putExtra("AlarmID", mAlarmId);
            alarmIntent.putExtra("AlarmTask", mTaskName);
            alarmIntent.putExtra("AlarmComment", mCommentName);
            snoozeInMillis = calendar.getTimeInMillis() + (mins * DPConst.MIN_MILLIS);
            pendingIntent = PendingIntent.getBroadcast(AlarmPopUp.this, 0, alarmIntent, 0);
        } else {
            for (int i = 0; i < mItems.size(); i++) {
                alarmIntent.putExtra("AlarmID", mItems.get(i).getId());
                alarmIntent.putExtra("AlarmTask", mItems.get(i).getTaskName());
                alarmIntent.putExtra("AlarmComment", mItems.get(i).getTaskComment());

                if (!mItems.get(i).getDayOfWeek(dayNow)){
                    Toast.makeText(this, "You can shift only this day", Toast.LENGTH_SHORT).show();
                    return;
                }
                snoozeInMillis = calendar.getTimeInMillis()
                        + DPConst.HRS_MILLIS * (hourNow - calendar.get(Calendar.HOUR_OF_DAY))
                        + DPConst.MIN_MILLIS * (minuteNow + mins);

                mItems.get(i).setMinute(mItems.get(i).getMinute() + mins);
                TodoListSQLHelper listSQLHelper = new TodoListSQLHelper(AlarmPopUp.this);
                SQLiteDatabase sqLiteDatabase = listSQLHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.clear();
                values.put(TodoListSQLHelper.COL4_TASK, mItems.get(i).getMinute());
                sqLiteDatabase.update(TodoListSQLHelper.TABLE_LIST, values,
                        "_id="+ mItems.get(i).getId(), null);

                int id = mItems.get(i).getId() + Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                pendingIntent = PendingIntent.getBroadcast(AlarmPopUp.this, id,
                        alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);//// TODO: What a FLAG?
                Log.v("ALARM_SNOOZE", "ID: " + id);
            }
        }
        alarmManager.set(DPConst.ALARM_MODE, snoozeInMillis, pendingIntent);
    }

    public void onResume() {
        Window window = getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
