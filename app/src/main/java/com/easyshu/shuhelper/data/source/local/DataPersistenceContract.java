package com.easyshu.shuhelper.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by shiyan on 2016/11/22.
 */

public final class DataPersistenceContract {

    private DataPersistenceContract() {

    }

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_PARAM = "param";
    }

    public static abstract class CourseCreditPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "credit";
        public static final String COLUMN_NAME_PARAM = "param";
        public static final String COLUMN_NAME_CREDIT = "credit";
        public static final String COLUMN_NAME_POINT = "point";
    }

    public static abstract class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "student";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static abstract class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_STUDENT_ID = "student_id";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static abstract class CourseDetailEntry implements BaseColumns {
        public static final String TABLE_NAME = "detail";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NO = "no";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CREDIT = "credit";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_NAME_POINT = "point";
        public static final String COLUMN_NAME_PARAM = "param";
    }
}
