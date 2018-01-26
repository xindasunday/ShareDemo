package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by zhongfei.sun on 2018/1/26.
 */

public class RootParentLayout extends RelativeLayout{
    public RootParentLayout(Context context) {
        super(context);
    }

    public RootParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RootParentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String action = "";
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            action = "ACTION_DOWN";
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            action = "ACTION_UP";
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            action = "ACTION_MOVE";
        }
        Log.e("sunday","RootParentLayout-dispatchTouchEvent -action = " + action);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        String action = "";
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            action = "ACTION_DOWN";
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            action = "ACTION_UP";
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            action = "ACTION_MOVE";
        }
        Log.e("sunday","RootParentLayout-onInterceptTouchEvent-action=" + action);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        String action = "";
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            action = "ACTION_DOWN";
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            action = "ACTION_UP";
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            action = "ACTION_MOVE";
        }
        Log.e("sunday","RootParentLayout-onTouchEvent--action=" + action);
        return super.onTouchEvent(ev);
    }
}
