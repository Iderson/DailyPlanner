package com.plugnplay.dailyplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        AddTaskDialogFragment.TaskDialogListener, View.OnClickListener {

    private RecyclerView mTaskRecycler;
    private LinearLayoutManager mLayout;
    private List<TaskItemModel> mTaskList = new ArrayList<>();
    private AlarmManager mAlarmManager;
    private TodoListSQLHelper mListSQLHelper;
    private List<TaskItemModel> mSelectedItems = new ArrayList<>();
    private Button mBtnAdd;
    private EditText mEtTaskName;
    private Button mBtnShift;
    private Button mBtnRemove;
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        updateTodoList(day);
    }

    private void initViews() {
        mEtTaskName = (EditText) findViewById(R.id.etTask);
        mBtnAdd = (Button)findViewById(R.id.btnAdd_AM);
        mBtnShift = (Button) findViewById(R.id.btnShift_AM);
        mBtnRemove = (Button) findViewById(R.id.btnRemove_AM);
        mBtnCancel = (Button) findViewById(R.id.btnCancel_AM);

        mBtnAdd.setOnClickListener(this);
        mBtnShift.setOnClickListener(this);
        mBtnRemove.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mLayout = new LinearLayoutManager(this);
        mTaskRecycler = (RecyclerView) findViewById(R.id.rcTaskList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mTaskList != null)  initRecycler();
    }

    private void initRecycler() {
        TaskListAdapter taskAdapter = new TaskListAdapter(this, mTaskList);
        mTaskRecycler.setLayoutManager(mLayout);
        mTaskRecycler.setAdapter(taskAdapter);

        mTaskRecycler.addOnItemTouchListener(
                new RecyclerTouchListener(this, mTaskRecycler, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if(!mTaskList.get(position).isChecked()) {
                            mSelectedItems.add(mTaskList.get(position));
                            mTaskList.get(position).setChecked(true);

                            mBtnAdd.setVisibility(View.GONE);
                            mEtTaskName.setVisibility(View.GONE);
                            mBtnShift.setVisibility(View.VISIBLE);
                            mBtnRemove.setVisibility(View.VISIBLE);
                            mBtnCancel.setVisibility(View.VISIBLE);
                            view.setBackgroundColor(Color.GRAY);
                        } else {
                            mTaskList.get(position).setChecked(false);
                            mSelectedItems.remove(mTaskList.get(position));
                            view.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));
    }

    private void addTaskItem(final String _taskName) {
        FragmentManager fm = getSupportFragmentManager();
        AddTaskDialogFragment editNameDialogFragment = AddTaskDialogFragment.newInstance(_taskName);
        editNameDialogFragment.show(fm, "add_task_dialog");
    }

    private void updateTodoList(int _day) {
        String dayOfWeek = "";
        switch (_day){
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;
            default:
                dayOfWeek = "All tasks for a week";
                break;
        }
        ((TextView)findViewById(R.id.tvLabelDays_AM)).setText(dayOfWeek);
        mListSQLHelper = new TodoListSQLHelper(MainActivity.this);
        try {
            SQLiteDatabase sqLiteDatabase = mListSQLHelper.getReadableDatabase();

            mTaskList = new ArrayList<>();

            Cursor cursor = sqLiteDatabase.query(TodoListSQLHelper.TABLE_DAYS,
                    new String[]{TodoListSQLHelper._ID, TodoListSQLHelper.COL1_SUN,
                            TodoListSQLHelper.COL2_MON, TodoListSQLHelper.COL3_TUE,
                            TodoListSQLHelper.COL4_WED, TodoListSQLHelper.COL5_THU,
                            TodoListSQLHelper.COL6_FRI, TodoListSQLHelper.COL7_SAT},
                    null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TaskItemModel item = new TaskItemModel();
                item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                item.setSunday(cursor.getInt(cursor.getColumnIndex("days_sun")) == 1);
                item.setMonday(cursor.getInt(cursor.getColumnIndex("days_mon")) == 1);
                item.setTuesday(cursor.getInt(cursor.getColumnIndex("days_tue")) == 1);
                item.setWednesday(cursor.getInt(cursor.getColumnIndex("days_wed")) == 1);
                item.setThursday(cursor.getInt(cursor.getColumnIndex("days_thu")) == 1);
                item.setFriday(cursor.getInt(cursor.getColumnIndex("days_fri")) == 1);
                item.setSaturday(cursor.getInt(cursor.getColumnIndex("days_sat")) == 1);
                if(item.getDayOfWeek(_day))    mTaskList.add(item);
                cursor.moveToNext();
            }
            cursor.close();

            cursor = sqLiteDatabase.query(TodoListSQLHelper.TABLE_LIST,
                    new String[]{TodoListSQLHelper._ID, TodoListSQLHelper.COL1_TASK,
                            TodoListSQLHelper.COL2_TASK,TodoListSQLHelper.COL3_TASK,
                            TodoListSQLHelper.COL4_TASK}, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                for (int i = 0; i < mTaskList.size(); i++)
                    if (mTaskList.get(i).getId() == id) {
                        mTaskList.get(i).setTaskName(cursor.getString(cursor.getColumnIndex("todo_name")));
                        mTaskList.get(i).setTaskComment(cursor.getString(cursor.getColumnIndex("todo_comment")));
                        mTaskList.get(i).setHour(cursor.getInt(cursor.getColumnIndex("todo_hour")));
                        mTaskList.get(i).setMinute(cursor.getInt(cursor.getColumnIndex("todo_minute")));
                    }
                cursor.moveToNext();
            }
            cursor.close();

            ((TextView)findViewById(R.id.tvLabelDays_AM)).append(": " +
                    (mTaskList != null ? String.valueOf(mTaskList.size()) + " task(s)" : "0 tasks"));
        } catch (RuntimeException re){
            re.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        initRecycler();
    }

    @Override
    public void onFinishEditDialog(TaskItemModel _taskItem) {
            Calendar calendar = Calendar.getInstance();
            int id = (int) calendar.getTimeInMillis();
            _taskItem.setId(id);
            Log.v("ALARM", "Calendar Id" + String.valueOf(id));

            mListSQLHelper = new TodoListSQLHelper(MainActivity.this);
            SQLiteDatabase sqLiteDatabase = mListSQLHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.clear();

            values.put(TodoListSQLHelper._ID, id);
            values.put(TodoListSQLHelper.COL1_TASK, _taskItem.getTaskName());
            values.put(TodoListSQLHelper.COL2_TASK, _taskItem.getTaskComment());
            values.put(TodoListSQLHelper.COL3_TASK, _taskItem.getHour());
            values.put(TodoListSQLHelper.COL4_TASK, _taskItem.getMinute());
            sqLiteDatabase.insertWithOnConflict(TodoListSQLHelper.TABLE_LIST, null, values, SQLiteDatabase.CONFLICT_IGNORE);

            values.clear();

            values.put(TodoListSQLHelper._ID, id);
            values.put(TodoListSQLHelper.COL1_SUN, _taskItem.isSunday());
            values.put(TodoListSQLHelper.COL2_MON, _taskItem.isMonday());
            values.put(TodoListSQLHelper.COL3_TUE, _taskItem.isTuesday());
            values.put(TodoListSQLHelper.COL4_WED, _taskItem.isWednesday());
            values.put(TodoListSQLHelper.COL5_THU, _taskItem.isThursday());
            values.put(TodoListSQLHelper.COL6_FRI, _taskItem.isFriday());
            values.put(TodoListSQLHelper.COL7_SAT, _taskItem.isSaturday());
            sqLiteDatabase.insertWithOnConflict(TodoListSQLHelper.TABLE_DAYS, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        setAlarm(_taskItem);
        updateTodoList(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }
    private void setAlarm(TaskItemModel _taskItem) {
        Calendar calendar = Calendar.getInstance();
        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(Calendar.MINUTE);

        Intent alarmIntent = new Intent("com.plugnplay.dailyplanner.ALARM");
        alarmIntent.putExtra("AlarmID", _taskItem.getId());
        alarmIntent.putExtra("AlarmTask", _taskItem.getTaskName());
        alarmIntent.putExtra("AlarmComment", _taskItem.getTaskComment());

        ArrayList<Integer> daysOfWeek = _taskItem.getDaysOfWeek();
        for(int i = 0; i < daysOfWeek.size(); i ++){
            int idDay = _taskItem.getId() + daysOfWeek.get(i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    idDay, alarmIntent, 0);
            int dayStart = daysOfWeek.get(i) - dayNow;
            int hourStart = _taskItem.getHour() - hourNow;
            int minStart = _taskItem.getMinute() - minuteNow;
//            dayStart = (dayStart < 0 ? dayStart + 7 : dayStart) * DPConst.MIN_MILLIS;
            long timeStart = calendar.getTimeInMillis() + (dayStart < 0 ? dayStart + 7 : dayStart)
                    * DPConst.DAY_MILLIS + DPConst.HRS_MILLIS * hourStart + DPConst.MIN_MILLIS * minStart;
            mAlarmManager.setRepeating(DPConst.ALARM_MODE, timeStart, DPConst.WEEK, pendingIntent);
            Log.v("ALARM_SET", "ID: " + idDay);
//            mAlarmManager.setRepeating(ALARM_MODE, calendar.getTimeInMillis(), MainActivity.INTERVAL_WEEK, mPendingIntent);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        updateTodoList(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd_AM:
                addSelect();
                break;
            case R.id.btnShift_AM:
                shiftSelect();
                break;
            case R.id.btnRemove_AM:
                removeSelect();
                break;
            case R.id.btnCancel_AM:
                doCancelSelect();
                break;
        }
    }

    private void doCancelSelect() {
        mBtnShift.setVisibility(View.GONE);
        mBtnRemove.setVisibility(View.GONE);
        mBtnCancel.setVisibility(View.GONE);
        mEtTaskName.setVisibility(View.VISIBLE);
        mBtnAdd.setVisibility(View.VISIBLE);

        for (int i = 0; i < mTaskList.size(); i++)  mTaskList.get(i).setChecked(false);
        mSelectedItems = new ArrayList<>();
        initRecycler();

    }

    private void removeSelect() {
        for (int i = 0; i < mSelectedItems.size(); i++) {
            SQLiteDatabase sqLiteDatabase = mListSQLHelper.getWritableDatabase();
            String id = String.valueOf(mSelectedItems.get(i).getId());
            sqLiteDatabase.delete(TodoListSQLHelper.TABLE_LIST, "_id=" + id, null);
            sqLiteDatabase.delete(TodoListSQLHelper.TABLE_DAYS, "_id=" + id, null);
            mTaskList.remove(mSelectedItems.get(i));

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                        Intent cancelServiceIntent = new Intent(mContext, AlarmReceiver.class);
            Intent cancelServiceIntent = new Intent("com.plugnplay.dailyplanner.ALARM");
            ArrayList<Integer> daysOfWeek = mSelectedItems.get(i).getDaysOfWeek();
            for(int j = 0; j < daysOfWeek.size(); j ++) {
                int idDay = mSelectedItems.get(j).getId() + daysOfWeek.get(j);
                PendingIntent cancelServicePendingIntent = PendingIntent.getBroadcast(
                        this, idDay, cancelServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.cancel(cancelServicePendingIntent);
                Log.v("ALARM_REMOVE", "ID: " + idDay);
            }
        }
        doCancelSelect();
    }

    private void shiftSelect() {
        Bundle args = new Bundle();
        args.putSerializable("shiftItems", (Serializable) mSelectedItems);

        Intent shiftItems = new Intent(MainActivity.this, AlarmPopUp.class);
        shiftItems.putExtras(args);
        startActivityForResult(shiftItems, 111);
        doCancelSelect();
    }

    private void addSelect() {
        if(mEtTaskName.getText().length() > 0)
            addTaskItem(mEtTaskName.getText().toString());
        mEtTaskName.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Shifted successfully!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
