package com.source.yin.yinlayout.pulltorefreshlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.source.yin.yinlayout.R;

public class PullToRefreshLayout extends LinearLayout {


    private View refreshLoadingView;
    private ViewDragHelper viewDragHelper;
    private RefreshListener refreshListener;
    private int pullRange;
    private float preMoveY;

    private ScrollDirection scrollDirection = null;

    public enum ScrollDirection {
        UP, DOWN,
    }

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        refreshLoadingView = LayoutInflater.from(context).inflate(R.layout.refresh_loading, this, false);
        addView(refreshLoadingView);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }


            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.d("yzh", "top = " + top + "\tdy = " + dy);

                if (dy > 0) {
                    scrollDirection = ScrollDirection.UP;
                } else {
                    scrollDirection = ScrollDirection.DOWN;
                }
                if (top > pullRange) {
                    return pullRange;
                }
                return top;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
//                return super.getViewVerticalDragRange(child);
                return pullRange;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
//                scrollDirection = null;
                if (scrollDirection == ScrollDirection.UP) {
                    // 下拉刷新松手之后，当前被拖拽的 view（一般是列表）需要回到距离顶部一个refreshLoadingView的高度的地方，
                    // 在其上面显示刷新进度条，又由于 layout 中设置了 marginTop 为一个 -refreshLoadingView 的高度，
                    // 所以在此需要另外再补偿一个 refreshLoadingView 的高度
                    viewDragHelper.settleCapturedViewAt(0, refreshLoadingView.getMeasuredHeight() * 2);
                } else {
                    viewDragHelper.settleCapturedViewAt(0, 0);
                }
                int top = releasedChild.getTop();
                if (top >= refreshLoadingView.getHeight()) {
                    if (refreshListener != null) {
                        refreshListener.onRefresh();
                    }
                }
                super.onViewReleased(releasedChild, xvel, yvel);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d("yzh", "onViewPositionChanged dy = " + dy);
                PullToRefreshLayout pullToRefreshLayout = PullToRefreshLayout.this;

                int childCount = pullToRefreshLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = pullToRefreshLayout.getChildAt(i);
                    if (childView != changedView) {
                        childView.offsetTopAndBottom(dy);
                    }
                }
                invalidate();
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }
        });

    }


    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        View childAt = getChildAt(1);
        boolean canScrollVerticallyDown = childAt.canScrollVertically(1);
        boolean canScrollVerticallyUp = childAt.canScrollVertically(-1);
        Log.d("yzh", "onTouchEvent canScrollVerticallyUp = " + canScrollVerticallyUp + "\tcanScrollVerticallyDown = " + canScrollVerticallyDown);
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                Log.d("yzh", "preMoveY = " + preMoveY + "\ty = " + y);
                if (preMoveY > y) {
                    scrollDirection = ScrollDirection.DOWN;
                } else {
                    scrollDirection = ScrollDirection.UP;
                }
                preMoveY = y;
                break;
        }

        int childCount = getChildCount();
        if (childCount < 2) {
            return super.onInterceptTouchEvent(ev);
        }
        View childAt = getChildAt(1);
        boolean canScrollVerticallyDown = childAt.canScrollVertically(1);
        boolean canScrollVerticallyUp = childAt.canScrollVertically(-1);
        Log.d("yzh", "onInterceptTouchEvent canScrollVerticallyUp = " + canScrollVerticallyUp
                + "\tcanScrollVerticallyDown = " + canScrollVerticallyDown
                + "\tscrollDirection = " + scrollDirection);


        //todo 第一次操作的时候 scrollDirection 一定是 null，待解决
        if (scrollDirection == null) {
            return super.onInterceptTouchEvent(ev);
        }
        //先不做上拉加载更多，故在此直接返回
        if (scrollDirection == ScrollDirection.DOWN) {
            return super.onInterceptTouchEvent(ev);
        }
        if (canScrollVerticallyUp && scrollDirection == ScrollDirection.UP) {
            return super.onInterceptTouchEvent(ev);
        }
        if (canScrollVerticallyDown && scrollDirection == ScrollDirection.DOWN) {
            return super.onInterceptTouchEvent(ev);
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        pullRange = refreshLoadingView.getMeasuredHeight() * 3;
        Log.d("yzh", "t = " + t
                + "\nrefreshLoadingView.getMeasuredHeight() = " + refreshLoadingView.getMeasuredHeight()
                + "\npullRange height = " + pullRange);
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = layoutParams.topMargin - refreshLoadingView.getHeight();
    }


    public void finishRefresh() {
        View contentView = getChildAt(1);
        if (viewDragHelper.smoothSlideViewTo(contentView, 0, refreshLoadingView.getMeasuredHeight())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    public interface RefreshListener {
        void onRefresh();
    }

    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
}
