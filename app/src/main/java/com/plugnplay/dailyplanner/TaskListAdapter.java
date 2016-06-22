package com.plugnplay.dailyplanner;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andread on 08.06.2016.
 */
public class TaskListAdapter  extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private final List<TaskItemModel> mTaskList;
    private final AppCompatActivity mContext;

    public TaskListAdapter(AppCompatActivity _context, List<TaskItemModel> _taskList) {
        mContext = _context;
        mTaskList = _taskList;
    }

    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTaskName.setText(mTaskList.get(position).getTaskName());
        holder.mTaskComment.setText(mTaskList.get(position).getTaskComment());

        int hour = mTaskList.get(position).getHour();
        int minute = mTaskList.get(position).getMinute();
        String strHour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String strMinute = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        holder.mTaskTime.setText(String.format("%s : %s", strHour, strMinute));
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        public final TextView mTaskTime;
        public CheckBox mTaskName;
        public TextView mTaskComment;
        public ViewHolder(View _view) {
            super(_view);
            mTaskName = (CheckBox) _view.findViewById(R.id.сbTaskItemName);
            mTaskComment = (TextView) _view.findViewById(R.id.tvTaskItemComment);
            mTaskTime = (TextView) _view.findViewById(R.id.tvTaskItemTime);

        }
    }
}
