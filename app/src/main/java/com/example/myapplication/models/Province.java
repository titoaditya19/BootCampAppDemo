package com.example.myapplication.models;

public class Province {
    //"id": 12,
    //      "name": "SUMATERA UTARA",
    //      "alt_name": "SUMATERA UTARA",
    //      "latitude": "2.19235",
    //      "longitude": "99.38122"
    private int id;
    private String name;
    private String alt_name;
    private String latitude;
    private String longitude;

    public Province() {
    }

    public Province(int id, String name, String alt_name, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.alt_name = alt_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt_name() {
        return alt_name;
    }

    public void setAlt_name(String alt_name) {
        this.alt_name = alt_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alt_name='" + alt_name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
