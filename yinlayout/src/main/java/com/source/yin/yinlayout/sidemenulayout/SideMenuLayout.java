package com.source.yin.yinlayout.sidemenulayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.source.yin.yinlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 横划菜单
 * Created by yin on 2017/12/4.
 */

public class SideMenuLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private int menuStartLeft;
    private int totalRightMenuWidth;
    private OnMenuClickListener onMenuClickListener;

    //触发菜单打开的比例，比如当用户划开菜单总长度的0.3之后松手菜单自动继续打开
    private float menuTrigger = 0.3f;
    private float clickEventRange = 20;
    private float actionDownX;

    //内容
    private View contentView;
    //菜单
    private List<View> menuList;

    private MenuState menuState = MenuState.close;

    public enum MenuState {
        open, close;
    }

    public SideMenuLayout(Context context) {
        this(context, null);
    }

    public SideMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideMenuLayout(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SideMenuLayout,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SideMenuLayout_menu_trigger) {
                float aFloat = a.getFloat(attr, -1);
                if (aFloat != -1 && aFloat > 0) {
                    menuTrigger = aFloat;
                }
            } else if (attr == R.styleable.SideMenuLayout_click_event_range) {
                int anInt = a.getInt(attr, -1);
                if (anInt != -1 && anInt > 0) {
                    clickEventRange = anInt;
                }
            }
        }
        a.recycle();

        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
//                Log.d("yzh", "child = " + child.getId());
//                return child == contentView;
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                Log.d("yzh", "clampViewPositionHorizontal " + "left = " + left + "\tdx = " + dx);

                if (child == contentView) {
                    //内容栏的 x 坐标不能超过0，不能超出屏幕左边界全部菜单的宽度和
                    if (left > 0) {
                        return 0;
                    } else if (left < -totalRightMenuWidth) {
                        return -totalRightMenuWidth;
                    }
                }
                return left;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return totalRightMenuWidth;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return super.getViewVerticalDragRange(child);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
//                Log.d("yzh", "onViewPositionChanged " + "left = " + left + "\tdx = " + dx);
                SideMenuLayout sideMenuLayout = SideMenuLayout.this;
                int childCount = sideMenuLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = sideMenuLayout.getChildAt(i);
                    if (childView != changedView) {
                        childView.offsetLeftAndRight(dx);
                    }
                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
//                Log.d("yzh", "xvel = " + xvel + "\nview.getLeft() = " + menu.getLeft() + "\nmenuStartLeft = " + menuStartLeft);
                View firstMenuView = menuList.get(0);
                if (xvel < 0) {
                    open();
                } else if (xvel > 0) {
                    close();
                } else if (menuState == MenuState.close && menuStartLeft - firstMenuView.getLeft() > totalRightMenuWidth * menuTrigger) {
                    open();
                } else {
                    close();
                }
            }
        });
    }

    private void close() {
        if (viewDragHelper.smoothSlideViewTo(contentView, 0, 0)) {
//            viewDragHelper.continueSettling(true);
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (menuState == MenuState.open) {
            menuState = MenuState.close;
        }
    }

    private void open() {
        if (viewDragHelper.smoothSlideViewTo(contentView, 0 - totalRightMenuWidth, 0)) {
//            viewDragHelper.continueSettling(true);
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (menuState == MenuState.close) {
            menuState = MenuState.open;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 开始执行动画
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float absMoveX = Math.abs(actionDownX - event.getX());
                // 拖动距离大于某一个值后，发送一个cancel事件用来取消点击效果
                if (absMoveX > clickEventRange) {
                    MotionEvent obtain = MotionEvent.obtain(event);
                    obtain.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(obtain);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();

        if (childCount < 2) {
            throw new IllegalArgumentException("childCount is less than 2");
        }
        contentView = getChildAt(0);

        menuList = new ArrayList<>();
        for (int i = 1; i < childCount; i++) {
            View menuView = getChildAt(i);
            menuList.add(menuView);
            menuView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuClickListener != null) {
                        onMenuClickListener.onMenuClickListener(v);
                    }
                }
            });
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.d("yzh", "menuStartLeft = " + menuStartLeft + "\tleft = " + left + "\ttop = " + top + "\tright = " + right + "\tbottom = " + bottom);
        if (contentView != null) {
            menuStartLeft = contentView.getMeasuredWidth();
        }

        totalRightMenuWidth = 0;
        for (View menuView : menuList) {
            menuView.layout(menuStartLeft + totalRightMenuWidth, 0, menuStartLeft + totalRightMenuWidth + menuView.getMeasuredWidth(), menuView.getBottom());
            totalRightMenuWidth += menuView.getMeasuredWidth();
        }

    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {
        void onMenuClickListener(View view);
    }

    public void openMenu() {
        if (menuState == MenuState.close) {
            open();
        }
    }

    public void closeMenu() {
        if (menuState == MenuState.open) {
            close();
        }
    }
}
