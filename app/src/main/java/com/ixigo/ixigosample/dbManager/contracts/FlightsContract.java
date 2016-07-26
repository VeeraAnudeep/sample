package com.ixigo.ixigosample.dbManager.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.ixigo.ixigosample.dbManager.constants.DBConstants;


public class FlightsContract {

    public static final String PATH_NAME = "flight_data";

    public static final class FlightDataEntry implements BaseColumns {
        public static final Uri FLIGHT_DATA = DBConstants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_NAME).build();
        public static final String TABLE_NAME = "flight_data_table";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ORIGIN = "origin";
        public static final String COLUMN_DESTINATION = "destination";
        public static final String COLUMN_TAKE_OF_TIME = "takeOfTime";
        public static final String COLUMN_LANDING_TIME = "landingTime";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_AIRLINE = "airline";
        public static final String COLUMN_KLASS = "class";

        //Indices used for data extraction

        public static final int INDEX_ID = 0;
        public static final int INDEX_ORIGIN = INDEX_ID + 1;
        public static final int INDEX_DESTINATION = INDEX_ORIGIN + 1;
        public static final int INDEX_TAKE_OF_TIME = INDEX_DESTINATION + 1;
        public static final int INDEX_LANDING_TIME = INDEX_TAKE_OF_TIME + 1;
        public static final int INDEX_PRICE = INDEX_LANDING_TIME + 1;
        public static final int INDEX_AIRLINE = INDEX_PRICE + 1;
        public static final int INDEX_KLASS = INDEX_AIRLINE + 1;
    }
}
