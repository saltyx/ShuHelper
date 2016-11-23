package com.easyshu.shuhelper.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.easyshu.shuhelper.data.DataSource;
import com.easyshu.shuhelper.model.Course;
import com.easyshu.shuhelper.model.CourseCreditPoint;
import com.easyshu.shuhelper.model.CourseDetail;
import com.easyshu.shuhelper.model.LoginParam;
import com.easyshu.shuhelper.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiyan on 2016/11/22.
 */

public class LocalDataSource implements DataSource {

    private String tag = getClass().getName();

    private static LocalDataSource _DATA_SOURCE_;

    private DataBaseHelper mDataBaseHelper;

    private LocalDataSource(@NonNull Context context) {
        mDataBaseHelper = new DataBaseHelper(context);
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (_DATA_SOURCE_ == null) {
            synchronized (LocalDataSource.class) {
                if (_DATA_SOURCE_ == null) {
                    _DATA_SOURCE_ = new LocalDataSource(context);
                }
            }
        }
        return _DATA_SOURCE_;
    }

    @Override
    public void getAllCourse(@NonNull LoadDataCallback callback) {
        List<Course> courses = new ArrayList<>();

        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        String[] projection = {
                DataPersistenceContract.CourseEntry.COLUMN_NAME_NAME,
                DataPersistenceContract.CourseEntry.COLUMN_NAME_YEAR,
                DataPersistenceContract.CourseEntry.COLUMN_NAME_PARAM
        };

        Cursor c = db.query(
                DataPersistenceContract.CourseEntry.TABLE_NAME,projection,null,null,null,null,null
        );

        if (c != null && c.getCount() > 0 ) {
            while (c.moveToNext()){
                String name = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseEntry.COLUMN_NAME_NAME));
                String year = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseEntry.COLUMN_NAME_YEAR));
                String param = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseEntry.COLUMN_NAME_PARAM));
                courses.add(new Course(name,year,param));
            }
            callback.onDataLoaded(courses);
        }

        if (c != null) {
            c.close();
        }

        db.close();

        if (courses.isEmpty()) {
            callback.onDataNotAvailable(null);
        }
    }


    @Override
    public void getCourseDetailByParam(@NonNull String param, LoadDataCallback callback) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        List<CourseDetail> detail = new ArrayList<>();

        String[] projection = {
                DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_TITLE,
                DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_CREDIT,
                DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_POINT,
                DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_SCORE
        };

        String selection = DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_PARAM + " LIKE ? ";
        String[] selectionArgvs = {param};

        Cursor c = db.query(
                DataPersistenceContract.CourseDetailEntry.TABLE_NAME, projection, selection,selectionArgvs,
                null,null,null
        );

        if ( c!= null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String title = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_TITLE));
                String credit = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_CREDIT));
                String score = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_SCORE));
                String point = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_POINT));
                detail.add(new CourseDetail(title, credit, score, point));

            }
        }

        if ( c!= null ){
            c.close();
        }

        db.close();

        if (!detail.isEmpty()){
            callback.onDataLoaded(detail);
        } else {
            callback.onDataNotAvailable(null);
        }
    }

    @Override
    public void getCourseCreditAndPoint(@NonNull String param, @NonNull GetDataCallback callback) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        CourseCreditPoint data = null;

        String [] projection = {
                DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_PARAM
        };

        Cursor c = db.query(DataPersistenceContract.CourseCreditPointEntry.TABLE_NAME, projection, null,null,null,null,null);

        if ( c!= null && c.getCount() > 0) {
            c.moveToFirst();
            String credit = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_CREDIT));
            String point = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_POINT));

            data = new CourseCreditPoint(credit,param,point);
        }

        if (c != null ) {
            c.close();
        }

        db.close();

        if ( data != null ) {
            callback.onDataLoaded(data);
        } else {
            callback.onDataNotAvailable(null);
        }
    }

    @Override
    public void getLoginParam(@NonNull GetDataCallback callback) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        LoginParam param = null;

        String [] projection = {
                DataPersistenceContract.LoginEntry.COLUMN_NAME_STUDENT_ID,
                DataPersistenceContract.LoginEntry.COLUMN_NAME_PASSWORD
        };

        Cursor c = db.query(DataPersistenceContract.LoginEntry.TABLE_NAME, projection, null,null,null,null,null);

        if ( c!= null && c.getCount() > 0) {
            c.moveToFirst();
            String id = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.LoginEntry.COLUMN_NAME_STUDENT_ID));
            String password = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.LoginEntry.COLUMN_NAME_PASSWORD));

            param = new LoginParam(id, password);
        }

        if (c != null ) {
            c.close();
        }

        db.close();

        if ( param != null ) {
            callback.onDataLoaded(param);
        } else {
            callback.onDataNotAvailable(null);
        }
    }

    @Override
    public void getStudent(@Nullable LoginParam param, @NonNull GetDataCallback callback) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        Student student = null;

        String [] projection = {
                DataPersistenceContract.StudentEntry.COLUMN_NAME_NAME
        };

        Cursor c = db.query(DataPersistenceContract.StudentEntry.TABLE_NAME, projection, null,null,null,null,null);

        if ( c!= null && c.getCount() > 0) {
            c.moveToFirst();
            String name = c.getString(c.getColumnIndexOrThrow(DataPersistenceContract.StudentEntry.COLUMN_NAME_NAME));
            student = new Student(name);
        }

        if (c != null ) {
            c.close();
        }

        db.close();

        if ( student != null ) {
            callback.onDataLoaded(student);
        } else {
            callback.onDataNotAvailable(null);
        }
    }

    @Override
    public void saveAllCourses(@NonNull List<Course> courses) {
        if (courses != null && !courses.isEmpty()) {
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                for (Course c : courses) {

                    ContentValues values = new ContentValues();
                    values.put(DataPersistenceContract.CourseEntry.COLUMN_NAME_NAME, c.getName());
                    values.put(DataPersistenceContract.CourseEntry.COLUMN_NAME_YEAR, c.getYear());
                    values.put(DataPersistenceContract.CourseEntry.COLUMN_NAME_PARAM, c.getParam());

                    db.insertOrThrow(DataPersistenceContract.CourseEntry.TABLE_NAME, null, values);

                }
                db.setTransactionSuccessful();
            } catch (SQLiteConstraintException e) {

            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void saveStudent(@NonNull Student student) {
        if (student != null){
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                if (student.getName() != null) {
                    values.put(DataPersistenceContract.StudentEntry.COLUMN_NAME_NAME, student.getName());

                    db.insertOrThrow(DataPersistenceContract.StudentEntry.TABLE_NAME, null, values);

                    db.setTransactionSuccessful();
                }
            } catch (SQLiteConstraintException e) {

            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void saveCourseDetail(@NonNull String param, @NonNull List<CourseDetail> details) {
        if (details != null && !details.isEmpty()) {
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

            try {
                db.beginTransaction();
                for (CourseDetail detail : details) {

                    ContentValues values = new ContentValues();
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_ID, detail.getId());
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_CREDIT, detail.getCredit());
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_NO, detail.getCourseNo());
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_PARAM, param);
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_POINT, detail.getPoint());
                    values.put(DataPersistenceContract.CourseDetailEntry.COLUMN_NAME_TITLE, detail.getTitle());
                    db.insertOrThrow(DataPersistenceContract.StudentEntry.TABLE_NAME, null, values);

                }
                db.setTransactionSuccessful();
            } catch (SQLiteConstraintException e) {

            } finally {
                db.endTransaction();
                db.close();
            }

        }
    }

    @Override
    public void saveLoginParam(@NonNull LoginParam param) {
        if (param != null) {
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(DataPersistenceContract.LoginEntry.COLUMN_NAME_STUDENT_ID,param.getStudentID());
                values.put(DataPersistenceContract.LoginEntry.COLUMN_NAME_PASSWORD,param.getPassword());

                db.insertOrThrow(DataPersistenceContract.LoginEntry.TABLE_NAME,null,values);
                db.setTransactionSuccessful();
            }catch (SQLiteConstraintException e) {

            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void saveCourseCreditAndPoint(@NonNull CourseCreditPoint param) {
        if (param != null) {
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_CREDIT, param.getCredit());
                values.put(DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_PARAM, param.getParam());
                values.put(DataPersistenceContract.CourseCreditPointEntry.COLUMN_NAME_POINT, param.getPoint());

                db.insertOrThrow(DataPersistenceContract.CourseCreditPointEntry.TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            } catch (SQLiteConstraintException e) {

            } finally {
                db.endTransaction();
                db.close();
            }

        }
    }

    // 清空表
    @Override
    public void deleteCourseCreditAndPoint() {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.delete(DataPersistenceContract.CourseCreditPointEntry.TABLE_NAME,null,null);

        db.close();
    }

    @Override
    public void deleteLoginParam() {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.delete(DataPersistenceContract.LoginEntry.TABLE_NAME,null,null);

        db.close();
    }

    @Override
    public void deleteStudent() {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.delete(DataPersistenceContract.StudentEntry.TABLE_NAME,null,null);

        db.close();
    }

    @Override
    public void deleteAllCourses() {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.delete(DataPersistenceContract.CourseEntry.TABLE_NAME,null,null);

        db.close();
    }

    @Override
    public void deleteCourseDetail() {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.delete(DataPersistenceContract.CourseDetailEntry.TABLE_NAME,null,null);

        db.close();
    }
}

