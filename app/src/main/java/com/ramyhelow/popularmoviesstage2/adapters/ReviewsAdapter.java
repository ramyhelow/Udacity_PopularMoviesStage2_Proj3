package com.ramyhelow.popularmoviesstage2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramyhelow.popularmoviesstage2.R;
import com.ramyhelow.popularmoviesstage2.data.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Review> mReviewList;

    public ReviewsAdapter(List<Review> reviews) {
        mReviewList = reviews;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_review, viewGroup, false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder reviewsViewHolder, int i) {
        Review review = mReviewList.get(i);

        reviewsViewHolder.nameTextView.setText(review.getAuthor());
        reviewsViewHolder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }


    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView contentTextView;

        ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_review_author);
            contentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
