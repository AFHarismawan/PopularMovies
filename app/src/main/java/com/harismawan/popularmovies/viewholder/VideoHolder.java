package com.harismawan.popularmovies.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.model.Video;

public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.container) LinearLayout container;
    @BindView(R.id.video_name) TextView name;
    private Video video;

    public VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Video video) {
        this.video = video;
        container.setOnClickListener(this);
        name.setText(video.name);
    }

    @Override
    public void onClick(View view) {
        Log.d("URL", Constants.YOUTUBE_BASE_URL + video.key);
        Context context = view.getContext();
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + video.key)));
    }
}
