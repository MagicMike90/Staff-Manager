package com.sanitation.app.staffmanagment.staff;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sanitation.app.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class StaffListFragmentAdapter extends RecyclerView.Adapter<StaffListFragmentAdapter.ViewHolder> implements Filterable {

    private final List<Staff> mStaffList;
    private final List<Staff> mFilteredStaffList;
    private StaffFilter mStaffFilter;

    public StaffListFragmentAdapter(List<Staff> items) {
        mStaffList = items;
        this.mFilteredStaffList = new ArrayList<>();
    }

    @Override
    public StaffListFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_list_content, parent, false);
        return new StaffListFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StaffListFragmentAdapter.ViewHolder holder, int position) {
        holder.mItem = mStaffList.get(position);
        holder.mStaffNameView.setText(mStaffList.get(position).staff_name);
        holder.mGenderView.setText(mStaffList.get(position).gender);
        holder.mJoinWorkDateView.setText(mStaffList.get(position).join_work_date);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, StaffDetailActivity.class);
                intent.putExtra(StaffDetailFragment.ARG_STAFF_ID, holder.mItem.id);
                intent.putExtra(StaffDetailFragment.ARG_STAFF_NAME, holder.mItem.staff_name);
                intent.putExtra(StaffDetailFragment.ARG_STAFF_GENDER, holder.mItem.gender);
                intent.putExtra(StaffDetailFragment.ARG_STAFF_JOIN_WORK_DATE, holder.mItem.join_work_date);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

    @Override
    public Filter getFilter() {
        if (mStaffFilter == null)
            mStaffFilter = new StaffFilter(this, mStaffList);
        return mStaffFilter;
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

    private static class StaffFilter extends Filter {

        private final StaffListFragmentAdapter adapter;

        private final List<Staff> originalList;

        private final List<Staff> filteredList;

        private StaffFilter(StaffListFragmentAdapter adapter, List<Staff> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Staff user : originalList) {
                    if (user.staff_name.contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.mFilteredStaffList.clear();
            adapter.mFilteredStaffList.addAll((ArrayList<Staff>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}