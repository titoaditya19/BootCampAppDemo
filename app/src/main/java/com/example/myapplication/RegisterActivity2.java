package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Login;
import com.example.myapplication.models.Register;
import com.example.myapplication.utlities.Const;
import com.example.myapplication.utlities.UserApiService;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity2 extends AppCompatActivity {

    private String TAG = "RegisterActivity";
    private String username, first_name, last_name, email, password, rePassword;
    private EditText input_username, input_firstName, input_lastname, input_email, input_password, input_rePassword;
    private Button buttonRegister;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        initView();
        initRetrofit();
    }

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );
    private void initView(){
        input_username = findViewById(R.id.input_usernameRegis);
        input_firstName = findViewById(R.id.input_firstname);
        input_lastname = findViewById(R.id.input_lastname);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_passwordRegis);
        input_rePassword = findViewById(R.id.input_rePassword);
        buttonRegister = findViewById(R.id.button_registerRegis);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Button register ditekan");
                username = input_username.getText().toString();
                first_name = input_firstName.getText().toString();
                last_name = input_lastname.getText().toString();
                email = input_email.getText().toString();
                password = input_password.getText().toString();
                rePassword = input_rePassword.getText().toString();
                Register register =  new Register(username,first_name,last_name,email,password,rePassword);
            try {
                if (username.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (username.trim().length() < 4) {
                    Toast.makeText(RegisterActivity2.this, "Username tidak boleh kurang dari 4 karakter", Toast.LENGTH_SHORT).show();
                } else if (first_name.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "First Name tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (password.trim().length() < 6) {
                    Toast.makeText(RegisterActivity2.this, "Password tidak boleh kurang dari 6 karakter", Toast.LENGTH_SHORT).show();
                }else if (last_name.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "Last Name tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (rePassword.equals("")) {
                    Toast.makeText(RegisterActivity2.this, "Re Type Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
                    Toast.makeText(RegisterActivity2.this, "Email tidak valid", Toast.LENGTH_SHORT).show();
                } else if (!rePassword.equals(password)){
                    Toast.makeText(RegisterActivity2.this, "Re Password tidak sama dengan Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity2.this, "BERHASILLL", Toast.LENGTH_SHORT).show();
                    sendRegister(register);
                }

            } catch (Exception e){

            }
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

    private void sendRegister(Register registerBody) {
        UserApiService userApiService = retrofit.create(UserApiService.class);  //instansiasi interfacenya ke retrofit
        Call<ApiResult> result = userApiService.userRegister(registerBody);   // call method interfacenya

        result.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResponse = response.body();
                boolean success = apiResponse.isSuccess();
                if (success) {
                    Toast.makeText(RegisterActivity2.this, "Berhasil Register, Silakan Login", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                } else {
                    Toast.makeText(RegisterActivity2.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Toast.makeText(RegisterActivity2.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void toMainActivity(){
        Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}