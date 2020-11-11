package com.example.myapplication.models;

public class UserAddress {
    private String person_id;
    private String address;
    private String postal_code;
    private String province_id;
    private String regency_id;
    private String district_id;
    private String village_id;

    public UserAddress() {
    }

    public UserAddress(String person_id, String address, String postal_code, String province_id, String regency_id, String district_id, String village_id) {
        this.person_id = person_id;
        this.address = address;
        this.postal_code = postal_code;
        this.province_id = province_id;
        this.regency_id = regency_id;
        this.district_id = district_id;
        this.village_id = village_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getRegency_id() {
        return regency_id;
    }

    public void setRegency_id(String regency_id) {
        this.regency_id = regency_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "person_id='" + person_id + '\'' +
                ", address='" + address + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", province_id='" + province_id + '\'' +
                ", regency_id='" + regency_id + '\'' +
                ", district_id='" + district_id + '\'' +
                ", village_id='" + village_id + '\'' +
                '}';
    }
}
