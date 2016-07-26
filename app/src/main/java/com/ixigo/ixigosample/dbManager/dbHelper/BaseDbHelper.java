package com.ixigo.ixigosample.dbManager.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseDbHelper extends SQLiteOpenHelper {
    protected Context mContext;
    protected SQLiteQueryBuilder mChatSessionQueryBuilder = new SQLiteQueryBuilder();

    public BaseDbHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        mContext = context;
    }

    public Cursor query(String table, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder, String limit) {
        SQLiteDatabase db = getReadableDatabase();
        mChatSessionQueryBuilder.setTables(table);
        return mChatSessionQueryBuilder.query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, limit);
    }

    public long insert(String table, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(table, whereClause, whereArgs);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(table, values, whereClause, whereArgs);
    }

    public int bulkInsert(String table, ContentValues[] contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int length = contentValues.length;
            for (int i = 0; i < length; i++) {
                ContentValues contentValue = contentValues[i];
                if (contentValue == null || contentValue.size() == 0) {
                    continue;
                }
                long rowId = db.insertWithOnConflict(table, null, contentValue, SQLiteDatabase.CONFLICT_REPLACE);
                if (rowId <= 0) {
                    throw new SQLiteException("Failed to insert row into " + table);
                }
            }
            db.setTransactionSuccessful();
            return length;
        } finally {
            db.endTransaction();
        }
    }

    protected void copyDataBase(Context context, String dbName) throws IOException {
        getReadableDatabase().close();
        InputStream inputStream = context.getAssets().open(dbName);
        File file = context.getDatabasePath(dbName);
        file.mkdirs();
        OutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

}
