package com.sanitation.app.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;

import java.util.List;


public class NoticeListFragmentAdapter extends RecyclerView.Adapter<NoticeListFragmentAdapter.ViewHolder> {

    private List<Notice> mValues;

    public NoticeListFragmentAdapter(List<Notice> items) {
        mValues = items;
    }
    public void updateList(List<Notice> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notice_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mTitleView.setText(mValues.get(position).title);
        holder.mDateView.setText(mValues.get(position).date);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", genreArrayList);
                bundle.putString(NoticeDetailFragment.ARG_ID, holder.mItem.id);
                bundle.putString(NoticeDetailFragment.ARG_TITLE, holder.mItem.title);
                bundle.putString(NoticeDetailFragment.ARG_CONTENT, holder.mItem.content);
                bundle.putString(NoticeDetailFragment.ARG_DATE, holder.mItem.date);

                Intent intent = new Intent(context, NoticeDetailActivity.class);
                intent.putExtras(bundle);
//                intent.putExtra(NoticeDetailFragment.ARG_ID, holder.mItem.id);
//                intent.putExtra(NoticeDetailFragment.ARG_DESCRIPTION, holder.mItem.description);
//                intent.putExtra(NoticeDetailFragment.ARG_CONTENT, holder.mItem.content);
//                intent.putExtra(NoticeDetailFragment.ARG_DATE, holder.mItem.date);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public Notice mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDateView = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
