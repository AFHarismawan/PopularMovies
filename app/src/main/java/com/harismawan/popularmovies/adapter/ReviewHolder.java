package com.harismawan.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.model.Review;

public class ReviewHolder extends RecyclerView.ViewHolder {

    private TextView author, content;

    public ReviewHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.review_author);
        content = itemView.findViewById(R.id.review_content);
    }

    public void bind(Review review) {
        author.setText(review.author);
        content.setText(review.content);
    }
}
