package com.source.yin.yinlayout.moonmenu;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.source.yin.yinlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 卫星式菜单
 * Created by yin on 2017/8/29.
 */

public class MoonMenu extends ViewGroup {

    private Context context;
    //菜单展开总角度
    private int totalAngle = 120;
    private boolean isMenuClose = true;

    private MoonMenuSwitch moonMenuSwitch;
    private List<MoonMenuItem> moonMenuItemList;
    private OnMenuItemClickListener onMenuItemClickListener;
    private OnMenuSwitchClickListener onMenuSwitchClickListener;
    private int centerX;
    private int centerY;

    public MoonMenu(Context context) {
        this(context, null);
    }

    public MoonMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoonMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MoonMenu,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MoonMenu_total_angle) {
                int totalAngle = a.getInt(attr, 120);
                if (totalAngle > 180 || totalAngle <= 0) {
                    totalAngle = 180;
                }
                this.totalAngle = totalAngle;
            }
        }
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();

                int cl = centerX - childWidth / 2;
                int ct = centerY - childHeight / 2;
                childView.layout(cl, ct, cl + childWidth, ct + childHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //当点击事件未匹配到子 View，回传到 ViewGroup 本身处理。如果菜单为展开状态，拦截事件，处理播放动画关闭菜单
        if (!isMenuClose) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                isMenuClose = true;
                moonMenuSwitch.playSwitchChangeAnim(isMenuClose);
                if (moonMenuItemList != null) {
                    for (MoonMenuItem moonMenuItem : moonMenuItemList) {
                        moonMenuItem.playMenuSwitchChangeAnimation(isMenuClose);
                    }
                }
                Drawable background = getBackground();
                if (background != null) {
                    background.setAlpha(0);
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int childCount = getChildCount();
        //主菜单按钮，布局里的最后一个View
        View centerButton = getChildAt(childCount - 1);
        centerX = getWidth() / 2;
        centerY = getHeight() - centerButton.getMeasuredHeight() / 2 - centerButton.getMeasuredHeight() / 4;

        moonMenuSwitch = new MoonMenuSwitch(centerButton);

        int delay = 50;
        //半径算法 ：通过角度计算刚好碰到手机边框的距离乘 3/4，记为 t 。根据子菜单的个数，最后的半径最小等于t的二分之一，数量越多越逼近t（不考虑子菜单个数超过6的情况）
        double t = getWidth() / 2 / Math.cos((180 - totalAngle) / 2 / 180.0 * Math.PI) * 3 / 4;
        int radius = (int) (t * (1 / 2.0 + (childCount - 1) / 12.0));

        moonMenuItemList = new ArrayList<>();

        for (int i = 0; i < childCount - 1; i++) {
            //从左底边开始的子 item 的角度。注意由于中心按钮也包含在 childCount 内，所以平分时分母是 (childCount - 2) 而不是 (childCount - 1)
            double angle = (180 - totalAngle) / 2.0 + (double) totalAngle / (childCount - 2) * i;
            View child = getChildAt(i);
            MoonMenuItem moonMenuItem = new MoonMenuItem(child, radius, angle, delay * i);
            moonMenuItemList.add(moonMenuItem);
        }


//        Drawable backgroundDrawable = context.getResources().getDrawable(R.drawable.shape_dim);
//        Resources resources = context.getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.shape_dim);
//        BitmapDrawable foregroundBitmapDrawable = new BitmapDrawable(resources, bitmap);
//        setBackground(backgroundDrawable);
        setBackgroundResource(R.drawable.shape_dim);
        Drawable background = getBackground();
        if (background != null) {
            //初始化的时候背景设置为透明，否则为全黑的前景色
            background.setAlpha(0);
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {

        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnMenuSwitchClickListener(OnMenuSwitchClickListener onMenuSwitchClickListener) {
        this.onMenuSwitchClickListener = onMenuSwitchClickListener;
    }


    public interface OnMenuItemClickListener {
        void onMenuItemClick(View view);
    }


    /**
     * 展开的开关
     */
    public interface OnMenuSwitchClickListener {
        void onMenuSwitchClick(View view, boolean isMenuClose);
    }

    //主开关对象
    private class MoonMenuSwitch {
        private View view;
        //主菜单按钮点击播放自身旋转动画
        private RotateAnimation openMenuRotateAnimation;
        private RotateAnimation closeMenuRotateAnimation;


        MoonMenuSwitch(View view) {
            this.view = view;

            openMenuRotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            openMenuRotateAnimation.setDuration(300);
            openMenuRotateAnimation.setFillAfter(true);

            closeMenuRotateAnimation = new RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            closeMenuRotateAnimation.setDuration(300);
            closeMenuRotateAnimation.setFillAfter(true);


            if (view != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isMenuClose = !isMenuClose;
                        moonMenuSwitch.playSwitchChangeAnim(isMenuClose);
                        if (moonMenuItemList != null) {
                            for (MoonMenuItem moonMenuItem : moonMenuItemList) {
                                moonMenuItem.playMenuSwitchChangeAnimation(isMenuClose);
                            }
                        }
                        Drawable background = getBackground();
                        if (background != null) {
                            if (isMenuClose) {
                                background.setAlpha(0);
                            } else {
                                background.setAlpha(127);
                            }
                        }
                        if (onMenuSwitchClickListener != null) {
                            onMenuSwitchClickListener.onMenuSwitchClick(v, isMenuClose);
                        }
                    }
                });
            }
        }


        private void playSwitchChangeAnim(boolean isMenuClose) {
            if (isMenuClose) {
                view.startAnimation(openMenuRotateAnimation);
            } else {
                view.startAnimation(closeMenuRotateAnimation);
            }
        }
    }

    //展开后的菜单对象
    private class MoonMenuItem {
        private View view;
        private double angle;
        //角度转换成弧度
        private double radian;
        private int targetX;
        private int targetY;
        private int startX;
        private int startY;

        private ObjectAnimator openMenuMoveXAnimation;
        private ObjectAnimator openMenuMoveYAnimation;
        private ObjectAnimator openMenuAlphaAnimation;

        private ObjectAnimator closeMenuMoveXAnimation;
        private ObjectAnimator closeMenuMoveYAnimation;
        private ObjectAnimator closeMenuAlphaAnimation;

        private AnimationSet clickAnimationSet;

        private MoonMenuItem(final View view, int radius, double angle, int delay) {
            this.view = view;
            this.angle = angle;
            this.radian = angle / 180.0 * Math.PI;

//            this.startX = getWidth() / 2 - view.getMeasuredWidth() / 2;
            this.startX = centerX - view.getMeasuredWidth() / 2;
//            this.startY = getHeight() - view.getMeasuredHeight();
            this.startY = centerY - view.getMeasuredHeight() / 2;

            this.targetX = startX - (int) (radius * Math.cos(radian));
            this.targetY = startY - (int) (radius * Math.sin(radian));

            int moveDuration = 100;

            view.setClickable(false);
            view.setVisibility(GONE);
            openMenuMoveXAnimation = ObjectAnimator.ofFloat(view, "x", startX, targetX);
            openMenuMoveYAnimation = ObjectAnimator.ofFloat(view, "y", startY, targetY);
            openMenuAlphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 0, 1);

            closeMenuMoveXAnimation = ObjectAnimator.ofFloat(view, "x", targetX, startX);
            closeMenuMoveYAnimation = ObjectAnimator.ofFloat(view, "y", targetY, startY);
            closeMenuAlphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 1, 0);

            List<ObjectAnimator> objectAnimatorList = new ArrayList<>();
            objectAnimatorList.add(openMenuMoveXAnimation);
            objectAnimatorList.add(openMenuMoveYAnimation);
            objectAnimatorList.add(openMenuAlphaAnimation);
            objectAnimatorList.add(closeMenuMoveXAnimation);
            objectAnimatorList.add(closeMenuMoveYAnimation);
            objectAnimatorList.add(closeMenuAlphaAnimation);

            for (ObjectAnimator objectAnimator : objectAnimatorList) {
                objectAnimator.setInterpolator(new DecelerateInterpolator());
                objectAnimator.setDuration(moveDuration);
                objectAnimator.setStartDelay(delay);
            }


            AlphaAnimation clickAlphaAnimation = new AlphaAnimation(1, 0);
            //ScaleAnimation的后两个参数，即缩放中心的xy坐标，所处坐标系原点是这个View本身的左上角(不受属性动画影响，即 onLayout 时的位置)
            ScaleAnimation clickScaleAnimAnimation = new ScaleAnimation(1f, 4f, 1f, 4f, targetX + view.getMeasuredWidth() / 2 - startX, targetY + view.getMeasuredHeight() / 2 - startY);
            clickScaleAnimAnimation.setFillAfter(false);
            clickAlphaAnimation.setFillAfter(false);
            clickAnimationSet = new AnimationSet(true);
            clickAnimationSet.addAnimation(clickAlphaAnimation);
            //未播放完动画就跳转到另外的 Activity 导致返回的时候动画继续播放，暂时不用此放大动画
            clickAnimationSet.addAnimation(clickScaleAnimAnimation);
            clickAnimationSet.setDuration(200);

            initClickListener(view);
        }

        private void initClickListener(final View view) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (moonMenuItemList != null) {
                        //其他的 item 渐变消失
                        for (final MoonMenuItem moonMenuItem : moonMenuItemList) {
                            if (moonMenuItem != MoonMenuItem.this) {
                                ObjectAnimator closeMenuAlphaAnimation = moonMenuItem.getCloseMenuAlphaAnimation();
                                closeMenuAlphaAnimation.start();
                                closeMenuAlphaAnimation.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        moonMenuItem.getView().setVisibility(GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                            }
                        }
                    }
                    playClickAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.setVisibility(GONE);
                            Drawable background = getBackground();
                            if (background != null) {
                                background.setAlpha(0);
                            }
                            if (onMenuItemClickListener != null) {
                                onMenuItemClickListener.onMenuItemClick(v);
                            }
                            //点击菜单项后关闭状态
                            isMenuClose = true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }

            });
        }


        private void playClickAnimation(Animation.AnimationListener animationListener) {
            if (clickAnimationSet != null) {
                view.startAnimation(clickAnimationSet);
                clickAnimationSet.setAnimationListener(animationListener);
            }

        }

        private void playMenuSwitchChangeAnimation(boolean isMenuClose) {
            if (!isMenuClose) {
                openMenuMoveXAnimation.start();
                openMenuMoveYAnimation.start();
                openMenuAlphaAnimation.start();
                view.setVisibility(VISIBLE);
            } else {
                closeMenuMoveXAnimation.start();
                closeMenuMoveYAnimation.start();
                closeMenuAlphaAnimation.start();
            }

        }

        public View getView() {
            return view;
        }

        public ObjectAnimator getCloseMenuAlphaAnimation() {
            return closeMenuAlphaAnimation;
        }
    }
}

