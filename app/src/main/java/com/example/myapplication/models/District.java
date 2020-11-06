package com.example.myapplication.models;

public class District {
    private int id;
    private int regency_id;
    private String name;
    private String alt_name;
    private String latitude;
    private String longitude;

    public District() {
    }

    public District(int id, int regency_id, String name, String alt_name, String latitude, String longitude) {
        this.id = id;
        this.regency_id = regency_id;
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

    public int getRegency_id() {
        return regency_id;
    }

    public void setRegency_id(int regency_id) {
        this.regency_id = regency_id;
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
        return "District{" +
                "id=" + id +
                ", regency_id=" + regency_id +
                ", name='" + name + '\'' +
                ", alt_name='" + alt_name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
