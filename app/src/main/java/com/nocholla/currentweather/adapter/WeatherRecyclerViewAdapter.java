package com.nocholla.currentweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nocholla.currentweather.R;
import com.nocholla.currentweather.model.Weather;

import java.util.List;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.MyViewHolder> {

    private List<Weather> weathers;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView latitude;
        public TextView longitude;
        public TextView country;
        public TextView description;
        public TextView temperature;
        public TextView humidity;
        public TextView temperatureMinimum;
        public TextView temperatureMaximum;
        public TextView speed;
        public TextView degree;
        public TextView dateTime;
        public TextView name;

        public MyViewHolder(View view) {
            super(view);

            latitude = view.findViewById(R.id.tview_latitude_text);
            longitude = view.findViewById(R.id.tview_longitude_text);
            country = view.findViewById(R.id.tview_country_text);
            description = view.findViewById(R.id.tview_description_text);
            temperature = view.findViewById(R.id.tview_temperature_text);
            humidity = view.findViewById(R.id.tview_humidity_text);
            temperatureMinimum = view.findViewById(R.id.tview_temperature_minimum_text);
            temperatureMaximum = view.findViewById(R.id.tview_temperature_maximum_text);
            speed = view.findViewById(R.id.tview_speed_text);
            degree = view.findViewById(R.id.tview_degree_text);
            dateTime = view.findViewById(R.id.tview_date_time_text);
            name = view.findViewById(R.id.tview_name_text);

        }
    }

    public WeatherRecyclerViewAdapter(Context context, List<Weather> weathers) {
        mContext = context;
        this.weathers = weathers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Weather weather = weathers.get(position);

        // Set TextViews
        holder.latitude.setText(weather.getLat());
        holder.longitude.setText(weather.getLon());
        holder.country.setText(weather.getCountry());
        holder.description.setText(weather.getDescription());
        holder.temperature.setText(weather.getTemp());
        holder.humidity.setText(weather.getHumidity());
        holder.temperatureMinimum.setText(weather.getTemp_min());
        holder.temperatureMaximum.setText(weather.getTemp_max());
        holder.speed.setText(weather.getSpeed());
        holder.degree.setText(weather.getDeg());
        holder.dateTime.setText(weather.getDt());
        holder.name.setText(weather.getName());

    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private WeatherRecyclerViewAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final WeatherRecyclerViewAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

