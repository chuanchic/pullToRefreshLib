package com.github.chuanchic.helper;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * 通用RecyclerView帮助类
 */
public abstract class CommonRecyclerViewHelper {
    protected static final int Scroll_From_Top_To_Bottom = 1;//从上往下滑动
    protected static final int Scroll_From_Bottom_To_Top = 2;//从下往上滑动
    public static final int Page_First = 1;//第一页
    public static final int Page_Count = 16;//每页条数
    protected Activity activity;
    protected Fragment fragment;
    protected View rootView;
    protected MyScrollListener myScrollListener;
    protected GridLayoutManager gridLayoutManager;
    public RecyclerView recyclerView;
    public CommonRecyclerViewAdapter adapter;
    public List<CommonEntity> list;
    public boolean isLoadingData;//true正在加载数据，false相反
    protected int scrolledOrientation;//滑动的方向
    public int totalScrolledY;//Y方向滑动的距离
    public int page = Page_First;//第几页

    public CommonRecyclerViewHelper(Activity activity, View rootView, MyScrollListener myScrollListener){
        this.activity = activity;
        this.rootView = rootView;
        this.myScrollListener = myScrollListener;
        init();
    }

    public CommonRecyclerViewHelper(Fragment fragment, View rootView, MyScrollListener myScrollListener){
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.rootView = rootView;
        this.myScrollListener = myScrollListener;
        init();
    }

    protected void init() {
        recyclerView = createRecyclerView();
        gridLayoutManager = new GridLayoutManager(activity, 72, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalScrolledY += dy;
                if(totalScrolledY > 0){
                    scrolledOrientation = Scroll_From_Bottom_To_Top;
                }else if(totalScrolledY < 0){
                    scrolledOrientation = Scroll_From_Top_To_Bottom;
                }
                if(myScrollListener != null){
                    myScrollListener.onScrolled(dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {//滑动拖拽
                } else if (RecyclerView.SCROLL_STATE_IDLE == newState) {//滑动停止
                    if (gridLayoutManager != null && myScrollListener != null) {
                        int firstPosition = gridLayoutManager.findFirstVisibleItemPosition();
                        int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                        int itemCount = gridLayoutManager.getItemCount();

                        if(scrolledOrientation == Scroll_From_Top_To_Bottom){//从上往下滑动
                            if(firstPosition == 0){//滑到顶部
                                myScrollListener.onScrollToTop();
                            }else if(lastPosition >= itemCount - 1){//滑到底部
                                myScrollListener.onScrollToBottom();
                            }
                        }else if(scrolledOrientation == Scroll_From_Bottom_To_Top){//从下往上滑动
                            if(lastPosition >= itemCount - 1){//滑到底部
                                myScrollListener.onScrollToBottom();
                            }else if(firstPosition == 0){//滑到顶部
                                myScrollListener.onScrollToTop();
                            }
                        }else{//没有滑动
                            myScrollListener.onScrollNot();
                        }
                    }
                } else {//滑动中
                }
            }
        });
    }

    /**
     * 添加没有更多条目
     * @param addInEnd true：在列表的末尾添加，false：在列表的开头添加
     * @param needScroll true：需要滚动，false：不需要滚动
     */
    public void addNoMoreItem(CommonNoMoreEntity noMoreEntity, boolean addInEnd, boolean needScroll){
        if(list != null && list.size() > 0){
            if(addInEnd){//在列表的末尾添加
                if(list.get(list.size() - 1).getViewType() == CommonViewType.NoMore){
                    return;//已经存在，直接返回
                }
                list.add(noMoreEntity);
                adapter.notifyItemRangeInserted(list.size() - 1, 1);
                if(needScroll){//需要滚动
                    scrollToPosition(list.size() - 1);
                }
            }else{//在列表的开头添加
                if(list.get(0).getViewType() == CommonViewType.NoMore){
                    return;//已经存在，直接返回
                }
                list.add(0, noMoreEntity);
                adapter.notifyItemRangeInserted(0, 1);
                if(needScroll){//需要滚动
                    scrollToPosition(0);
                }
            }
        }
    }

    /**
     * 添加加载更多条目
     * @param addInEnd true：在列表的末尾添加，false：在列表的开头添加
     * @param needScroll true：需要滚动，false：不需要滚动
     */
    public void addLoadMoreItem(CommonNoMoreEntity noMoreEntity, boolean addInEnd, boolean needScroll){
        if(list != null && list.size() > 0){
            if(addInEnd){//在列表的末尾添加
                if(list.get(list.size() - 1).getViewType() == CommonViewType.LoadMore){
                    return;//已经存在，直接返回
                }
                list.add(noMoreEntity);
                adapter.notifyItemRangeInserted(list.size() - 1, 1);
                if(needScroll){//需要滚动
                    scrollToPosition(list.size() - 1);
                }
            }else{//在列表的开头添加
                if(list.get(0).getViewType() == CommonViewType.LoadMore){
                    return;//已经存在，直接返回
                }
                list.add(0, noMoreEntity);
                adapter.notifyItemRangeInserted(0, 1);
                if(needScroll){//需要滚动
                    scrollToPosition(0);
                }
            }
        }
    }

    public void scrollToPosition(final int position){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                gridLayoutManager.scrollToPosition(position);
            }
        });
    }

    public void scrollToPosition(final int position, final int offset){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                gridLayoutManager.scrollToPositionWithOffset(position, offset);
            }
        });
    }

    public void smoothScrollToPosition(final int position){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                gridLayoutManager.smoothScrollToPosition(recyclerView, null, position);
            }
        });
    }

    public void smoothScrollBy(final int dx, final int dy){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollBy(dx, dy);
            }
        });
    }

    public interface MyScrollListener{
        void onScrollToTop();//滑到顶部
        void onScrollToBottom();//滑到底部
        void onScrollNot();//没有滑动
        void onScrolled(int dx, int dy);//滑动
    }

    public abstract RecyclerView createRecyclerView();
}
