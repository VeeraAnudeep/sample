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

public class SplashActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    FlightsListAdapter flightsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        flightsListAdapter = new FlightsListAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flightsListAdapter);
        getSupportLoaderManager().initLoader(11, null, this);
        GetJsonFile getJsonFile = new GetJsonFile();
        getJsonFile.execute();
    }

    private InputStream getStream(String endpoint) {
        try {
            URL url = new URL(endpoint);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            return (InputStream) url.getContent();
        } catch (Exception ex) {
            return null;
        }
    }


    private String getJsonFile() {
        InputStream i = getStream("http://blog.ixigo.com/sampleflightdata.json");
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
        //populating entire data assuming only single origin-destination flights
        return new CursorLoader(this, FlightsContract.FlightDataEntry.FLIGHT_DATA, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        flightsListAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class GetJsonFile extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                new FlightsList(jsonObject, SplashActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return getJsonFile();
        }
    }
}

