package com.ramyhelow.popularmoviesstage2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhelow.popularmoviesstage2.adapters.MoviesAdapter;
import com.ramyhelow.popularmoviesstage2.utils.api.GetDataInterface;
import com.ramyhelow.popularmoviesstage2.utils.api.RetrofitClient;
import com.ramyhelow.popularmoviesstage2.data.models.Movie;
import com.ramyhelow.popularmoviesstage2.data.models.MovieResponse;
import com.ramyhelow.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickHandler {

    private ProgressBar mProgressBar;
    private MoviesAdapter mAdapter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateMovieList();
        showPopularMovies();
    }

    @Override
    public void onItemClick(int position) {
        Movie movie = mMovies.get(position);
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
    }

    private void showPopularMovies() {
        mProgressBar = findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<MovieResponse> call = service.getPopularMovies(getString(R.string.tmdb_api_key));
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    mProgressBar.setVisibility(View.GONE);

                    MovieResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        mMovies.clear();
                        mMovies.addAll(moviesResponse.getMovies());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTopRatedMovies() {
        // Show the progressbar while waiting for data to load
        mProgressBar = findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<MovieResponse> call = service.getTopRatedMovies(getString(R.string.tmdb_api_key));
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    mProgressBar.setVisibility(View.GONE);

                    MovieResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        mMovies.clear();
                        mMovies.addAll(moviesResponse.getMovies());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateMovieList() {
        mMovies = new ArrayList<>();
        RecyclerView mMoviesRecycler = findViewById(R.id.recycler_movies);
        mMoviesRecycler.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(mMovies, this);
        mMoviesRecycler.setAdapter(mAdapter);
        mMoviesRecycler.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            sort();
            return true;
        } else if (id == R.id.action_fav) {
            goToFavourites();
            return true;
        }
        return false;
    }

    private void goToFavourites() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void sort() {
        CharSequence[] sorting = {"Most Popular", "Top Rated"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By:");
        builder.setItems(sorting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {
                    showTopRatedMovies();
                } else {
                    showPopularMovies();
                }
            }
        });
        builder.show();
    }
}
