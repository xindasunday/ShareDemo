package custom.sunday.com.sharedemo.component.refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import custom.sunday.com.sharedemo.R;

/**
 * Created by zhongfei.sun on 2018/1/19.
 */

public class ClassicsHeaderView implements HeaderView {

    private Context mContext;
    private View mParent;
    private TextView mTextView;

    public ClassicsHeaderView(Context context){
        mContext = context;
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void progress(float progress) {

    }

    @Override
    public void loading() {

    }

    @Override
    public void normal() {

    }

    @Override
    public View getView() {
        if(mParent == null){
            mParent = LayoutInflater.from(mContext).inflate(R.layout.layout_head_classics,null,false);
            mTextView = (TextView) mParent.findViewById(R.id.text);
        }
        return mParent;
    }
}
