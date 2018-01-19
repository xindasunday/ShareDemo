package custom.sunday.com.sharedemo.component.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import custom.sunday.com.sharedemo.HomeApplication;
import custom.sunday.com.sharedemo.R;


/**
 * Created by zhongfei.sun on 2018/1/5.
 */

public class SettingItemText extends SettingItem {

    private View.OnClickListener mOnClickListener;

    public SettingItemText(String title, String summary, View.OnClickListener onClickListener) {
        this.mTitle = title;
        this.mSummary = summary;
        mOnClickListener = onClickListener;
    }

    //用于ListView 创建View
    @Override
    public View createView() {
        View view = LayoutInflater.from(HomeApplication.getInstance()).inflate(
                R.layout.item_text_setting,
                null,
                false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.summary = (TextView) view.findViewById(R.id.summary);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public boolean getState() {
        return false;
    }

    @Override
    public void setState(boolean isOn) {

    }

    @Override
    public void bindView(View view) {
        view.setClickable(true);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.title.setText(mTitle);
        viewHolder.summary.setText(mSummary);
        if (mOnClickListener != null) {
            view.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public void refreshData() {
        //需要覆写
    }

    @Override
    public void syncNetwork() {

    }

    public static class ViewHolder {
        TextView title;
        TextView summary;
    }

}
