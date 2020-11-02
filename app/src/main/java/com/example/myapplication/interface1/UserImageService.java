package com.example.myapplication.interface1;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface UserImageService {

        @GET("avatar/getById")
        Call<ApiResult> getUserImage(@Header("Authorization") String token, @Query("person_id") Long person_id);

        @POST("avatar/insert")
        Call<ApiResult> insertUserImage(@Header("Authorization") String token, @Body PersonImage personImage);

        @PUT("avatar/update")
        Call<ApiResult> updateUserImage(@Header("Authorization") String token, @Body PersonImage personImage);

}