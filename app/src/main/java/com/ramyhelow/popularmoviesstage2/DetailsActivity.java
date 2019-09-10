package com.ramyhelow.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramyhelow.popularmoviesstage2.adapters.ReviewsAdapter;
import com.ramyhelow.popularmoviesstage2.adapters.TrailersAdapter;
import com.ramyhelow.popularmoviesstage2.utils.api.GetDataInterface;
import com.ramyhelow.popularmoviesstage2.utils.api.RetrofitClient;
import com.ramyhelow.popularmoviesstage2.data.AppDatabase;
import com.ramyhelow.popularmoviesstage2.data.MovieEntity;
import com.ramyhelow.popularmoviesstage2.data.models.Movie;
import com.ramyhelow.popularmoviesstage2.data.models.Review;
import com.ramyhelow.popularmoviesstage2.data.models.ReviewResponse;
import com.ramyhelow.popularmoviesstage2.data.models.Trailer;
import com.ramyhelow.popularmoviesstage2.data.models.TrailerResponse;
import com.ramyhelow.popularmoviesstage2.utils.AppExecutors;
import com.ramyhelow.popularmoviesstage2.utils.NetworkUtils;
import com.ramyhelow.popularmoviesstage2.viewmodels.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Movie mMovie;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    private TrailersAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewAdapter;

    private AppDatabase mDatabase;
    private List<MovieEntity> favoriteMovies;
    private MovieEntity mMovieToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDatabase = AppDatabase.getInstance(this.getApplication());
        fab = findViewById(R.id.fav_fab);
        Intent intent = getIntent();
        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra("Movie");
        }
        populateUI(mMovie);
        generateTrailersList();
        generateReviewsList();
        showTrailers(mMovie.getId());
        showReviews(mMovie.getId());
        setUpViewModel();
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavourites().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable final List<MovieEntity> taskEntries) {
                favoriteMovies = taskEntries;
            }
        });
    }

    private void populateUI(Movie movie) {
        ImageView backdrop = findViewById(R.id.iv_movie_backdrop);
        TextView title = findViewById(R.id.tv_movie_title);
        TextView date = findViewById(R.id.tv_release_date);
        TextView rating = findViewById(R.id.tv_user_rating);
        TextView synopsis = findViewById(R.id.tv_synopsis);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + movie.getPosterPath();
        String backdropUrl = "http://image.tmdb.org/t/p/w500/" + movie.getBackdropPath();
        Picasso.get().load(backdropUrl).placeholder(R.drawable.placeholder).into(backdrop);

        title.setText(movie.getOriginalTitle());
        date.setText(movie.getReleaseDate());
        rating.setText(movie.getVoteAverage() + "/10");
        synopsis.setText(movie.getOverview());
    }

    public void favourite(View view) {
        if (isFavorite()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.getDao().deleteFav(mMovieToDelete);
                }
            });
            Toast.makeText(DetailsActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            String title = mMovie.getOriginalTitle();
            int id = mMovie.getId();
            final MovieEntity newFav = new MovieEntity(id, title);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.getDao().insertFav(newFav);
                }
            });
            Toast.makeText(DetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFavorite() {
        if (favoriteMovies != null) {
            for (int i = 0; i < favoriteMovies.size(); i++) {
                // The movie id exist in the favorites list
                if (favoriteMovies.get(i).getId() == mMovie.getId()) {
                    mMovieToDelete = favoriteMovies.get(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void generateTrailersList() {
        mTrailers = new ArrayList<>();
        RecyclerView trailersRecycler = findViewById(R.id.rv_trailers);
        trailersRecycler.setHasFixedSize(true);
        mTrailerAdapter = new TrailersAdapter(this, mTrailers);
        trailersRecycler.setAdapter(mTrailerAdapter);
        trailersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void generateReviewsList() {
        mReviews = new ArrayList<>();
        RecyclerView reviewsRecycler = findViewById(R.id.rv_reviews);
        reviewsRecycler.setHasFixedSize(true);
        mReviewAdapter = new ReviewsAdapter(mReviews);
        reviewsRecycler.setAdapter(mReviewAdapter);
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void showTrailers(int id) {
        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<TrailerResponse> call = service.getMovieTrailers(id, getString(R.string.tmdb_api_key));
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    TrailerResponse trailerResponse = response.body();
                    if (trailerResponse != null) {
                        mTrailers.clear();
                        mTrailers.addAll(trailerResponse.getResults());
                        mTrailerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showReviews(int id) {
        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<ReviewResponse> call = service.getMovieReviews(id, getString(R.string.tmdb_api_key));
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    ReviewResponse reviewResponse = response.body();
                    if (reviewResponse != null) {
                        mReviews.clear();
                        mReviews.addAll(reviewResponse.getResults());
                        mReviewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
