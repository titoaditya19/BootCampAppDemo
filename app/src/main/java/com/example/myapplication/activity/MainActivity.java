package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity2;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;

public class MainActivity extends AppCompatActivity {
private PersonImage personImage;
private Person person;
private ImageActivity imageActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageActivity imageActivity;
        ImageButton setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ImageActivity.class);
//                toImageActivity();
                startActivity(i);
            }
        });
    }

//    private void toImageActivity(){
//        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
//        imageActivity.getAvatar();
//        startActivity(intent);
//        finish();
//    }
}
