package com.ixigo.ixigosample.dbManager.contentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ixigo.ixigosample.dbManager.constants.DBConstants;
import com.ixigo.ixigosample.dbManager.dbHelper.BaseDbHelper;
import com.ixigo.ixigosample.dbManager.dbHelper.IxigoDbHelper;


public class IxigoProvider extends ContentProvider {

    private static final String TAG = IxigoProvider.class.getSimpleName();
    private IxigoDbHelper mIxigoDbHelper;
    private Context mContext;

    private synchronized BaseDbHelper getDataBaseHelper(Uri uri) {
        return mIxigoDbHelper;
    }

    private synchronized IxigoDbHelper getDbHelper() {
        return mIxigoDbHelper;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mIxigoDbHelper = new IxigoDbHelper(mContext, "ixigo.db");

        return true;
    }

    @Nullable
    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        BaseDbHelper baseDbHelper = getDataBaseHelper(uri);
        String groupBy = uri.getQueryParameter(DBConstants.QUERY_PARAMETER_GROUP_BY);
        String limit = uri.getQueryParameter(DBConstants.QUERY_PARAMETER_LIMIT);
        String notifyUri = uri.getQueryParameter(DBConstants.NOTIFY_URI);
        Cursor cursor = baseDbHelper.query(Matcher.getTable(uri), projection, selection, selectionArgs, groupBy, null, sortOrder, limit);
        ContentResolver contentResolver = mContext.getContentResolver();
        cursor.setNotificationUri(contentResolver, uri);
        if (!TextUtils.isEmpty(notifyUri)) {
            contentResolver.notifyChange(Uri.parse(notifyUri), null);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        BaseDbHelper baseDbHelper = getDataBaseHelper(uri);
        long _id = baseDbHelper.insert(Matcher.getTable(uri), values);
        notifyURI(uri);
        Uri returnUri = DBConstants.buildIdForInsert(_id);
        return returnUri;
    }

    @Override
    public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {

        BaseDbHelper baseDbHelper = getDataBaseHelper(uri);
        int rowsAffected = baseDbHelper.delete(Matcher.getTable(uri), selection, selectionArgs);
        notifyURI(uri);
        return rowsAffected;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        BaseDbHelper baseDbHelper = getDataBaseHelper(uri);
        int rowsAffected = baseDbHelper.update(Matcher.getTable(uri), values, selection, selectionArgs);
        notifyURI(uri);
        return rowsAffected;
    }

    @Override
    public synchronized int bulkInsert(Uri uri, ContentValues[] values) {
        BaseDbHelper baseDbHelper = getDataBaseHelper(uri);
        int rowsCreated = baseDbHelper.bulkInsert(Matcher.getTable(uri), values);
        notifyURI(uri);
        return rowsCreated;
    }

    private void notifyURI(Uri uri) {
        String notifyUri = uri.getQueryParameter(DBConstants.NOTIFY_URI);
        if (TextUtils.isEmpty(notifyUri)) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
    }

}
