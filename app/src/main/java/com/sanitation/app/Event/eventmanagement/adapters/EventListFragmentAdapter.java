package com.sanitation.app.Event.eventmanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;
import com.sanitation.app.Event.eventmanagement.event.detail.EventDetailActivity;
import com.sanitation.app.Event.eventmanagement.event.detail.EventDetailFragment;
import com.sanitation.app.factory.event.Event;

import java.util.List;


public class EventListFragmentAdapter extends RecyclerView.Adapter<EventListFragmentAdapter.ViewHolder> {

    private List<Event> mEvents;

    public EventListFragmentAdapter(List<Event> items) {
        mEvents = items;
    }

    public void updateList(List<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mEvents.get(position);
        holder.mTitleView.setText(mEvents.get(position).description);
        holder.mDateView.setText(mEvents.get(position).upload_time);
        holder.mStatusView.setText(mEvents.get(position).status);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra(EventDetailFragment.ARG_ID, holder.mItem.id);
                intent.putExtra(EventDetailFragment.ARG_DESCRIPTION, holder.mItem.description);
                intent.putExtra(EventDetailFragment.ARG_TYPE, holder.mItem.type);
                intent.putExtra(EventDetailFragment.ARG_STATUS, holder.mItem.status);
                intent.putExtra(EventDetailFragment.ARG_UPLOAD_TIME, holder.mItem.upload_time);
                intent.putExtra(EventDetailFragment.ARG_DURATION, holder.mItem.duration);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public final TextView mStatusView;
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDateView = (TextView) view.findViewById(R.id.date);
            mStatusView = (TextView) view.findViewById(R.id.status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
