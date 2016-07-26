package com.ixigo.ixigosample.dbManager.contentProvider;

import android.content.UriMatcher;
import android.net.Uri;

import com.ixigo.ixigosample.dbManager.constants.DBConstants;
import com.ixigo.ixigosample.dbManager.contracts.FlightsContract;

import static com.ixigo.ixigosample.dbManager.constants.DBConstants.FLIGHT_DETAILS;


public class Matcher {
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(DBConstants.CONTENT_AUTHORITY, FlightsContract.PATH_NAME, FLIGHT_DETAILS);
    }

    public static synchronized String getTable(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case FLIGHT_DETAILS:
                return FlightsContract.FlightDataEntry.TABLE_NAME;
        }
        return null;
    }
}
