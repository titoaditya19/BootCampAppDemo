package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.interface1.UserAddressService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.District;
import com.example.myapplication.models.Province;
import com.example.myapplication.models.Regencies;
import com.example.myapplication.models.UserAddress;
import com.example.myapplication.models.Village;
import com.example.myapplication.utlities.AppService;
import com.example.myapplication.utlities.DialogUtility;
import com.example.myapplication.utlities.RetrofitUtility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressDataFragment extends Fragment {

    private View view;
    private EditText inputAddress, inputPosCode, inputProvince, inputRegencie, inputDistrict, inputVillage;
    private Retrofit retrofit;
    private String TAG = "UserAddressData";

    private UserAddress userAddress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_addressdata, container, false);
        retrofit = RetrofitUtility.initializeRetrofit();

        initView();


        return view;
    }

    private void initView() {

        inputAddress = view.findViewById(R.id.txtAlamatAddress);
        inputPosCode = view.findViewById(R.id.txtPostalCodeAddress);
        inputProvince = view.findViewById(R.id.txtProvinceAddress);
        inputRegencie = view.findViewById(R.id.txtRegencyAddress);
        inputDistrict = view.findViewById(R.id.txtDistrictAddress);
        inputVillage = view.findViewById(R.id.txtVillageAddress);


//        getUserAddressData();
        getData();

    }

    private void getUserAddressData() {


        String userId = String.valueOf(AppService.getPerson().getId());


        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> resultCall = userAddressService.getAddressById(AppService.getToken(), userId);

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();


                Log.e(TAG, "onResponse: " + result.getData());

                if (result.isSuccess()) {
                    Log.e(TAG, "onResponse ada");


                } else {
                    Log.e(TAG, "onResponse: tidak ada");
                }

            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    private void getData() {
        ImageActivity activity = (ImageActivity) getActivity();
        UserAddress userAddress = activity.getUserAddress();

        inputAddress.setText(userAddress.getAddress());
        inputPosCode.setText(userAddress.getPostal_code());

        inputAddress.setEnabled(false);
        inputPosCode.setEnabled(false);
        inputProvince.setEnabled(false);
        inputRegencie.setEnabled(false);
        inputDistrict.setEnabled(false);
        inputVillage.setEnabled(false);

        inputProvince = view.findViewById(R.id.txtProvinceAddress);
        inputRegencie = view.findViewById(R.id.txtRegencyAddress);
        inputDistrict = view.findViewById(R.id.txtDistrictAddress);
        inputVillage = view.findViewById(R.id.txtVillageAddress);

        getProvince(Integer.parseInt(userAddress.getProvince_id()));
        getRegencie(Integer.parseInt(userAddress.getRegency_id()));
        getDistrict(Integer.parseInt(userAddress.getDistrict_id()));
        getVillage(Integer.parseInt(userAddress.getVillage_id()));

    }

    private void getProvince(int province_id) {
        retrofit = RetrofitUtility.initializeRetrofit();
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> resultCall = userAddressService.getProvinceById(AppService.getToken(), province_id);

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();

                Gson gson = new Gson();
                Province province = gson.fromJson(gson.toJson(result.getData()), Province.class);
                inputProvince.setText(province.getName());
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
//                Toasty.error(getBaseContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getRegencie(int id) {
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> resultCall = userAddressService.getRegenciesById(AppService.getToken(), id);

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();

                Gson gson = new Gson();
                Regencies regencie = gson.fromJson(gson.toJson(result.getData()), Regencies.class);
                inputRegencie.setText(regencie.getName());
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
//                Toasty.error(getBaseContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDistrict(int id) {
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> resultCall = userAddressService.getDistrictById(AppService.getToken(), id);

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();

                Gson gson = new Gson();
                District district = gson.fromJson(gson.toJson(result.getData()), District.class);
                inputDistrict.setText(district.getName());
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
//                Toasty.error(getBaseContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getVillage(int id) {
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> resultCall = userAddressService.getVillageById(AppService.getToken(), id);

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();

                Gson gson = new Gson();
                Village village = gson.fromJson(gson.toJson(result.getData()), Village.class);
                inputVillage.setText(village.getName());
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
//                Toasty.error(getBaseContext(), t.getMessage(), Toasty.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();


            }
        });
    }

}
