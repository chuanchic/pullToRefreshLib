package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * RecyclerView下拉刷新
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new InternalScrollViewSDK9(context, attrs);
        // viewPager.setId(R.id.recyclerView);
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        RecyclerView recyclerView = getRefreshableView();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return true;
        }
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager == null) {
            return true;
        }
        return adapter.getItemCount() == 0 || manager.findFirstCompletelyVisibleItemPosition() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        RecyclerView recyclerView = getRefreshableView();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return true;
        }
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (manager == null) {
            return true;
        }
        return manager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1;
    }

    final class InternalScrollViewSDK9 extends RecyclerView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                       int scrollY, int scrollRangeX, int scrollRangeY,
                                       int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            final boolean returnValue = super.overScrollBy(deltaX, deltaY,
                    scrollX, scrollY, scrollRangeX, scrollRangeY,
                    maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshRecyclerView.this, deltaX,
                    scrollX, deltaY, scrollY, getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight()
                        - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }
}
