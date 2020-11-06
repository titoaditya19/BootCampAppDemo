package com.example.myapplication.interface1;

import com.example.myapplication.models.ApiResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserAddressService {

    @GET("api/provinces/getAll")
     Call<ApiResult> getProvince(@Header("Authorization") String token);

    @GET("api/regencies/getByProvinceId")
    Call<ApiResult> getRegencies(@Header("Authorization") String token, @Query("province_Id") int province_id);

    @GET("api/district/getByRegenciesId")
    Call<ApiResult> getDistrict(@Header("Authorization") String token, @Query("regencies_Id") int regencies_Id);

    @GET("api/villages/getByDistrictId")
    Call<ApiResult> getVillage(@Header("Authorization") String token, @Query("district_Id") int district_Id);
}
