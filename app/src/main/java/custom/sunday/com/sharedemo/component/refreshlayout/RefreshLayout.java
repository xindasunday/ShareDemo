package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by zhongfei.sun on 2017/10/20.
 */

public class RefreshLayout extends LinearLayout {

    private HeaderView mHeaderView;
    private FootView mFootView;
    private float mLastX;
    private float mLastY;
    private int mHeadViewHeight;
    private int mFootViewHeight;
    private Scroller mScroller;
    private RefreshListener mRefreshListener;
    private int maxPullHeight;
    private int maxPushHeight;
    private boolean isFullPull = true;
    //值越大，滑动速度越慢
    private float mFiller = 3;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mHeaderView = new ClassicsHeaderView(getContext());
        addView(mHeaderView.getView(), 0);
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void setHeadView(HeaderView headerView) {
        mHeaderView = headerView;
        View headView = getChildAt(0);
        if (headerView != null && headerView instanceof HeaderView) {
            removeView(headView);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        this.mHeaderView.getView().setLayoutParams(layoutParams);
        if (this.mHeaderView.getView().getParent() != null) {
            ((ViewGroup) this.mHeaderView.getView().getParent()).removeAllViews();
        }
        addView(mHeaderView.getView(), 0);
    }

    public void setFootView(FootView footView){
        mFootView = footView;
        int index = getChildCount() -1;
        View child = getChildAt(index);
        if (footView != null && child instanceof FootView) {
            removeView(child);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        mFootView.getView().setLayoutParams(layoutParams);
        if (mFootView.getView().getParent() != null) {
            ((ViewGroup) this.mFootView.getView().getParent()).removeAllViews();
        }
        addView(mFootView.getView());
    }

    private boolean isChildTop() {
        View child = getChildAt(1);
        if (child instanceof AbsListView) {
            AbsListView listView = (AbsListView) child;
            if (listView.getFirstVisiblePosition() == 0) {
                View topChildView = listView.getChildAt(0);
                return topChildView.getTop() == 0;
            }
        }
        return false;
    }

    private boolean isChildBottom() {
        return true;
    }

    private boolean isRefresh;
    private boolean isLoadMore;
    public boolean isRefreshStatus() {
        return isRefresh;
    }

    public boolean isLoadMoreStatus() {
        return isLoadMore;
    }

    public void setRefreshStatus(boolean status) {
        isRefresh = status;
        isLoadMore = !status;
        mHeaderView.getView().setVisibility(VISIBLE);
        mFootView.getView().setVisibility(GONE);
    }

    public void setLoadMoreStatus(boolean status) {
        isLoadMore = status;
        isRefresh = !status;
        mFootView.getView().setVisibility(VISIBLE);
        mHeaderView.getView().setVisibility(GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View headView = getChildAt(0);
        if(mHeaderView != null && mHeaderView.getView() == headView) {
            mHeadViewHeight = headView.getMeasuredHeight();
            scrollTo(0, mHeadViewHeight);
        }else{
            //throw new RuntimeException("headView is error");
        }

        View footView = getChildAt(getChildCount() - 1);
        if(mFootView != null && mFootView.getView() == footView){
            mFootViewHeight = footView.getMeasuredHeight();
        }else{
            //throw new RuntimeException("footView is error");
        }

    }

    public void setMaxPullHeight(int height) {
        if (height > mHeadViewHeight) {
            maxPullHeight = height;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mTotalWidth = 0;
        int mTotalHeight = 0;

        final int count = getChildCount();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        boolean matchWidth = false;

        // See how tall everyone is. Also remember max width.
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int usedHeight = mTotalHeight;
            measureChildBeforeLayout(child, i, widthMeasureSpec, 0,
                    heightMeasureSpec, 0);

            final int childHeight = child.getMeasuredHeight();
            final int totalLength = mTotalHeight;
            mTotalHeight = totalLength + childHeight + lp.topMargin +
                    lp.bottomMargin;

            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            mTotalWidth = Math.max(mTotalWidth, measuredWidth);

        }
        mTotalHeight += getPaddingTop() + getPaddingBottom();
        mTotalWidth += getPaddingLeft() + getPaddingRight();
        setMeasuredDimension(mTotalWidth, mTotalHeight);
    }

    void measureChildBeforeLayout(View child, int childIndex,
                                  int widthMeasureSpec, int totalWidth, int heightMeasureSpec,
                                  int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth,
                heightMeasureSpec, totalHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int top = getPaddingTop();
        int bottom = getPaddingBottom();
        for(int i = 0;i < count;i++){
            View view = getChildAt(i);
            right = getPaddingRight() + view.getMeasuredWidth();
            top = top + view.getPaddingTop();
            bottom = top + view.getMeasuredHeight();
            view.layout(left,top,right,bottom);
            top = top + view.getMeasuredHeight() + view.getPaddingBottom();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - mLastY;
                if (isChildTop() && moveY > 0) {
                    //scrollBy(0, (int) -moveY);
                    setRefreshStatus(true);
                    mHeaderView.begin();
                    return true;
                } else if (moveY < 0) {
                    if (isChildBottom() || getScrollY() < mHeadViewHeight) {
                        //scrollBy(0, (int) -moveY);
                        setLoadMoreStatus(true);
                        mFootView.begin();
                        return true;
                    }
                }

        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastX = event.getX();
                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - mLastY;
                //Log.e("sunday","onTouchEvent-moveY = " + moveY);
                if (isChildTop() && moveY > 0) {
                    float move = getScrollValue(moveY);
                    mHeaderView.progress(0.5f);
                    scrollBy(0, (int) -move);
                } else if (moveY < 0) {
                    if (isChildBottom() || getScrollY() < mHeadViewHeight) {
                        Log.e("sunday","scrollBy-  = " + -moveY);
                        scrollBy(0, (int) -moveY);
                    }
                }
                mLastY = event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                if (isRefreshStatus()) {
                    if (mRefreshListener != null) {
                        if (getScrollY() > 0) {
                            mHeaderView.reset();
                            mScroller.startScroll(0, getScrollY(), 0, mHeadViewHeight - getScrollY(), 500);
                            postInvalidate();
                        } else {
                            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 500);
                            postInvalidate();
                            mHeaderView.loading();
                            mRefreshListener.refresh();
                        }
                    }
                } else if (isLoadMoreStatus()) {
                    if (mRefreshListener != null) {
                        if (getScrollY() >  mHeadViewHeight + mFootViewHeight) {
                            mFootView.loading();
                            mRefreshListener.loadMore();
                            mScroller.startScroll(0, getScrollY(), 0, mFootViewHeight * 2 - getScrollY(), 500);
                            postInvalidate();
                        } else {
                            mScroller.startScroll(0, getScrollY(), 0, mHeadViewHeight - getScrollY(), 500);
                            postInvalidate();

                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(int x, int y) {
//        if (y > mHeadViewHeight) {
//            y = mHeadViewHeight;
//        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && !mScroller.isFinished()) {
            Log.e("sunday","mScroller.getCurrY() = " + mScroller.getCurrY());
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }

    }
    private Runnable finishRunnable =new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
    public void finishRefresh(boolean success) {
        if(getScrollY() >= 0) {
            removeCallbacks(finishRunnable);
            mHeaderView.showPause(success);
            postDelayed(finishRunnable, mHeaderView.getPauseMillTime());
        }
    }

    public void finish(){
        if (getScrollY() >= 0) {
            mScroller.startScroll(
                    0,
                    getScrollY(),
                    0,
                    (isRefreshStatus() ? mHeadViewHeight : mFootViewHeight),
                    1000);
            postInvalidate();
        }
    }

    public boolean isSupportFullPull() {
        return isFullPull;
    }

    private void setFullPull(boolean isFullPull) {
        this.isFullPull = isFullPull;
    }

    public float getMoveFiller() {
        return mFiller;
    }

    public void setMoveFiller(float num) {
        mFiller = num;
    }

    private float getScrollValue(float moveY) {
        //刷新
        if (isSupportFullPull()) {
            return moveY / mFiller;
        } else {
            if (getScrollY() > 0 || getScrollY() > (mHeadViewHeight - maxPullHeight)) {
                return moveY / mFiller;
            } else {
                return 0;
            }
        }
    }

}
