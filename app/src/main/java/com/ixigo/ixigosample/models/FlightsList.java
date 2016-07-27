
package com.ixigo.ixigosample.models;

import android.content.ContentValues;
import android.content.Context;

import com.ixigo.ixigosample.Constants;
import com.ixigo.ixigosample.dbManager.contracts.FlightsContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class FlightsList {

    public HashMap<String, String> airlineMap = new HashMap<>();
    public HashMap<String, String> airportMap = new HashMap<>();
    public String originCode;
    public String destinationCode;
    public String departureTime;
    public String arrivalTime;
    public String price;
    public String airlineCode;
    public String klass;
    public String date;


    public FlightsList(JSONObject object, Context context) {
        JSONObject airlineMapObject = object.optJSONObject(Constants.AIRLINE_MAP);
        JSONObject airportMapObject = object.optJSONObject(Constants.AIRPORT_MAP);
        createHashMap(airlineMapObject, airlineMap);
        createHashMap(airportMapObject, airportMap);
        JSONArray jsonArray = object.optJSONArray(Constants.FLIGHTS_DATA);
        if (jsonArray != null) {
            int len = jsonArray.length();
            if (len > 0) {
                context.getContentResolver().delete(FlightsContract.FlightDataEntry.FLIGHT_DATA, null, null);
            }
            ContentValues[] contentValues = new ContentValues[len];
            for (int i = 0; i < len; i++) {
                ContentValues values = new ContentValues();
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                originCode = jsonObject.optString(Constants.ORIGIN_CODE);
                destinationCode = jsonObject.optString(Constants.DESTINATION_CODE);
                long takeoffTime = jsonObject.optLong(Constants.TAKEOFF_TIME);
                departureTime = getDate(takeoffTime, Constants.HH_MM);
                long landingTime = jsonObject.optLong(Constants.LANDING_TIME);
                arrivalTime = getDate(landingTime, Constants.HH_MM);
                price = jsonObject.optString(Constants.PRICE);
                airlineCode = jsonObject.optString(Constants.AIRLINE_CODE);
                klass = jsonObject.optString(Constants.KLASS);
                date = getDate(takeoffTime, Constants.DATE);
                values.put(FlightsContract.FlightDataEntry.COLUMN_ORIGIN, airportMap.get(originCode));
                values.put(FlightsContract.FlightDataEntry.COLUMN_DESTINATION, airportMap.get(destinationCode));
                values.put(FlightsContract.FlightDataEntry.COLUMN_AIRLINE, airlineMap.get(airlineCode));
                values.put(FlightsContract.FlightDataEntry.COLUMN_KLASS, klass);
                values.put(FlightsContract.FlightDataEntry.COLUMN_TAKE_OF_TIME, departureTime);
                values.put(FlightsContract.FlightDataEntry.COLUMN_LANDING_TIME, arrivalTime);
                values.put(FlightsContract.FlightDataEntry.COLUMN_PRICE, Integer.valueOf(price));
                values.put(FlightsContract.FlightDataEntry.COLUMN_DATE, date);
                contentValues[i] = values;
            }
            context.getContentResolver().bulkInsert(FlightsContract.FlightDataEntry.FLIGHT_DATA, contentValues);
        }
    }

    //function to store mappings given in the API
    private void createHashMap(JSONObject object, HashMap<String, String> map) {
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = object.optString(key);
            map.put(key, value);
        }
    }


    //function to covert the server time in ms to date/time format
    public String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
