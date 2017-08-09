package com.harismawan.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.model.Video;
import com.harismawan.popularmovies.viewholder.VideoHolder;

import java.util.ArrayList;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoHolder> {

    private ArrayList<Video> videos;

    public VideoRecyclerAdapter(ArrayList<Video> videos) {
        this.videos = videos;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videos, parent, false);
        return new VideoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
