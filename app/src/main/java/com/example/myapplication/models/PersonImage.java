package com.example.myapplication.models;

public class PersonImage {
    private Long person_id;
    private String avatar;

    public PersonImage() {
    }

    public PersonImage(Long id, String avatar) {
        this.person_id = id;
        this.avatar = avatar;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long id) {
        this.person_id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "PersonImage{" +
                "person_id='" + person_id + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
