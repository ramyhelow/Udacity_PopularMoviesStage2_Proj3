package com.ramyhelow.popularmoviesstage2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM fav_movies")
    LiveData<List<MovieEntity>> loadAllFavs();

    @Insert
    void insertFav(MovieEntity taskEntry);

    @Delete
    void deleteFav(MovieEntity taskEntry);
}
