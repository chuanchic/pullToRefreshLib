package com.github.chuanchic.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.List;

/**
 * 视图基类
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    protected Context mContext;
    protected int screenWidth;//屏幕宽度

    public BaseViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        screenWidth = widthPixels < heightPixels ? widthPixels : heightPixels;
    }

    /**
     * 更新UI
     */
    public abstract void updateUI(final List<CommonEntity> list, final int position);
}
