package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.interface1.UserImageService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;
import com.example.myapplication.utlities.AppService;
import com.example.myapplication.utlities.RetrofitUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageFragment extends Fragment {

    public static final int PICK_IMAGE = 1;
    private String TAG = "UserImageFragment";
    private View view;
    private MaterialButton fileButton, saveButton, updateButton;
    private ImageView image;
    private String base64Image;
    MainActivity mainActivity;
    Person person;
    PersonImage personImage;
    ImageView mImageView;
    Button mChooseBtn;
    Button btnSave, btnUpdate, btnSetting;
    TabLayout tab_image, tab_address, tabLayout;
    private String avatar;
    private Long id;
//    private String TAG = "User Setting";
    private static final int PERMISSION_CODE = 1001;
    private Retrofit retrofit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);
        retrofit = RetrofitUtility.initializeRetrofit();
        initView();
        getAvatar();

        return view;
    }

    private void initView() {
        fileButton = view.findViewById(R.id.btn_ChooseImage);
        saveButton = view.findViewById(R.id.btn_save);
        updateButton = view.findViewById(R.id.btn_update);
        image = view.findViewById(R.id.image_view);

        saveButton.setEnabled(false);

        fileButton.setOnClickListener(v -> fileChooser());
        saveButton.setOnClickListener(v -> {
            sendImage(personImage);
        });

        updateButton.setOnClickListener(v -> {
            updateAvatar();
            Log.e(TAG, "update data: ");
        });
    }

    //function pemilihan file
    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    //event yang terjadi saat file image di pilih
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            image.setImageURI(uri);
            InputStream imageStream;
            String encodedImage = "";

            image.getLayoutParams().height = 500;
            image.getLayoutParams().width = 400;

            try {
                imageStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodedImage + encodeImage(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            base64Image = encodedImage; // hasilnya sudah dalam bentuk base64 text encoded
            Log.e(TAG, "hasil encoded image : " + encodedImage);
            saveButton.setEnabled(true);
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
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
                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateAvatar(){
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
                    Toast.makeText(getContext(), apiResult.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), apiResult.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setImageThumb(String base64String) {
        Log.e(TAG, "data image dari server api: "+ base64String );
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);
    }

    private void setButtonVisibility(int saveState, int updateState) {
        saveButton.setVisibility(saveState);
        updateButton.setVisibility(updateState);
    }


}
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.Image;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.content.ContextWrapper;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.example.myapplication.R;
//import com.example.myapplication.interface1.UserImageService;
//import com.example.myapplication.models.ApiResult;
//import com.example.myapplication.models.Person;
//import com.example.myapplication.models.PersonImage;
//import com.example.myapplication.utlities.AppService;
//import com.example.myapplication.utlities.Const;
//import com.google.android.material.tabs.TabLayout;
//import com.google.gson.Gson;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ImageFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ImageFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    MainActivity mainActivity;
//    Person person;
//    PersonImage personImage;
//    View view;
//    ImageView mImageView;
//    Button mChooseBtn;
//    Button btnSave, btnUpdate, btnSetting;
//    TabLayout tab_image, tab_address, tabLayout;
//    private String base64Image;
//    private String avatar;
//    private Long id;
//    private String TAG = "User Setting";
//    private static final int PICK_IMAGE = 1000;
//    private static final int PERMISSION_CODE = 1001;
//    private Retrofit retrofit;
//
//    public ImageFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ImageFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ImageFragment newInstance(String param1, String param2) {
//        ImageFragment fragment = new ImageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_image, container, false);
//    }
//
//    private void initView(){
//
//        btnSave = view.findViewById(R.id.btn_save);
//        mImageView = view.findViewById(R.id.image_view);
//        mChooseBtn = view.findViewById(R.id.btn_ChooseImage);
//        btnUpdate = view.findViewById(R.id.btn_update);
//        tab_address = view.findViewById(R.id.tab_address);
//        tab_image = view.findViewById(R.id.tab_image);
//        tabLayout = view.findViewById(R.id.tabLayout);
//        mChooseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick: Button save ditekan");
//                PersonImage personImage =  new PersonImage(id,avatar);
//                pickImageFromGallery();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            ==  PackageManager.PERMISSION_DENIED){
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    }
//                    else {
//                        pickImageFromGallery();
//                    }
//                }
//                else {
//                    pickImageFromGallery();
//                }
//            }
//
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                id = AppService.getPerson().getId();
//                avatar = base64Image;
//                PersonImage personImage = new PersonImage(id, base64Image);
////                personImage.setId(Long.valueOf(id));
//                sendImage(personImage);
//                Log.e("info","value id : " + id);
//            }
//        });
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PersonImage personImage = new PersonImage();
//                updateAvatar(personImage);
//            }
//        });
//
//    }
//
//    private void pickImageFromGallery() {
//        Intent intent =  new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK_IMAGE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case PERMISSION_CODE: {
//                if (grantResults.length > 0 && grantResults[0]
//                        == PackageManager.PERMISSION_GRANTED){
//                    pickImageFromGallery();
//                }
//                else {
//                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private void sendImage(PersonImage personImageBody) {
//        UserImageService userImageService = retrofit.create(UserImageService.class);  //instansiasi interfacenya ke retrofit
//        Call<ApiResult> result = userImageService.insertUserImage(AppService.getToken(), personImageBody);   // call method interfacenya
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
//                ApiResult apiResponse = response.body();
//                boolean success = apiResponse.isSuccess();
//                if (success) {
//                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ApiResult> call, Throwable t) {
//                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getAvatar(){
//        Long person_id = AppService.getPerson().getId();
//        UserImageService userImageService = retrofit.create(UserImageService.class);
//        Call<ApiResult> result = userImageService.getUserImage(AppService.getToken(), person_id);
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
//                Gson gson = new Gson();
//                ApiResult apiResult = response.body();
//                boolean success = apiResult.isSuccess();
//                if (success) {
//                    PersonImage personImageResult = gson.fromJson(gson.toJson(apiResult.getData()), PersonImage.class);
//                    setImageThumb(personImageResult.getAvatar());
//                    setButtonVisibility(View.GONE, View.VISIBLE);
//                } else {
//                    setButtonVisibility(View.VISIBLE, View.GONE);
//                }
//            }
//            @Override
//            public void onFailure(Call<ApiResult> call, Throwable t) {
//                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateAvatar(PersonImage personImageBody){
////        Long person_id = AppService.getPerson().getId();
//        personImage = new PersonImage();
//        personImage.setPerson_id(AppService.getPerson().getId());
//        personImage.setAvatar(base64Image);
//
//        UserImageService userImageService = retrofit.create(UserImageService.class);
//        Call<ApiResult> result = userImageService.updateUserImage(AppService.getToken(), personImage);
//
//        result.enqueue(new Callback<ApiResult>() {
//            @Override
//            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
//                ApiResult apiResult = response.body();
//                if (apiResult.isSuccess()){
//                    Toast.makeText(ImageActivity.this, "Berhasil Update Avatar", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ImageActivity.this, apiResult.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResult> call, Throwable t) {
//                Toast.makeText(ImageActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void initRetrofit() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(); //intercept semua log http
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient
//                .Builder()
//                .addInterceptor(interceptor)
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(Const.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
//            mImageView.setImageURI(data.getData());
//            Uri uri = data.getData();
//            InputStream imageStream;
//            String encodedImage = "";
//            try{
//                imageStream = getApplicationContext().getContentResolver().openInputStream(uri);
//                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                encodedImage = encodedImage + encodeImage(selectedImage);
//            } catch  (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            base64Image = encodedImage;
//        }
//    }
//
//    private String encodeImage(Bitmap bm) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
//        byte[] b = baos.toByteArray();
//
//        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
//
//        return encImage;
//    }
//
//    private void setImageThumb(String base64String){
//        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        mImageView.setImageBitmap(decodedByte);
//    }
//
//    private void setButtonVisibility(int saveState, int updateState) {
//        btnSave.setVisibility(saveState);
//        btnUpdate.setVisibility(updateState);
//    }
//
////    public void openUserAddressFragment() {
////        Log.e(TAG, "openUserAddressFragment: ");
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        AddressFragment strCode = new AddressFragment();
////        fragmentTransaction.replace(R.id.content, strCode, "addressFragment");
////        fragmentTransaction.commit();
////    }
//}