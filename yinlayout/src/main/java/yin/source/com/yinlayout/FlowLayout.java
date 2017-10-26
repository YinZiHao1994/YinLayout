package yin.source.com.yinlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签布局
 * Created by Yin on 2017/10/25.
 */
public class FlowLayout extends ViewGroup {
    private List<Integer> childViewNumOfEachRow = new ArrayList<>();
    private List<Integer> heightOfEachRow = new ArrayList<>();
    private boolean isFirstLayout = true;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*onMeasure()方法会执行多次，每次执行先清空List防止多次叠加出错*/
        childViewNumOfEachRow.clear();
        heightOfEachRow.clear();

        /*默认的sizeWidth和sizeHeight的大小以match_parent计算，如果实际设置为wrap_content
        则会在setMeasuredDimension()后设置*/
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//        如果ViewGroup设置为wrap_content，则设置实际宽高为我们计算的width和height
        int width = 0;
        int height = 0;
        //已经用掉的行宽
        int hasUsedLineWidth = 0;
        //已经用掉的行高
        int hasUsedLineHeight = 0;
        int count = getChildCount();
//        每一行的子View数目
        int childNumOfLine = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams layoutParams = child.getLayoutParams();
            int leftMargin = 0;
            int rightMargin = 0;
            int topMargin = 0;
            int bottomMargin = 0;
            if (layoutParams instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                leftMargin = marginLayoutParams.leftMargin;
                rightMargin = marginLayoutParams.rightMargin;
                topMargin = marginLayoutParams.topMargin;
                bottomMargin = marginLayoutParams.bottomMargin;
            }
//            child所占的宽高需要考虑它的margin
            int childWidth = child.getMeasuredWidth() + leftMargin + rightMargin;
            int childHeight = child.getMeasuredHeight() + topMargin + bottomMargin;

//            如果超过了最大宽度(match_parent所得)
            if (hasUsedLineWidth + childWidth > sizeWidth) {
                heightOfEachRow.add(hasUsedLineHeight);
                width = Math.max(hasUsedLineWidth, width);
                height += hasUsedLineHeight;
                //此时已经另起一行了，所以新的行高就是这个导致新起一行的view的高度，行宽同理
                hasUsedLineHeight = childHeight;
                hasUsedLineWidth = childWidth;
                childViewNumOfEachRow.add(childNumOfLine);
                childNumOfLine = 1;
            } else {
                hasUsedLineWidth += childWidth;
                hasUsedLineHeight = Math.max(hasUsedLineHeight, childHeight);
                childNumOfLine++;
            }
            //前面每一次结算的时候都是结算了上一行的宽高，当到达最后一个view时，结算自己这一行（最后一行）的宽高
            if (i == count - 1) {
                width = Math.max(hasUsedLineWidth, width);
                height += hasUsedLineHeight;
                childViewNumOfEachRow.add(childNumOfLine);
                heightOfEachRow.add(hasUsedLineHeight);
            }
        }

//        根据测量模式决定是否使用我们测得的宽高
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        /*避免多次执行*/
        if (isFirstLayout) {
            int left = 0;
            int top = 0;
            int childCursor = 0;
            for (int i = 0; i < childViewNumOfEachRow.size(); i++) {

                for (int j = 0; j < childViewNumOfEachRow.get(i); j++) {
                    View child = getChildAt(childCursor);
                    int leftMargin = 0;
                    int topMargin = 0;
                    int rightMargin = 0;
                    LayoutParams layoutParams = child.getLayoutParams();
                    if (layoutParams instanceof MarginLayoutParams) {
                        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                        leftMargin = marginLayoutParams.leftMargin;
                        topMargin = marginLayoutParams.topMargin;
                        rightMargin = marginLayoutParams.rightMargin;
                    }

                    int lc = leftMargin + left;
                    int tc = topMargin + top;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = tc + child.getMeasuredHeight();
                    left += leftMargin + rightMargin + child.getMeasuredWidth();
                    childCursor++;
                    child.layout(lc, tc, rc, bc);
                }
                left = 0;
                top += heightOfEachRow.get(i);
            }
            isFirstLayout = false;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}