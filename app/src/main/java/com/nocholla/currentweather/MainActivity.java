package com.nocholla.currentweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nocholla.currentweather.util.Constants;
import com.nocholla.currentweather.util.WeatherUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // Widgets
    private Button btnChangeCity;
    private TextView tviewWeatherName;
    private TextView tviewWeatherDescription;
    private TextView tviewWeatherTemp;
    private TextView tviewWeatherDate;
    private TextView tviewWeatherIcon;
    private TextView tviewWeatherTempSmall;
    private TextView tviewWeatherHumidity;
    private TextView tviewWeatherPressure;

    // Progress Bar
    private ProgressBar progressBar;
    Typeface weatherFont;

    // City
    String city = "San Diego, US";

    // Dialog
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
    private android.support.v7.app.AlertDialog dialog;

    /**
     * @method onCreate
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin Lock Screen Orientation for Phones to Portrait Mode
        if (!isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // Begin Lock Screen Orientation for Tablets to Landscape Mode
        if (isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Widgets
        btnChangeCity = findViewById(R.id.btn_change_city);
        tviewWeatherName = findViewById(R.id.tview_weather_name);
        tviewWeatherDescription = findViewById(R.id.tview_weather_description);
        tviewWeatherTemp = findViewById(R.id.tview_weather_temp);
        tviewWeatherDate = findViewById(R.id.tview_weather_date);
        tviewWeatherIcon = findViewById(R.id.tview_weather_icon);
        tviewWeatherTempSmall = findViewById(R.id.tview_weather_temp_small);
        tviewWeatherHumidity = findViewById(R.id.tview_weather_humidity);
        tviewWeatherPressure = findViewById(R.id.tview_weather_pressure);

        // Progress Bar
        progressBar = findViewById(R.id.progress_bar_weather);

        // Fonts
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        tviewWeatherIcon.setTypeface(weatherFont);

        // Get City
        getCity(city);

        // Change City Button
        btnChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Dialog
                openInputDialog();
            }
        });

    }

    /**
     * @method openInputDialog
     */
    public void openInputDialog() {
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);

        // Edit City
        final EditText editTxtSearchCity = view.findViewById(R.id.edittxt_weather_search);
        editTxtSearchCity.setText(city);

        // Button Cancel
        Button btnCancel = view.findViewById(R.id.btn_weather_cancel);

        // Button Submit
        Button btnSubmit = view.findViewById(R.id.btn_weather_submit);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        // Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTxtSearchCity.getText().toString().isEmpty()) {
                    city = editTxtSearchCity.getText().toString();

                    // Get City
                    getCity(city);
                }

                dialog.dismiss();
            }
        });

    }

    /**
     * @method getCity
     */
    public void getCity(String query) {
        if (WeatherUtil.isNetworkAvailable(getApplicationContext())) {
            WeatherTask task = new WeatherTask();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @Class WeatherTask
     */
    class WeatherTask extends AsyncTask < String, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String...args) {
            String xml = WeatherUtil.getWeatherUrl("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + Constants.WEATHER_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {

                // Weather JSON Object
                JSONObject weatherJSONObject = new JSONObject(xml);

                // Weather JSON Object
                String weatherBase = weatherJSONObject.getString("base");
                Log.d("DEBUG WEATHER BASE", weatherBase);

                String weatherVisibility = weatherJSONObject.getString("visibility");
                Log.d("DEBUG WEATHER VSBILITY", weatherVisibility);

                String weatherDt = weatherJSONObject.getString("dt");
                Log.d("DEBUG WEATHER DT", weatherDt);

                String weatherId = weatherJSONObject.getString("id");
                Log.d("DEBUG WEATHER ID", weatherId);

                String weatherName = weatherJSONObject.getString("name");
                Log.d("DEBUG WEATHER NAME", weatherName);

                String weatherCod = weatherJSONObject.getString("cod");
                Log.d("DEBUG WEATHER COD", weatherCod);

                if (weatherJSONObject != null) {

                    // Weather JSON Array
                    JSONObject weatherJSONArray = weatherJSONObject.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = weatherJSONObject.getJSONObject("main");

                    // Format Date
                    DateFormat df = DateFormat.getDateInstance();

                    tviewWeatherName.setText(weatherJSONObject.getString("name") + ", " + weatherJSONObject.getJSONObject("sys").getString("country"));
                    tviewWeatherDescription.setText(weatherJSONArray.getString("description"));
                    tviewWeatherTemp.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    tviewWeatherDate.setText(df.format(new Date(weatherJSONObject.getLong("dt") * 1000)));
                    tviewWeatherIcon.setText(Html.fromHtml(WeatherUtil.setWeatherIcon(weatherJSONArray.getInt("id"),
                            weatherJSONObject.getJSONObject("sys").getLong("sunrise") * 1000,
                            weatherJSONObject.getJSONObject("sys").getLong("sunset") * 1000)));
                    tviewWeatherTempSmall.setText(String.format("%.2f", main.get("temp")) + "°");
                    tviewWeatherHumidity.setText(main.getString("humidity") + "%");
                    tviewWeatherPressure.setText(main.getString("pressure") + " hPa");

                    // Progress Bar
                    progressBar.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_error_check_city), Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * Set Screen Orientation
     */
    private boolean isTablet() {
        return (this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
