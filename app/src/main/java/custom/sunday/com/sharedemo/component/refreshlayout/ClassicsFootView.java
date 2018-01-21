package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.common.view.LoadingView;

/**
 * Created by Administrator on 2018/1/21.
 */

public class ClassicsFootView implements FootView {
    private Context mContext;
    private View mParent;

    private LoadingView mLoadingView;
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

    }

    @Override
    public void loading() {
        mTextView.setText("刷新中");
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void reset() {
        mLoadingView.setVisibility(View.GONE);
        mFinishView.setVisibility(View.GONE);
        mTextView.setText("下拉刷新");
    }

    @Override
    public View getView() {
        if(mParent == null){
            mParent = LayoutInflater.from(mContext).inflate(R.layout.layout_head_classics,null,false);
            mTextView = (TextView) mParent.findViewById(R.id.text);
            mLoadingView = (LoadingView) mParent.findViewById(R.id.loading);
            mFinishView = (ImageView) mParent.findViewById(R.id.finish);
        }
        return mParent;
    }

    @Override
    public void showPause(boolean success) {
        mFinishView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mTextView.setText("刷新成功");
    }

    @Override
    public boolean isPauseTime() {
        return mFinishView.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getPauseMillTime() {
        return 2000;
    }
}
