package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity2;
import com.example.myapplication.interface1.UserApiService;
import com.example.myapplication.interface1.UserImageService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;
import com.example.myapplication.models.Register;
import com.example.myapplication.utlities.AppService;
import com.example.myapplication.utlities.Const;
import com.example.myapplication.utlities.RetrofitUtility;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageActivity extends AppCompatActivity {
    MainActivity mainActivity;
    Person person;
    PersonImage personImage;
    ImageView mImageView;
    Button mChooseBtn;
    Button btnSave, btnUpdate, btnSetting;
    TabLayout tab_image, tab_address, tabLayout;
    private String base64Image;
    private String avatar;
    private Long id;
    private String TAG = "User Setting";
    private static final int PICK_IMAGE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Retrofit retrofit;
//    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
//        initView();
//        initRetrofit();
//        getAvatar();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabItem tabImage = findViewById(R.id.tab_image);
        TabItem tabAddress = findViewById(R.id.tab_address);
        ViewPager viewPager = findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView(){

        btnSave = findViewById(R.id.btn_save);
        mImageView = findViewById(R.id.image_view);
        mChooseBtn = findViewById(R.id.btn_ChooseImage);
        btnUpdate = findViewById(R.id.btn_update);
        tab_address = findViewById(R.id.tab_address);
        tab_image = findViewById(R.id.tab_image);
        tabLayout = findViewById(R.id.tabLayout);
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Button save ditekan");
                PersonImage personImage =  new PersonImage(id,avatar);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            ==  PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = AppService.getPerson().getId();
                avatar = base64Image;
                PersonImage personImage = new PersonImage(id, base64Image);
//                personImage.setId(Long.valueOf(id));
                sendImage(personImage);
                Log.e("info","value id : " + id);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonImage personImage = new PersonImage();
                updateAvatar(personImage);
            }
        });

    }

    private void pickImageFromGallery() {
        Intent intent =  new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendImage(PersonImage personImageBody) {
        UserImageService userImageService = retrofit.create(UserImageService.class);  //instansiasi interfacenya ke retrofit
        Call<ApiResult> result = userImageService.insertUserImage(AppService.getToken(), personImageBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResponse = response.body();
                boolean success = apiResponse.isSuccess();
                if (success) {
                    Toast.makeText(ImageActivity.this, "Berhasil Input Avatar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAvatar(){
        Long person_id = AppService.getPerson().getId();
        UserImageService userImageService = retrofit.create(UserImageService.class);
        Call<ApiResult> result = userImageService.getUserImage(AppService.getToken(), person_id);

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                Gson gson = new Gson();
                ApiResult apiResult = response.body();
                boolean success = apiResult.isSuccess();
                if (success) {
                    PersonImage personImageResult = gson.fromJson(gson.toJson(apiResult.getData()), PersonImage.class);
                    setImageThumb(personImageResult.getAvatar());
                    setButtonVisibility(View.GONE, View.VISIBLE);
                } else {
                    setButtonVisibility(View.VISIBLE, View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAvatar(PersonImage personImageBody){
//        Long person_id = AppService.getPerson().getId();
        personImage = new PersonImage();
        personImage.setPerson_id(AppService.getPerson().getId());
        personImage.setAvatar(base64Image);

        UserImageService userImageService = retrofit.create(UserImageService.class);
        Call<ApiResult> result = userImageService.updateUserImage(AppService.getToken(), personImage);

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResult = response.body();
                if (apiResult.isSuccess()){
                    Toast.makeText(ImageActivity.this, "Berhasil Update Avatar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageActivity.this, apiResult.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(); //intercept semua log http
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

//    private void getUserAvatar() {
//        String person_id = String.valueOf(AppService.getPerson().getId());
//
//        retrofit = RetrofitUtility.initializeRetrofit();
//
//        UserImageService userImageService = retrofit.create(UserImageService.class);  //instansiasi interfacenya ke retrofit
//        Call<ApiResult> result = userImageService.getUserImage(AppService.getToken(), person_id);   // call method interfacenya
//
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
//                Log.e(TAG, "User setting response : " + response);
//            }
//
//            @Override
//            public void onFailure(Call<ApiResult> call, Throwable t) {
//                Log.e(TAG, "onFailure user setting: " + t);
//            }
//        });
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                Log.e(TAG, "onTabSelected: " + position);
//                if (position == 0) {
//                    Log.e(TAG, "onTabSelected: user image");
//                    openUserImageFragment();
//                } else {
//                    Log.e(TAG, "onTabSelected: user address");
//                    openUserAddressFragment();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.e(TAG, "onTabUnselected: " + tab);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                Log.e(TAG, "onTabReselected: " + tab);
//            }
//        });
//
//    }
//
//    private void getUserImage() {
//        DialogUtility.showDialog(R.raw.world, "Loading", getActivity());
//        String userId = String.valueOf(AppService.getUser().getId());
//
//        UserImageService userImageService = retrofit.create(UserImageService.class);  //instansiasi interfacenya ke retrofit
//        Call<ApiResult> result = userImageService.getUserImage(AppService.getToken(), userId);   // call method interfacenya
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
//                Log.e(TAG, "onResponse: " + response);
//                Gson gson = new Gson();
//                ApiResult apiResult = response.body();
//
//                if (apiResult.isSuccess()) {
//                    UserImage userImageResult = gson.fromJson(gson.toJson(apiResult.getData()), UserImage.class);
//                    setImageThumb(userImageResult.getAvatar());
//                    setButtonVisibility(View.GONE, View.VISIBLE);
//                } else {
//                    setButtonVisibility(View.VISIBLE, View.GONE);
//                }
//
//
//                DialogUtility.closeAllDialog();
//            }

//    private void sendRegister(Register registerBody) {
//        UserImageService userImageService = retrofit.create(UserImageService.class);  //instansiasi interfacenya ke retrofit
//        retrofit2.Call<ApiResult> result = userImageService.insertUserImage()   // call method interfacenya
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(retrofit2.Call<ApiResult> call, Response<ApiResult> response) {
//                ApiResult apiResponse = response.body();
//                boolean success = apiResponse.isSuccess();
//                if (success) {
//                    Toast.makeText(ImageActivity.this, "Berhasil Register, Silakan Login", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ImageActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ApiResult> call, Throwable t) {
//                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            mImageView.setImageURI(data.getData());
            Uri uri = data.getData();
            InputStream imageStream;
            String encodedImage = "";
            try{
                imageStream = getApplicationContext().getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodedImage + encodeImage(selectedImage);
            } catch  (FileNotFoundException e) {
                e.printStackTrace();
            }
            base64Image = encodedImage;
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();

        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void setImageThumb(String base64String){
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mImageView.setImageBitmap(decodedByte);
    }

    private void setButtonVisibility(int saveState, int updateState) {
        btnSave.setVisibility(saveState);
        btnUpdate.setVisibility(updateState);
    }

//    public void openUserAddressFragment() {
//        Log.e(TAG, "openUserAddressFragment: ");
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        AddressFragment strCode = new AddressFragment();
//        fragmentTransaction.replace(R.id.content, strCode, "addressFragment");
//        fragmentTransaction.commit();
//    }

    private void toAddressActivity(){
        Intent intent = new Intent(ImageActivity.this, AddressActivity.class);
        startActivity(intent);
        finish();
    }
}