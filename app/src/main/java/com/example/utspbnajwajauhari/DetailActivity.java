package com.example.utspbnajwajauhari;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utspbnajwajauhari.api.ApiConfig;
import com.example.utspbnajwajauhari.api.ApiService;
import com.example.utspbnajwajauhari.data.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ProgressBar progressBarDetail;
    private TextView nameDetail, usernameDetail, bioDetail;
    private ImageView avatarDetail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBarDetail = findViewById(R.id.progressBarDetail);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String username = extras.getString("usernameDetail");
            ApiService apiService = ApiConfig.getApiService();
            Call<User> userCall = apiService.getUser(username);

            nameDetail = findViewById(R.id.nameDetail);
            usernameDetail = findViewById(R.id.usernameDetail);
            bioDetail = findViewById(R.id.bioDetail);
            avatarDetail = findViewById(R.id.avatarDetail);

            progressBarDetail.setVisibility(View.VISIBLE);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        progressBarDetail.setVisibility(View.GONE);
                        User user = response.body();
                        if (user != null){
                            usernameDetail.setText("Nama Pengguna : " + user.getUsername());
                            nameDetail.setText("Nama : " + user.getName());
                            bioDetail.setText("Bio : " + user.getBio());
                            Picasso.get().load(user.getAvatarUrl()).into(avatarDetail);
                        }else {
                            Toast.makeText(DetailActivity.this, "Gagal mendapatkan data pengguna", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(DetailActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}