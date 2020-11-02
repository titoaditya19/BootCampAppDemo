package com.example.myapplication.utlities;

import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;

public class AppService {
    private static String token;
    private static Person person;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AppService.token = token;
    }

    public static Person getPerson() {
        return person;
    }

    public static void setPerson(Person person) {
        AppService.person = person;
    }
}
