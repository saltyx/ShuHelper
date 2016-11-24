package com.easyshu.shuhelper.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyshu.shuhelper.BR;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.model.CourseDetail;

import java.util.ArrayList;

/**
 * Created by shiyan on 2016/11/21.
 */

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.DetailHolder> {

    private ArrayList<CourseDetail> data;

    public static class DetailHolder extends RecyclerView.ViewHolder{

        private ViewDataBinding binding;

        public DetailHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    public CourseDetailAdapter(ArrayList<CourseDetail> data) {
        this.data = data;
    }

    public void changeData(ArrayList data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item,parent,false);

        return new DetailHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {

        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.detail,data.get(position));
        binding.executePendingBindings();

    }
}
