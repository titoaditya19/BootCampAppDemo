package com.example.myapplication.interface1;

import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Login;
import com.example.myapplication.models.Register;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("login")
    Call<ApiResult> userLogin(@Body Login loginBody);

    @POST("users/register")
    Call<ApiResult> userRegister(@Body Register registerBody);
}