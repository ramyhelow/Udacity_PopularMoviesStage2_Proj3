package com.ramyhelow.popularmoviesstage2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhelow.popularmoviesstage2.data.MovieEntity;

import java.util.List;

public class FavsAdapter extends RecyclerView.Adapter<FavsAdapter.MoviesViewHolder> {

    private List<MovieEntity> mMoviesList;

    public FavsAdapter() {}

    public void setFavsList(List<MovieEntity> taskEntries) {
        mMoviesList = taskEntries;
        notifyDataSetChanged();
    }

    public List<MovieEntity> getFavsList() {
        return mMoviesList;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        MovieEntity currentMovie = mMoviesList.get(i);

        moviesViewHolder.movieTitleTv.setTextColor(Color.parseColor("#ffffff"));
        moviesViewHolder.movieTitleTv.setText(currentMovie.getName());
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        }
        return mMoviesList.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitleTv;

        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitleTv = itemView.findViewById(android.R.id.text1);
        }
    }
}
