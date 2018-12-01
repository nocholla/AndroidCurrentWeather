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
    private TextView tviewWeatherTemperature;
    private TextView tviewWeatherDate;
    private TextView tviewWeatherCountry;
    private TextView tviewWeatherLatitude;
    private TextView tviewWeatherLongitude;
    private TextView tviewWeatherHumidity;
    private TextView tviewWeatherPressure;
    private TextView tviewWeatherSpeed;
    private TextView tviewWeatherDegree;
    private TextView tviewWeatherTemperatureMininimum;
    private TextView tviewWeatherTemperatureMaximum;
    //private TextView tviewWeatherIcon;

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
        tviewWeatherTemperature = findViewById(R.id.tview_weather_temp);
        tviewWeatherDate = findViewById(R.id.tview_weather_date);
        tviewWeatherCountry = findViewById(R.id.tview_weather_country);
        tviewWeatherLatitude = findViewById(R.id.tview_weather_latitude);
        tviewWeatherLongitude = findViewById(R.id.tview_weather_longitude);
        tviewWeatherHumidity = findViewById(R.id.tview_weather_humidity);
        tviewWeatherPressure = findViewById(R.id.tview_weather_pressure);
        tviewWeatherSpeed = findViewById(R.id.tview_weather_speed);
        tviewWeatherDegree = findViewById(R.id.tview_weather_degree);
        tviewWeatherTemperatureMininimum = findViewById(R.id.tview_weather_temperature_minimum);
        tviewWeatherTemperatureMaximum = findViewById(R.id.tview_weather_temperature_maximum);
        //tviewWeatherIcon = findViewById(R.id.tview_weather_icon);

        // Progress Bar
        progressBar = findViewById(R.id.progress_bar_weather);

        // Fonts
        //weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        //tviewWeatherIcon.setTypeface(weatherFont);

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

                    // Coord JSON Object
                    JSONObject coord = weatherJSONObject.getJSONObject("coord");

                    // Main JSON Object
                    JSONObject main = weatherJSONObject.getJSONObject("main");

                    // Wind JSON Object
                    JSONObject wind = weatherJSONObject.getJSONObject("wind");

                    // Sys JSON Object
                    JSONObject sys = weatherJSONObject.getJSONObject("sys");

                    // Format Date
                    DateFormat df = DateFormat.getDateInstance();

                    tviewWeatherName.setText(weatherJSONObject.getString("name"));
                    tviewWeatherDescription.setText(weatherJSONArray.getString("description"));
                    tviewWeatherTemperature.setText(String.format("%.2f", main.get("temp")) + "°");
                    tviewWeatherDate.setText(df.format(new Date(weatherJSONObject.getLong("dt") * 1000)));
                    tviewWeatherCountry.setText(sys.getString("country"));
                    tviewWeatherLatitude.setText(coord.getString("lat") + "°");
                    tviewWeatherLongitude.setText(coord.getString("lon") + "°");
                    tviewWeatherHumidity.setText(main.getString("humidity") + "%");
                    tviewWeatherPressure.setText(main.getString("pressure") + " hPa");
                    tviewWeatherSpeed.setText(wind.getString("speed") + "mph");
                    tviewWeatherDegree.setText(wind.getString("deg") + "°");
                    tviewWeatherTemperatureMininimum.setText(String.format("%.2f", main.get("temp_min")) + "°");
                    tviewWeatherTemperatureMaximum.setText(String.format("%.2f", main.get("temp_max")) + "°");

//                    tviewWeatherIcon.setText(Html.fromHtml(WeatherUtil.setWeatherIcon(weatherJSONArray.getInt("id"),
//                            weatherJSONObject.getJSONObject("sys").getLong("sunrise") * 1000,
//                            weatherJSONObject.getJSONObject("sys").getLong("sunset") * 1000)));

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

/*
{
    "coord": {
        "lon": 72.85,
        "lat": 19.01
    },
    "weather": [
        {
        "id": 711,
        "main": "Smoke",
        "description": "smoke",
        "icon": "50n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 300.15,
        "pressure": 1014,
        "humidity": 54,
        "temp_min": 300.15,
        "temp_max": 300.15
    },
    "visibility": 2100,
    "wind": {
        "speed": 1.5,
        "deg": 150
    },
    "clouds": {
        "all": 0
    },
    "dt": 1543519800,
    "sys": {
        "type": 1,
        "id": 9052,
        "message": 0.0072,
        "country": "IN",
        "sunrise": 1543454671,
        "sunset": 1543494574
    },
    "id": 1275339,
    "name": "Mumbai",
    "cod": 200
}
 */
