package com.ixigo.ixigosample.dbManager.dbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ixigo.ixigosample.dbManager.contracts.FlightsContract;


public class IxigoDbHelper extends BaseDbHelper {

    private static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "ixigo.db";

    public IxigoDbHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public IxigoDbHelper(Context context, String dbName) {
        super(context, dbName, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the tables

        final String SQL_CREATE_FLIGHT_DATA_TABLE = "CREATE TABLE " + FlightsContract.FlightDataEntry.TABLE_NAME + " (" +
                FlightsContract.FlightDataEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FlightsContract.FlightDataEntry.COLUMN_ORIGIN + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_DESTINATION + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_TAKE_OF_TIME + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_LANDING_TIME + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_PRICE + " INTEGER, " +
                FlightsContract.FlightDataEntry.COLUMN_AIRLINE + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_KLASS + " TEXT, " +
                FlightsContract.FlightDataEntry.COLUMN_DATE + " TEXT); ";


        db.execSQL(SQL_CREATE_FLIGHT_DATA_TABLE);
    }

    @Override
    public Cursor query(String table, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder, String limit) {
        return super.query(table, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, limit);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FlightsContract.FlightDataEntry.TABLE_NAME);
        onCreate(db);
    }
}
