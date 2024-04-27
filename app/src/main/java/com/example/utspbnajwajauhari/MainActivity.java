package com.example.utspbnajwajauhari;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utspbnajwajauhari.R;
import com.example.utspbnajwajauhari.api.ApiConfig;
import com.example.utspbnajwajauhari.api.ApiService;
import com.example.utspbnajwajauhari.data.SearchUserResponse;
import com.example.utspbnajwajauhari.data.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etQuery;
    private TextView txtSearchResult;
    private RecyclerView rvUser;
    private ProgressBar progressBar;
    private GithubUserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSearch = findViewById(R.id.btnSearch);
        etQuery = findViewById(R.id.etQuery);
        rvUser = findViewById(R.id.rvUser);
        progressBar = findViewById(R.id.progressBar);
        txtSearchResult = findViewById(R.id.txtSearchResult);

        apiService = ApiConfig.getApiService();

        etQuery.setText("NajwaJauhari");

        searchUsers("NajwaJauhari");
        txtSearchResult.setText("Hasil Pencarian \"NajwaJauhari\"");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etQuery.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    searchUsers(query);
                    txtSearchResult.setText("Hasil Pencarian \"" + query + "\"");
                } else {
                    Toast.makeText(MainActivity.this, "Mohon masukkan nama pengguna", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchUsers(String query) {
        progressBar.setVisibility(View.VISIBLE);

        Call<SearchUserResponse> call = apiService.searchUsers(query);

        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse>response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body().getUsers());
                    showRecyclerView();
                } else {
                    Toast.makeText(MainActivity.this, "Gagal mendapatkan daftar pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRecyclerView() {
        if (adapter == null) {
            adapter = new GithubUserAdapter(userList);
            rvUser.setAdapter(adapter);
            rvUser.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}