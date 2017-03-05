package com.sanitation.app.staff;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanitation.app.R;

import java.util.List;


public class StaffListFragmentAdapter extends RecyclerView.Adapter<StaffListFragmentAdapter.ViewHolder> {

    private final List<Staff> mValues;

    public StaffListFragmentAdapter(List<Staff> items) {
        mValues = items;
    }

    @Override
    public StaffListFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_list_content, parent, false);
        return new StaffListFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StaffListFragmentAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mStaffNameView.setText(mValues.get(position).staff_name);
        holder.mGenderView.setText(mValues.get(position).gender);
        holder.mJoinWorkDateView.setText(mValues.get(position).join_work_date);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, StaffDetailActivity.class);
                intent.putExtra(StaffDetailFragment.ARG_STAFF_ID, holder.mItem.id);

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
        public final TextView mStaffNameView;
        public final TextView mGenderView;
        public final TextView mJoinWorkDateView;
        public Staff mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStaffNameView = (TextView) view.findViewById(R.id.staff_name);
            mGenderView = (TextView) view.findViewById(R.id.gender);
            mJoinWorkDateView = (TextView) view.findViewById(R.id.join_work_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStaffNameView.getText() + "'";
        }
    }
}
