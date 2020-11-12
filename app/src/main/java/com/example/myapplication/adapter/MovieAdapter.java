package com.example.myapplication.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.MovieDetailActivity;
import com.example.myapplication.models.Movie;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private String TAG = "MovieAdapter";
    private ArrayList<Movie> dataList;
    private Button btnDetail;
    private MainActivity mainActivity;
    private Activity activity;
    private MovieDetailActivity movieDetailActivity;
    private View mView;
    private Long movieId;
    private JSONObject getJSONObject;

    public MovieAdapter(ArrayList<Movie> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        View mainView = layoutInflater.inflate(R.layout.activity_main2, parent, false);
        return new MovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.txtJudul.setText(dataList.get(position).getJudul());
        holder.txtRating.setText(dataList.get(position).getRating());
        holder.txtSinopsis.setText(dataList.get(position).getSinopsis());
        holder.imagePoster.setImageBitmap(convertToBitmap(dataList.get(position).getImage()));
        holder.btn_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieId = dataList.get(position).getId();
                Intent myIntent;
                myIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                myIntent.putExtra("movieId", movieId);
                v.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView txtJudul, txtRating, txtSinopsis;
        private ImageView imagePoster;
        private MenuView.ItemView itemView;
        private Button btn_Detail;

        public MovieViewHolder(View itemView) {
            super(itemView);
            txtJudul = itemView.findViewById(R.id.txt_judul);
            txtRating = itemView.findViewById(R.id.txt_rating);
            txtSinopsis = itemView.findViewById(R.id.txt_sinopsis);
            imagePoster = itemView.findViewById(R.id.image_poster);
            btn_Detail = itemView.findViewById(R.id.buttonDetail);
        }
    }

    private Bitmap convertToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
