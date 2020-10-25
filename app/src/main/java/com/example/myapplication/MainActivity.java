package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private EditText inputUsername, inputPassword;
    private Button btnLogin, btnRegister;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        inputUsername=findViewById(R.id.input_username);
        inputPassword=findViewById(R.id.input_password);
        btnLogin=findViewById(R.id.button_login);
        btnRegister=findViewById(R.id.button_register);

        btnLogin.setOnClickListener(new View.OnClickListener){
            @Override

            Log.i((TAG, "onClick: Button login ditekan"));
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();

        }

    }
}