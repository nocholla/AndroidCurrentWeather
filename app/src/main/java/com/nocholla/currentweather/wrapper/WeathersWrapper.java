package com.nocholla.currentweather.wrapper;

import com.nocholla.currentweather.model.Weather;
import java.util.List;

public class WeathersWrapper {
    private List<Weather> weathers;

    public List<Weather> getWeatherList() {
        return weathers;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weathers = weatherList;
    }
}
