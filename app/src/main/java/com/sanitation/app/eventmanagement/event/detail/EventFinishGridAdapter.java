package com.sanitation.app.eventmanagement.event.detail;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sanitation.app.R;

import java.util.List;

/**
 * Created by Michael on 5/1/17.
 */

public class EventFinishGridAdapter extends RecyclerView.Adapter<EventFinishGridAdapter.ViewHolder> {
    private Context mContext;
    private List<Uri> mUris;

    void setData(List<Uri> uris) {
        mUris = uris;
        notifyDataSetChanged();
    }

    public EventFinishGridAdapter(Context context, List<Uri> uris) {
        mContext = context;
        mUris = uris;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upload_event_grid_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EventFinishGridAdapter.ViewHolder holder, int position) {
        Uri uri = mUris.get(position);

        Glide.with(mContext)
                .load(uri)
                .centerCrop().override(600, 600)
                .crossFade()
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;

        ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.uploaded_image);
        }
    }
}
