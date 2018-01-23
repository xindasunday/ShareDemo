package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import custom.sunday.com.sharedemo.R;


/**
 * Created by Administrator on 2018/1/21.
 */

public class ClassicsFootView implements FootView {
    private Context mContext;
    private View mParent;

    private TextView mTextView;
    private ImageView mFinishView;

    public ClassicsFootView(Context context){
        mContext = context;
    }

    @Override
    public void begin() {
        reset();
    }

    @Override
    public void progress(float progress) {
        if(progress >= 1f){
            mTextView.setText("松开加载更多");
        }else{
            mTextView.setText("上拉加载更多");
        }
    }

    @Override
    public void loading() {
        mTextView.setText("加载中");
    }

    @Override
    public void reset() {
        mFinishView.setVisibility(View.GONE);
        mTextView.setText("加载更多");
    }

    @Override
    public View getView() {
        if(mParent == null){
            mParent = LayoutInflater.from(mContext).inflate(R.layout.layout_head_classics,null,false);
            mTextView = (TextView) mParent.findViewById(R.id.text);
            mFinishView = (ImageView) mParent.findViewById(R.id.finish);
        }
        return mParent;
    }

    @Override
    public void showPause(boolean success) {
        mFinishView.setVisibility(View.VISIBLE);
        mTextView.setText("加载成功");
    }

    @Override
    public boolean isPauseTime() {
        return mFinishView.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getPauseMillTime() {
        return 0;
    }
}
