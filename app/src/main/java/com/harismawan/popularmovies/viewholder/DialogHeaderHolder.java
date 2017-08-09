package com.harismawan.popularmovies.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;

public class DialogHeaderHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title) public TextView title;

    public DialogHeaderHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
