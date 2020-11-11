package com.example.myapplication.interface1;

import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.UserAddress;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserAddressService {

    // PROVINCE
    @GET("api/provinces/getAll")
     Call<ApiResult> getProvince(@Header("Authorization") String token);

    @GET("api/provinces/getById")
    Call<ApiResult> getProvinceById(@Header("Authorization") String token, @Query("province_id") int province_id);

    // REGENCIES
    @GET("api/regencies/getByProvinceId")
    Call<ApiResult> getRegencies(@Header("Authorization") String token, @Query("province_id") int province_id);

    @GET("api/regencies/getById")
    Call<ApiResult> getRegenciesById(@Header("Authorization") String token, @Query("regency_id") int regency_id);

    // DISTRICT
    @GET("api/district/getByRegenciesId")
    Call<ApiResult> getDistrict(@Header("Authorization") String token, @Query("regencies_id") int regencies_Id);

    @GET("api/district/getById")
    Call<ApiResult> getDistrictById(@Header("Authorization") String token, @Query("district_id") int district_id);

    // VILLAGE
    @GET("api/villages/getByDistrictId")
    Call<ApiResult> getVillage(@Header("Authorization") String token, @Query("district_id") int district_Id);

    @GET("api/villages/getById")
    Call<ApiResult> getVillageById(@Header("Authorization") String token, @Query("village_id") int village_id);

    // ADDRESSS
    @GET("address/getById")
    Call<ApiResult> getAddressById(@Header("Authorization") String token, @Query("person_id") String person_id);

    @POST("address/insert")
    Call<ApiResult> insertAddress(@Header("Authorization") String token, @Body UserAddress userAddressBody);

    @GET("address/getById")
    Call<ApiResult> getAllAddress(@Header("Authorization") String token, @Query("person_id") String person_id);

}
