package com.ramyhelow.popularmoviesstage2.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ramyhelow.popularmoviesstage2.data.AppDatabase;
import com.ramyhelow.popularmoviesstage2.data.MovieEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntity>> favoriteMovies;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoriteMovies = database.getDao().loadAllFavs();
    }

    public LiveData<List<MovieEntity>> getFavourites() {
        return favoriteMovies;
    }
}
