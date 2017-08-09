package com.harismawan.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;

public class DialogAlertHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image) public ImageView image;
    @BindView(R.id.text) public TextView text;
    @BindView(R.id.button_ok) public Button ok;

    public DialogAlertHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
