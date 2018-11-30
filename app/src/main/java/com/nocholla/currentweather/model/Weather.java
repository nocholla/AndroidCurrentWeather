package com.nocholla.currentweather.model;

public class Weather {
    private String lat;
    private String lon;
    private String country;
    private String description;
    private String temp;
    private String humidity;
    private String temp_min;
    private String temp_max;
    private String speed;
    private String deg;
    private String dt;
    private String name;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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