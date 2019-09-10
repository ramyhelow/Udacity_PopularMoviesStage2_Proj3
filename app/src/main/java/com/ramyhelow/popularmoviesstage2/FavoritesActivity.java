package com.ramyhelow.popularmoviesstage2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhelow.popularmoviesstage2.adapters.FavsAdapter;
import com.ramyhelow.popularmoviesstage2.data.MovieEntity;
import com.ramyhelow.popularmoviesstage2.viewmodels.MainViewModel;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private FavsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Favorites");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setUpViewModel();
        setupTheList();
    }

    private void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavourites().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntity> taskEntries) {
                mAdapter.setFavsList(taskEntries);
            }
        });
    }

    private void setupTheList() {
        RecyclerView recyclerView = findViewById(R.id.rv_favs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        mAdapter = new FavsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
