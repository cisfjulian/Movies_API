package com.example.moviefinderapp.UI.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import com.example.moviefinderapp.databinding.ActivityMainBinding;
import com.example.moviefinderapp.network.MovieDto;
import com.example.moviefinderapp.network.MoviesResponseDto;
import com.example.moviefinderapp.network.OmdbAPI;
import com.example.moviefinderapp.network.RetrofitClient;
import com.example.moviefinderapp.UI.Adapter.MoviesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchButton.setOnClickListener(v -> {
            String searchQuery = binding.searchQueryText.getText().toString();
            searchMovie(searchQuery);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new MoviesAdapter(new ArrayList<>()));
    }

    private void searchMovie(String searchQuery) {
        OmdbAPI service = RetrofitClient.getRetrofitInstance().create(OmdbAPI.class);
        Call<MoviesResponseDto> call = service.getMovies(searchQuery, "a2935151");

        call.enqueue(new Callback<MoviesResponseDto>() {
            @Override
            public void onResponse(Call<MoviesResponseDto> call, Response<MoviesResponseDto> response) {
                if (response.isSuccessful()) {
                    MoviesResponseDto movieDto = response.body();

                    List<MovieDto> movies = response.body().getSearch();
                    if (movies != null) {
                        MoviesAdapter adapter = new MoviesAdapter(movies);
                        binding.recyclerView.setAdapter(adapter);
                        Log.d("MainActivity", "Movie: " + movieDto);
                    }

                }
            }

            @Override
            public void onFailure(Call<MoviesResponseDto> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage(), t);
            }
        });

    }
}