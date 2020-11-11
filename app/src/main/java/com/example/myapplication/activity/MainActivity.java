package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity2;
import com.example.myapplication.adapter.MovieAdapter;
import com.example.myapplication.interface1.MovieApiService;
import com.example.myapplication.models.ApiResult;
import com.example.myapplication.models.Person;
import com.example.myapplication.models.PersonImage;
import com.example.myapplication.utlities.AppService;
import com.example.myapplication.utlities.RetrofitUtility;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.myapplication.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private PersonImage personImage;
    private Person person;
    private ImageActivity imageActivity;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<com.example.myapplication.models.Movie> moviesArrayList = new ArrayList<>();
    private Movie movie;
    private boolean doubleBackToExitPressedOnce;
    private String TAG = "MainActivity";
    private MaterialToolbar topAppBar;
    private Long movieId;
    private View mainView;
    private View movieView;
    private MainActivity mainActivity;


    private Retrofit retrofit;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_item);
        setContentView(R.layout.activity_main2);
        ImageActivity imageActivity;

        ImageButton setting = findViewById(R.id.btn_setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(i);
            }
        });

//        initView();
        getMovieData();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

//    private void initView() {
//        topAppBar = findViewById(R.id.topAppBar);
//        topAppBar.setOnMenuItemClickListener(item -> {
//            String menuTitle = (String) item.getTitle();
//            if (menuTitle.equals("user_setting")) {
//                openUserSetting();
//                return true;
//            }
//            return false;
//        });
//
//    }
//
//    private void openUserSetting() {
//        Intent intent = new Intent(this, UserSettingActivity.class);
//        startActivity(intent);
//    }


    private void getMovieData() {
        retrofit = RetrofitUtility.initializeRetrofit();
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<ApiResult> resultCall = movieApiService.getAllMovie(AppService.getToken());

        resultCall.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult result = response.body();

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Movie>>() {
                }.getType();

                List<com.example.myapplication.models.Movie> movies = gson.fromJson(gson.toJson(result.getData()), listType);
                moviesArrayList.addAll(movies);
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
//                DialogUtility.closeAllDialog();
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_main);
        movieAdapter = new MovieAdapter(moviesArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);
    }

    private void toMovieDetailActivity(){
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        startActivity(intent);
        finish();
    }

}
