package com.example.myapplication.activity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity2;
import com.example.myapplication.interface1.UserAddressService;
import com.example.myapplication.interface1.UserApiService;
import com.example.myapplication.interface1.UserImageService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.District;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;
import com.example.myapplication.models.Province;
import com.example.myapplication.models.Regencies;
import com.example.myapplication.models.UserAddress;
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
    private String person_id, address, province_id, regency_id, district_id, village_id, postalCode;
    private View addressView, addressDataView;
    private Retrofit retrofit;
    private Spinner spinnerProvince, spinnerRegencies, spinnerDistrict, spinnerVillage;
    private EditText inputAddress, inputPostalCode;
    private Button btnSave;
    private int provinceId, regencyId, districtId, villageId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addressView = inflater.inflate(R.layout.fragment_address, container, false);
        addressDataView = inflater.inflate(R.layout.fragment_addressdata, container, false);
        initView();

        return addressView;
    }

    public void initView() {
        retrofit = RetrofitUtility.initializeRetrofit();

        btnSave = addressView.findViewById(R.id.btnSaveAddress);
        inputAddress = addressView.findViewById(R.id.txtAlamat);
        inputPostalCode = addressView.findViewById(R.id.txtPostalCode);
        spinnerProvince = addressView.findViewById(R.id.spin_provinces);
        spinnerRegencies = addressView.findViewById(R.id.spin_regencie);
        spinnerDistrict = addressView.findViewById(R.id.spin_district);
        spinnerVillage = addressView.findViewById(R.id.spin_villages);
        getAddress();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Button save ditekan");
                address = inputAddress.getText().toString();
                province_id = String.valueOf(provinceId);
                regency_id = String.valueOf(regencyId);
                district_id = String.valueOf(districtId);
                village_id = String.valueOf(villageId);

                postalCode = inputPostalCode.getText().toString();
                person_id = AppService.getPerson().getId().toString();
                getAddress();
                UserAddress userAddressBody = new UserAddress(person_id, address, postalCode, province_id, regency_id, district_id, village_id);
                try {
                    if (address.equals("")) {
                        Toast.makeText(getContext(), "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else if (province_id.isEmpty()) {
                        Toast.makeText(getContext(), "Province tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else if (regency_id.isEmpty()) {
                        Toast.makeText(getContext(), "Regency tidak boleh kurang dari 6 karakter", Toast.LENGTH_SHORT).show();
                    } else if (district_id.isEmpty()) {
                        Toast.makeText(getContext(), "District tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }else if (village_id.isEmpty()) {
                        Toast.makeText(getContext(), "Village tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }else if (postalCode.equals("")) {
                        Toast.makeText(getContext(), "Postal Code tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        saveAddress(userAddressBody);
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    private void getAddress(){
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);
        Call<ApiResult> result = userAddressService.getAddressById(AppService.getToken(), AppService.getPerson().getId().toString());

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Log.e(TAG, "onResponse:" + response.body());
                ApiResult apiResult = response.body();
                Gson gson = new Gson();
                UserAddress userAddress = gson.fromJson(gson.toJson(apiResult.getData()), UserAddress.class);
                boolean success = apiResult.isSuccess();
                if (!success){
                    getProvince();
                } else {
                   setAddress(userAddress);
                   View view = new View(addressDataView.getContext());
                   view.showContextMenu();
                }
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {

            }
        });
    }

//    private void toAddressDataFragment(){
//        Intent fragment = new Intent(addressDataView.getContext());
//        startActivity(fragment);
//    }

    private void setAddress(UserAddress userAddressBody){
        inputAddress.setText(userAddressBody.getAddress());

    }

    private void saveAddress(UserAddress userAddressBody){
        UserAddressService userAddressService = retrofit.create(UserAddressService.class);  //instansiasi interfacenya ke retrofit
        Call<ApiResult> result = userAddressService.insertAddress(AppService.getToken(), userAddressBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResponse = response.body();
                boolean success = apiResponse.isSuccess();
                if (success) {
                    Toast.makeText(getContext(), "Berhasil Input Data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

                    provinceId = provinceList.get(Integer.parseInt(selectedId)).getId();
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

                    regencyId = regenciesList.get(Integer.parseInt(selectedId)).getId();

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
                    districtId = districtList.get(Integer.parseInt(selectedId)).getId();
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
                    villageId = villagesList.get(Integer.parseInt(selectedId)).getId();
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
