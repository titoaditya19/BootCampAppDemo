package com.example.myapplication.utlities;

import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("login")
    Call<ApiResult> userLogin(@Body Login loginBody);

}