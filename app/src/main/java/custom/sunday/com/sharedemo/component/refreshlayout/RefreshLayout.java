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
import android.widget.Scroller;

/**
 * Created by zhongfei.sun on 2017/10/20.
 */

public class RefreshLayout extends LinearLayout {

    private HeaderView mHeaderView;
    private float mLastX;
    private float mLastY;
    private int mHeadViewHeight;
    private int mRootViewHeight;
    private Scroller mScroller;
    private RefreshListener mRefreshListener;
    private int maxPullHeight;
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
        return false;
    }

    public boolean isRefreshStatus() {
        return isChildTop();
    }

    public boolean isLoadMoreStatus() {
        return isChildBottom();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View headView = getChildAt(0);
        mHeadViewHeight = headView.getMeasuredHeight();
        maxPullHeight = mHeadViewHeight;
        scrollTo(0, mHeadViewHeight);
    }

    public void setMaxPullHeight(int height) {
        if (height > mHeadViewHeight) {
            maxPullHeight = height;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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
                    mHeaderView.begin();
                    return true;
                } else if (moveY < 0) {
                    if (isChildBottom() || getScrollY() < mHeadViewHeight) {
                        //scrollBy(0, (int) -moveY);
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
                if (isChildTop() && moveY > 0) {
                    float move = getScrollValue(moveY);
                    mHeaderView.progress(0.5f);
                    scrollBy(0, (int) -move);
                } else if (moveY < 0) {
                    if (isChildBottom() || getScrollY() < mHeadViewHeight) {
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
                            mScroller.startScroll(0, getScrollY(), 0, mHeadViewHeight, 500);
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
                        mRefreshListener.loadMore();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y > mHeadViewHeight) {
            y = mHeadViewHeight;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && !mScroller.isFinished()) {
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
                    (isRefreshStatus() ? mHeadViewHeight : mRootViewHeight),
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
