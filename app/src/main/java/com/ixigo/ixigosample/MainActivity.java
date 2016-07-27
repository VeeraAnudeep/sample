package com.ixigo.ixigosample;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ixigo.ixigosample.adapters.FlightsListAdapter;
import com.ixigo.ixigosample.dbManager.contracts.FlightsContract;
import com.ixigo.ixigosample.models.FlightsList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private FlightsListAdapter flightsListAdapter;
    private TextView sortedBy;
    private TextView zeroCase;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightsListAdapter = new FlightsListAdapter(this);
        recyclerView.setAdapter(flightsListAdapter);
        findViewById(R.id.price).setOnClickListener(this);
        findViewById(R.id.takeOf).setOnClickListener(this);
        findViewById(R.id.landing).setOnClickListener(this);
        sortedBy = (TextView) findViewById(R.id.sortedBy);
        zeroCase = (TextView) findViewById(R.id.zeroCase);
        getSupportLoaderManager().initLoader(11, null, this);
        GetJsonFile getJsonFile = new GetJsonFile();
        getJsonFile.execute();
    }

    private InputStream getInputStream(String endpoint) {
        try {
            URL url = new URL(endpoint);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            return (InputStream) url.getContent();
        } catch (Exception ex) {
            return null;
        }
    }


    private String getJson() {
        InputStream i = getInputStream("http://blog.ixigo.com/sampleflightdata.json");
        String jsonString = "";
        if (i != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(i));

            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonString = sb.toString();
        }
        return jsonString;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Only simple single column sorting is being handled here
        String sortBy = FlightsContract.FlightDataEntry.COLUMN_PRICE; //sorting it by price by default
        if (args != null) {
            sortBy = args.getString(Constants.SORT_BY);
        }
        String sortedByFormat = String.format(getString(R.string.sorted_by), sortBy);
        sortedBy.setText(sortedByFormat);

        //populating entire data assuming only single origin-destination flights
        //so if this activity gets called for a particular origin and destination then the cursorloader call changes
        //with the respective origin and destination
        return new CursorLoader(this, FlightsContract.FlightDataEntry.FLIGHT_DATA, null, null, null, sortBy + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.getCount() == 0) { //handling zeroCase if no flights are found
                zeroCase.setVisibility(View.VISIBLE);
            } else {
                zeroCase.setVisibility(View.GONE);
            }
            flightsListAdapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.price:
                bundle.putString(Constants.SORT_BY, FlightsContract.FlightDataEntry.COLUMN_PRICE);
                break;
            case R.id.takeOf:
                bundle.putString(Constants.SORT_BY, FlightsContract.FlightDataEntry.COLUMN_TAKE_OF_TIME);
                break;
            case R.id.landing:
                bundle.putString(Constants.SORT_BY, FlightsContract.FlightDataEntry.COLUMN_LANDING_TIME);
                break;
        }
        getSupportLoaderManager().restartLoader(11, bundle, this); //to apply the sort and refresh the list
    }

    //Downloads the json file and retrieves json from it
    private class GetJsonFile extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                //json is parsed and inserted in to db in flightList class
                FlightsList flightsList = new FlightsList(jsonObject, MainActivity.this);
                setTitle(flightsList.originCode + " - " + flightsList.destinationCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return getJson();
        }
    }
}

