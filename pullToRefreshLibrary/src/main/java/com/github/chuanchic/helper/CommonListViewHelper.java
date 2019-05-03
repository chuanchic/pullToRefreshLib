package com.github.chuanchic.helper;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 通用ListView帮助类
 */
public abstract class CommonListViewHelper {
    public static final int Page_First = 1;//第一页
    public static final int Page_Count = 16;//每页条数
    private Activity activity;
    private Fragment fragment;
    private View rootView;
    private MyScrollListener myScrollListener;
    public PullToRefreshListView pullToRefreshListView;
    public ListView actualListView;
    public boolean isLoadingData;//true正在加载数据，false相反
    public int page = Page_First;//第几页
    private int totalItemCount;

    public CommonListViewHelper(Activity activity, View rootView, MyScrollListener myScrollListener){
        this.activity = activity;
        this.rootView = rootView;
        this.myScrollListener = myScrollListener;
        init();
    }

    public CommonListViewHelper(Fragment fragment, View rootView, MyScrollListener myScrollListener){
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.rootView = rootView;
        this.myScrollListener = myScrollListener;
        init();
    }

    private void init() {
        pullToRefreshListView = createPullToRefreshListView();
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState){//滑动停止
                    if (actualListView != null && myScrollListener != null) {
                        int firstVisiblePosition = actualListView.getFirstVisiblePosition();
                        int lastVisiblePosition = actualListView.getLastVisiblePosition();

                        if(firstVisiblePosition == 0){//滑到顶部
                            myScrollListener.onScrollToTop();
                        }
                        if(lastVisiblePosition == totalItemCount - 1){//滑到底部
                            myScrollListener.onScrollToBottom();
                        }
                    }
                }else if(AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState){//滑动拖拽
                }else{//滑动中
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                CommonListViewHelper.this.totalItemCount = totalItemCount;
            }
        });
    }

    public void addPaddingView(int dpValue){
        if(actualListView.getFooterViewsCount() <= 1){
            View paddingView = new View(activity);
            int width = AbsListView.LayoutParams.MATCH_PARENT;

            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

            int height = (int) (dpValue * displayMetrics.density + 0.5f);
            paddingView.setLayoutParams(new AbsListView.LayoutParams(width, height));
            actualListView.addFooterView(paddingView);
        }
    }

    public interface MyScrollListener{
        void onScrollToTop();
        void onScrollToBottom();
    }

    public abstract PullToRefreshListView createPullToRefreshListView();
}
