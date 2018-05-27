package com.example.troyphattrinh.fitness_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "FitnessApp.db";


    private static final String TABLE_USER = "User";
    private static final String TABLE_HEARTRATE = "HeartRate";
    private static final String TABLE_STEPS = "Steps";

    /*Create column*/
    private static final String COLUMN_USER_NAME = "User_name";
    private static final String COLUMN_USER_PASS = "User_pass";
    private static final String COLUMN_USER_DOB = "User_dob";
    private static final String COLUMN_USER_EMAIL = "User_email";
    private static final String COLUMN_HEARTRATE_EMAIL = "Hr_email";
    private static final String COLUMN_USER_HEARTRATE = "User_HeartRate";
    private static final String COLUMN_USER_STEPS= "User_Steps";

    /*Drop always follows up with the Create Query*/
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_HR_TABLE = "DROP TABLE IF EXISTS " + TABLE_HEARTRATE;
    private String DROP_STEP_TABLE = "DROP TABLE IF EXISTS " + TABLE_STEPS;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String UTable = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASS + " TEXT,"
                + COLUMN_USER_DOB + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT" + ")";

        String HRTable = "CREATE TABLE " + TABLE_HEARTRATE + "("
                + COLUMN_HEARTRATE_EMAIL + " TEXT,"
                + COLUMN_USER_HEARTRATE + " TEXT)";

        String StepsTable = "CREATE TABLE " + TABLE_STEPS + "("
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_STEPS + " TEXT" + ")";


        db.execSQL(UTable);
        db.execSQL(HRTable);
        db.execSQL(StepsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_HR_TABLE);
        db.execSQL(DROP_STEP_TABLE);
        onCreate(db);
    }


    /*Add users from registration to User table*/
    public boolean addUser (String name, String pass, String dob, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PASS, pass);
        values.put(COLUMN_USER_DOB, dob);
        values.put(COLUMN_USER_EMAIL, email);

        long success = db.insert(TABLE_USER, null, values);

        if(success == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT User_name, User_dob, User_email FROM User WHERE User_email = " + "'" + email + "'"  , null);
        return data;
    }


/*Add heart rate record to HRTable with corresponding email address*/
    public boolean addHRate (int HRate, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_HEARTRATE, HRate);
        values.put(COLUMN_USER_EMAIL, email);

        long success = db.insert(TABLE_HEARTRATE, null, values);

        if(success == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getHRate (String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT Hr_email, User_HeartRate FROM HeartRate WHERE Hr_email = " + "'" + email + "'"  , null);
        return data;
    }


/*Add Steps count to StepsTable with corresponding email address*/
    public boolean addSteps (String steps, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_STEPS, steps);
        values.put(COLUMN_USER_EMAIL, email);

        long success = db.insert(TABLE_STEPS, null, values);

        if(success == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getSteps (String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT User_email, User_Steps FROM Steps WHERE User_email = " + "'" + email + "'"  , null);
        return data;
    }
}
