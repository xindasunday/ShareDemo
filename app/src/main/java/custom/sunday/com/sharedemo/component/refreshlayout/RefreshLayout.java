package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by zhongfei.sun on 2017/10/20.
 */

public class RefreshLayout extends LinearLayout {

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float mLastX;
    private float mLastY;

    private int mHeadViewHeight;
    private Scroller mScroller;

    private void init(){
        mScroller = new Scroller(getContext());
    }


    private boolean isChildTop(){
        View child = getChildAt(1);
        if(child instanceof AbsListView){
            AbsListView listView = (AbsListView) child;
            if(listView.getFirstVisiblePosition()==0){
                 View topChildView = listView.getChildAt(0);
                return topChildView.getTop()== 0;
            }
        }
        return false;
    }

    private boolean isChildBottom(){
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View headView = getChildAt(0);
        mHeadViewHeight = headView.getMeasuredHeight();
        Log.e("sunday","headViewHeight = " + mHeadViewHeight );
        scrollTo(0,mHeadViewHeight);
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - mLastY;
                Log.e("sunday","moveY = " + moveY);
                Log.e("sunday","getScrollY() = " + getScrollY());
                if(isChildTop() && moveY > 0) {
                    scrollBy(0, (int) -moveY);
                }else if(moveY < 0 ){
                    if(isChildBottom() || getScrollY() < mHeadViewHeight) {
                        scrollBy(0, (int) -moveY);
                    }
                }
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(isChildTop() || isChildBottom()) {
                    mScroller.startScroll(0, getScrollY(), 0, mHeadViewHeight - getScrollY(), 1000);
                    postInvalidate();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public void scrollTo(int x, int y) {
        if(y > mHeadViewHeight){
            y = mHeadViewHeight;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            Log.e("sunday","mScroller.getCurrY() = " + mScroller.getCurrY() );
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }
    }
}
