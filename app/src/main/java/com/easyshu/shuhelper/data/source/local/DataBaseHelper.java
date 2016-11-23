package com.easyshu.shuhelper.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shiyan on 2016/11/22.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "Info.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_COURSE_ENTRIES =
            "CREATE TABLE " + DataPersistenceContract.CourseEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.CourseEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseEntry.COLUMN_NAME_YEAR + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseEntry.COLUMN_NAME_PARAM + TEXT_TYPE + " PRIMARY KEY" +
                    " )";

    private static final String SQL_CREATE_COURS_CREDIT_AND_DETAIL_ENTRIES =
            "CREATE TABLE " + DataPersistenceContract.CourseCreditPointEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_CREDIT + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_POINT + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_PARAM + TEXT_TYPE + " PRIMARY KEY" +
                    " )";

    private static final String SQL_CREATE_STUDENT_ENTRIES =
            "CREATE TABLE " + DataPersistenceContract.StudentEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.StudentEntry.COLUMN_NAME_NAME + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_LOGIN_ENTRIES =
            "CREATE TABLE " + DataPersistenceContract.LoginEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.LoginEntry.COLUMN_NAME_STUDENT_ID + TEXT_TYPE + " PRIMARY KEY," +
                    DataPersistenceContract.LoginEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_COURSE_DETAIL_ENTRIES =
            "CREATE TABLE " + DataPersistenceContract.CourseDetailEntry.TABLE_NAME + " (" +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_NO + TEXT_TYPE + " PRIMARY KEY," +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_CREDIT + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_SCORE + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_POINT + TEXT_TYPE + COMMA_SEP +
                    DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_PARAM + TEXT_TYPE +
                    " )";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_LOGIN_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_COURSE_DETAIL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_COURSE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_COURS_CREDIT_AND_DETAIL_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
