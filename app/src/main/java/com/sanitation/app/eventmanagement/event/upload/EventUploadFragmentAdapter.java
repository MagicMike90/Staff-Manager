package com.sanitation.app.eventmanagement.event.upload;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sanitation.app.R;

import java.util.List;

/**
 * Created by Michael on 5/1/17.
 */

public class EventUploadFragmentAdapter extends RecyclerView.Adapter<EventUploadFragmentAdapter.ViewHolder> {
    private List<Uri> mUris;

    void setData(List<Uri> uris) {
        mUris = uris;
        notifyDataSetChanged();
    }
    public EventUploadFragmentAdapter(List<Uri> uris) {
        mUris = uris;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_upload_event_grid_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EventUploadFragmentAdapter.ViewHolder holder, int position) {
        Uri uri = mUris.get(position);
        holder.mUri.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mUri;

        ViewHolder(View uri) {
            super(uri);
            mUri = (ImageView)uri;
        }
    }
}
