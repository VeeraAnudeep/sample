package com.ixigo.ixigosample.dbManager.constants;

import android.content.ContentUris;
import android.net.Uri;

public class DBConstants {
    public static final String CONTENT_AUTHORITY = "com.veera.ixigo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("app").build();

    public static final String QUERY_PARAMETER_LIMIT = "LIMIT";
    public static final String QUERY_PARAMETER_GROUP_BY = "GROUP BY";
    public static final String NOTIFY_URI = "NOTIFY_URI";

    public static Uri buildIdForInsert(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static final int FLIGHT_DETAILS = 1;

}
