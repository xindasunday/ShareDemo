package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by zhongfei.sun on 2018/1/26.
 */

public class TestRelativeLayout extends RelativeLayout {
    public TestRelativeLayout(Context context) {
        super(context);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        }else if(ev.getAction() == MotionEvent.ACTION_CANCEL){
            action = "ACTION_Cancel";
        }
        Log.e("sunday","TestRelativeLayout-dispatchTouchEvent -action = " + action);
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
        }else if(ev.getAction() == MotionEvent.ACTION_CANCEL){
            action = "ACTION_Cancel";
        }
        Log.e("sunday","TestRelativeLayout-onInterceptTouchEvent-action=" + action);
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
        Log.e("sunday","TestRelativeLayout-onTouchEvent--action=" + action);
        return super.onTouchEvent(ev);
    }
}
