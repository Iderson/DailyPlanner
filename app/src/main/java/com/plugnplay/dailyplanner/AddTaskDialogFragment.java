package com.plugnplay.dailyplanner;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

/**
 * Created by Andread on 09.06.2016.
 */
public class AddTaskDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TaskItemModel mTaskItem = new TaskItemModel();
    private int mHour;
    private int mMinute;


    public interface TaskDialogListener {
        void onFinishEditDialog(TaskItemModel _taskItem);
    }

    private EditText mEtTaskName;
    private EditText mEtComment;
    ToggleButton mSunday;
    ToggleButton mMonday;
    ToggleButton mTuesday;
    ToggleButton mWednesday;
    ToggleButton mThursday;
    ToggleButton mFriday;
    ToggleButton mSaturday;
    private Button mBtnTime;

    public AddTaskDialogFragment() {
    }

    public static AddTaskDialogFragment newInstance(String _task) {
        AddTaskDialogFragment frag = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("task", _task);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        updateData();
    }
    private void initViews(View _view) {
        mEtTaskName = (EditText) _view.findViewById(R.id.etTaskName_DF);
        mEtComment = (EditText) _view.findViewById(R.id.etComment_DF);

        mSunday = (ToggleButton) _view.findViewById(R.id.tgSu_FD);
        mMonday = (ToggleButton) _view.findViewById(R.id.tgMn_FD);
        mTuesday = (ToggleButton) _view.findViewById(R.id.tgTu_FD);
        mWednesday= (ToggleButton) _view.findViewById(R.id.tgWd_FD);
        mThursday = (ToggleButton) _view.findViewById(R.id.tgTh_FD);
        mFriday  = (ToggleButton) _view.findViewById(R.id.tgFr_FD);
        mSaturday = (ToggleButton) _view.findViewById(R.id.tgSt_FD);
        mBtnTime = (Button)_view.findViewById(R.id.btnTime_FD);

        mBtnTime.setOnClickListener(this);
        mSunday.setOnCheckedChangeListener(this);
        mMonday.setOnCheckedChangeListener(this);
        mTuesday.setOnCheckedChangeListener(this);
        mWednesday.setOnCheckedChangeListener(this);
        mThursday.setOnCheckedChangeListener(this);
        mFriday.setOnCheckedChangeListener(this);
        mSaturday.setOnCheckedChangeListener(this);

        (_view.findViewById(R.id.btnOk_FD)).setOnClickListener(this);
        (_view.findViewById(R.id.btnCancel_FD)).setOnClickListener(this);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void updateData() {
        if(getArguments().get("task") != null) {
            getDialog().setTitle("Add task");

            String task = getArguments().getString("task", "");
            mTaskItem.setTaskName(task);
            mEtTaskName.setText(task);

            mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            mMinute = Calendar.getInstance().get(Calendar.MINUTE);
            setTime(mHour, mMinute);

            int dayOfWeek = Calendar.getInstance().get(DAY_OF_WEEK);
            updateDay(dayOfWeek);
        } else if (getArguments().get("taskItem") != null){
            getDialog().setTitle("Edit task");

            SerializableItem serializableItem = (SerializableItem) getArguments().getSerializable("taskItem");
            mTaskItem = serializableItem != null ? serializableItem.getItemModel() : null;

            mEtTaskName.setText(mTaskItem.getTaskName());
            mEtComment.setText(mTaskItem.getTaskComment());

            setTime(mTaskItem.getHour(), mTaskItem.getMinute());
            for(int i = 0; i < mTaskItem.getDaysOfWeek().size(); i ++)
                updateDay(mTaskItem.getDaysOfWeek().get(i));
        }
        if (mEtTaskName.length() == 0)      mEtTaskName.requestFocus();
        else if (mEtComment.length() == 0)  mEtComment.requestFocus();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.tgSu_FD:
                mTaskItem.setSunday(isChecked);
                break;
            case R.id.tgMn_FD:
                mTaskItem.setMonday(isChecked);
                break;
            case R.id.tgTu_FD:
                mTaskItem.setTuesday(isChecked);
                break;
            case R.id.tgWd_FD:
                mTaskItem.setWednesday(isChecked);
                break;
            case R.id.tgTh_FD:
                mTaskItem.setThursday(isChecked);
                break;
            case R.id.tgFr_FD:
                mTaskItem.setFriday(isChecked);
                break;
            case R.id.tgSt_FD:
                mTaskItem.setSaturday(isChecked);
                break;
        }
    }

    private void updateDay(int _dayOfWeek) {

        switch (_dayOfWeek){
            case SUNDAY:
                mTaskItem.setSunday(true);
                mSunday.setChecked(true);
                break;
            case MONDAY:
                mTaskItem.setMonday(true);
                mMonday.setChecked(true);
                break;
            case TUESDAY:
                mTaskItem.setTuesday(true);
                mTuesday.setChecked(true);
                break;
            case WEDNESDAY:
                mTaskItem.setWednesday(true);
                mWednesday.setChecked(true);
                break;
            case THURSDAY:
                mTaskItem.setThursday(true);
                mThursday.setChecked(true);
                break;
            case FRIDAY:
                mTaskItem.setFriday(true);
                mFriday .setChecked(true);
                break;
            case SATURDAY:
                mTaskItem.setSaturday(true);
                mSaturday.setChecked(true);
                break;
        }
    }

    private void setTime(int _hour, int _minute) {
        String strHour = _hour < 10 ? "0" + String.valueOf(_hour) : String.valueOf(_hour);
        String strMinute = _minute < 10 ? "0" + String.valueOf(_minute) : String.valueOf(_minute);
        mBtnTime.setText(String.format("%s : %s", strHour, strMinute));

        mTaskItem.setHour(_hour);
        mTaskItem.setMinute(_minute);
        mHour = _hour;
        mMinute = _minute;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTime_FD) {
            TimePickerDialog tpd = new TimePickerDialog(getContext(), myCallBack, mHour, mMinute, true);
            tpd.show();
        } else if(v.getId() == R.id.btnOk_FD){
            String string = "" + mEtComment.getText().toString();
            mTaskItem.setTaskComment(string.equals("") ? "" :  string);

            string = "" + mEtTaskName.getText().toString();
            mTaskItem.setTaskName(string);

            TaskDialogListener listener = (TaskDialogListener) getActivity();
            listener.onFinishEditDialog(mTaskItem);
            getDialog().dismiss();
        } else if (v.getId() == R.id.btnCancel_FD) getDialog().dismiss();
    }
    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            setTime(hourOfDay, minute);
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}