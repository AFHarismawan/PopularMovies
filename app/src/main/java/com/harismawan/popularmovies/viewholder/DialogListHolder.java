package com.harismawan.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;

public class DialogListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.button_close) public Button close;
    @BindView(R.id.progress_dialog) public ProgressBar progressDialog;
    @BindView(R.id.movie_videos) public RecyclerView videos;

    public DialogListHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
