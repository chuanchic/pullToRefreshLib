package com.github.chuanchic.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用RecyclerView适配器
 */
public abstract class CommonRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected Context mContext;
    protected Fragment fragment;
    protected List<CommonEntity> list;
    protected LayoutInflater inflater;

    public CommonRecyclerViewAdapter(Context mContext, Fragment fragment, List<CommonEntity> list){
        this.mContext = mContext;
        this.fragment = fragment;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(list != null && position >= 0 && position < list.size()){
            return list.get(position).getViewType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onMCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(list != null && position >= 0 && position < list.size()){
            if(holder instanceof BaseViewHolder){
                BaseViewHolder viewHolder = (BaseViewHolder) holder;
                viewHolder.updateUI(list, position);
            }
        }
    }

    public abstract RecyclerView.ViewHolder onMCreateViewHolder(ViewGroup parent, int viewType);
}
