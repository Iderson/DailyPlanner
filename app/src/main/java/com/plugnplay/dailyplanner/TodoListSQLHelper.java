package com.plugnplay.dailyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Andread on 09.06.2016.
 */
public class TodoListSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME      = "com.plugnplay.dailyplanner";
    public static final String _ID          = BaseColumns._ID;
    public static final String TABLE_LIST   = "TODO_LIST";
    public static final String COL1_TASK    = "todo_name";
    public static final String COL2_TASK    = "todo_comment";
    public static final String COL3_TASK    = "todo_hour";
    public static final String COL4_TASK    = "todo_minute";

    public static final String TABLE_DAYS   = "DAYS_WEEK";
    public static final String COL1_SUN     = "days_sun";
    public static final String COL2_MON     = "days_mon";
    public static final String COL3_TUE     = "days_tue";
    public static final String COL4_WED     = "days_wed";
    public static final String COL5_THU     = "days_thu";
    public static final String COL6_FRI     = "days_fri";
    public static final String COL7_SAT     = "days_sat";

    public TodoListSQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String createTodoListTable = "CREATE TABLE " + TABLE_LIST + " ( _id INTEGER PRIMARY KEY, " +
                COL1_TASK + " TEXT," + COL2_TASK + " TEXT," + COL3_TASK + " INTEGER," + COL4_TASK + " INTEGER)";
        sqlDB.execSQL(createTodoListTable);
        String createDaysWeekTable = "CREATE TABLE " + TABLE_DAYS + " ( _id INTEGER PRIMARY KEY, " +
                COL1_SUN + " INTEGER," + COL2_MON + " INTEGER," + COL3_TUE + " INTEGER," + COL4_WED +
                " INTEGER," + COL5_THU + " INTEGER," + COL6_FRI+ " INTEGER," + COL7_SAT + " INTEGER)";
        sqlDB.execSQL(createDaysWeekTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
        onCreate(sqlDB);
    }
}
