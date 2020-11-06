package com.example.myapplication.activity;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.interface1.UserAddressService;
import com.example.myapplication.interface1.UserImageService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.District;
import com.example.myapplication.models.Province;
import com.example.myapplication.models.Regencies;
import com.example.myapplication.models.Village;
import com.example.myapplication.utlities.AppService;
import com.example.myapplication.utlities.DialogUtility;
import com.example.myapplication.utlities.RetrofitUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressFragment extends Fragment {
    private String TAG = "userAddressFragment";
    private View view;
    private Retrofit retrofit;
    private Spinner spinnerProvince, spinnerRegencies, spinnerDistrict, spinnerVillage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address, container, false);
        initView();

        return view;
    }

    public void initView() {
        retrofit = RetrofitUtility.initializeRetrofit();

        spinnerProvince = view.findViewById(R.id.spin_provinces);
        spinnerRegencies = view.findViewById(R.id.spin_regencie);
        spinnerDistrict = view.findViewById(R.id.spin_district);
        spinnerVillage = view.findViewById(R.id.spin_villages);

        getProvince();
    }

    private void getProvince(){
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> result = userAddressService.getProvince(AppService.getToken());

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Log.e(TAG, "onResponse:" + response.body());
                Gson gson = new Gson();
                ApiResult apiResult = response.body();

                Type listType = new TypeToken<List<Province>>(){}.getType();

                List<Province> provinces = gson.fromJson(gson.toJson(apiResult.getData()), listType);
                setProvinceSpinner(provinces);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {

            }
        });
    }

    private void setProvinceSpinner(List<Province> provinceList) {
        List<String> provinces = new ArrayList<>();
        provinces.add("Choose Province");

        for (Province province : provinceList) {
            provinces.add(province.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, provinces) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(dataAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: " + position);
                String selectedId = position == 0 ? null : String.valueOf(position - 1);

                if (selectedId == null) {
                    Log.e(TAG, "onItemSelected: " + null);
                } else {
                    Log.e(TAG, "pilihan selain itu ");
                    Log.e(TAG, "onItemSelected: " + provinceList.get(Integer.parseInt(selectedId)).getId());

                    int provinceId = provinceList.get(Integer.parseInt(selectedId)).getId();

                    getRegencies(provinceId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + parent);
            }
        });
    }

    private void getRegencies(int provinceId){
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> result = userAddressService.getRegencies(AppService.getToken(), provinceId);

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
            Log.e(TAG, "onResponse: " + response.body());
            Gson gson = new Gson();
            ApiResult apiResult = response.body();
            Type listType = new TypeToken<List<Regencies>>(){}.getType();
            List<Regencies> regencies = gson.fromJson(gson.toJson(apiResult.getData()), listType);
            setRegenciesSpinner(regencies);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {

            }
        });

    }

    private void setRegenciesSpinner(List<Regencies> regenciesList) {
        List<String> regency = new ArrayList<>();
        regency.add("Choose Regencie");

        for (Regencies regencies : regenciesList) {
            regency.add(regencies.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regency) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegencies.setAdapter(dataAdapter);
        spinnerRegencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: " + position);
                String selectedId = position == 0 ? null : String.valueOf(position - 1);

                if (selectedId == null) {
                    Log.e(TAG, "onItemSelected: " + null);
                } else {
                    Log.e(TAG, "pilihan selain itu ");

                    int regencyId = regenciesList.get(Integer.parseInt(selectedId)).getId();

                    Log.e(TAG, "onItemSelected: " + regencyId);

                    getDistrict(regencyId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + parent);
            }

        });

    }

    private void getDistrict(int regencyId) {

        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> result = userAddressService.getDistrict(AppService.getToken(),regencyId);
        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Gson gson = new Gson();
                ApiResult apiResult = response.body();

                Type listType = new TypeToken<List<District>>(){}.getType();
                List<District> district = gson.fromJson(gson.toJson(apiResult.getData()), listType);
                setDistrictSpinner(district);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }
    private void setDistrictSpinner(List<District> districtList) {
        List<String> districts = new ArrayList<>();
        districts.add("Choose District");

        for (District district : districtList) {
            districts.add(district.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, districts) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(dataAdapter);
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: " + position);
                String selectedId = position == 0 ? null : String.valueOf(position - 1);

                if (selectedId == null) {
                    Log.e(TAG, "onItemSelected: " + null);
                } else {
                    Log.e(TAG, "pilihan selain itu ");
                    int districtId = districtList.get(Integer.parseInt(selectedId)).getId();
                    Log.e(TAG, "onItemSelected: " + districtId);
                    getVillages(districtId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + parent);
            }
        });

    }

    private void getVillages(int districtId) {

        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> result = userAddressService.getVillage(AppService.getToken(),districtId);

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Gson gson = new Gson();
                ApiResult apiResult = response.body();

                Type listType = new TypeToken<List<Village>>() {
                }.getType();
                List<Village> villages = gson.fromJson(gson.toJson(apiResult.getData()), listType);
                Log.e(TAG, "onResponse: " + villages);
                setVillageSpinner(villages);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }
    private void setVillageSpinner(List<Village> villagesList) {
        List<String> villages = new ArrayList<>();
        villages.add("Choose Village");

        for (Village village : villagesList) {
            villages.add(village.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, villages) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVillage.setAdapter(dataAdapter);
        spinnerVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedId = position == 0 ? null : String.valueOf(position - 1);
                Log.e(TAG, "onItemSelected: " + position);
                if (selectedId == null) {
                    Log.e(TAG, "onItemSelected: " + null);
                } else {
                    Log.e(TAG, "pilihan selain itu ");
                    int villageId = villagesList.get(Integer.parseInt(selectedId)).getId();
                    Log.e(TAG, "onItemSelected: " + villageId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: " + parent);
            }
        });

    }

}
