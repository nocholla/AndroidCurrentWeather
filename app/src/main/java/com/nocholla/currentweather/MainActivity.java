package com.nocholla.currentweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.nocholla.currentweather.adapter.WeatherRecyclerViewAdapter;
import com.nocholla.currentweather.model.Weather;
import com.nocholla.currentweather.wrapper.WeathersWrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "content view is set in onCreate method of activity");
        recyclerView = findViewById(R.id.recycler_view_weather);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        //LinearDividerItemDecoration lddecoration = new LinearDividerItemDecoration(this, Color.BLUE, 10);
        //recyclerView.addItemDecoration(lddecoration);

        new MainActivity.GetWeathersAsync().execute(this);

    }

    private class GetWeathersAsync extends AsyncTask<Context, Void, List<Weather>> {
        private String TAG = GetWeathersAsync.class.getSimpleName();
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Weather> doInBackground(Context... params) {
            context = params[0];
            Log.e(TAG, "Start Asynctask to get Weathers");
            return getWeathersFromServer();
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            super.onPostExecute(weathers);

            if(weathers != null){
                Log.e(TAG, "Populate UI recycler view with GSON converted data");

                WeatherRecyclerViewAdapter weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(context, weathers);
                recyclerView.setAdapter(weatherRecyclerViewAdapter);
            }
        }
    }

    public List<Weather> getWeathersFromServer() {
        String serviceUrl = "http://api.openweathermap.org/data/2.5/weather?q=mumbai&AppID=cbfdb21fa1793c10b14b6b6d00fbef03";
        URL url = null;

        try {
            Log.d("DEBUG CALL REST SERVICE", "Call rest service to get JSON response");

            url = new URL(serviceUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(4000);
            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Pass buffered reader to convert JSON to java object using GSON
            return convertJsonToObject(bufferedReader);

        } catch (Exception e) {
            Log.e("DEBUG ERROR PARSING", "Error in getting and parsing response");
        }

        return null;
    }

    public List<Weather> convertJsonToObject(BufferedReader bufferedReader){
        // Instantiate GSON
        final Gson gson = new Gson();

        // Pass root element type to from JSON method along with Input Stream
        WeathersWrapper weathersWrapper= gson.fromJson(bufferedReader, WeathersWrapper.class);

        List<Weather> weatherList = weathersWrapper.getWeatherList();
        Log.e("DEBUG NO OF WEATHERS", "Number of Weathers from JSON response after GSON parsing" + weatherList.size());

        return weatherList;
    }

}
