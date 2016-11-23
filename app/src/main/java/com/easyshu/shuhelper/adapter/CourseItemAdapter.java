package com.easyshu.shuhelper.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.easyshu.shuhelper.R;
import com.easyshu.shuhelper.handler.CourseItemHandler;
import com.easyshu.shuhelper.model.Course;

import java.util.ArrayList;

/**
 * Created by shiyan on 2016/11/20.
 */

public class CourseItemAdapter extends RecyclerView.Adapter<CourseItemAdapter.ItemHolder> {

    private ArrayList<Course> data;
    private final Context mContext;

    public static class ItemHolder extends RecyclerView.ViewHolder{

        private ViewDataBinding binding;
        public ItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding(){
            return binding;
        }
    }

    public CourseItemAdapter(Context context,ArrayList<Course> data) {
        this.mContext = context;
        this.data = data;
    }

    public void changeData(ArrayList<Course> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.course,data.get(position));
        binding.setVariable(BR.handler,new CourseItemHandler(mContext));

        binding.executePendingBindings();
    }
}
