package com.ramyhelow.popularmoviesstage2.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_movies")
public class MovieEntity {

    @PrimaryKey
    private int id;
    private String name;

    public MovieEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
