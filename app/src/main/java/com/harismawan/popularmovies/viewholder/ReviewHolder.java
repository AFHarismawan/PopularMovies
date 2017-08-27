package com.harismawan.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.model.Review;

public class ReviewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.review_author) TextView author;
    @BindView(R.id.review_content) TextView content;

    public ReviewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Review review) {
        author.setText(review.author);
        content.setText(review.content);
    }
}
