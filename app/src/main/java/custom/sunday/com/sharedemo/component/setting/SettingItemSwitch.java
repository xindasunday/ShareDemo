package custom.sunday.com.sharedemo.component.setting;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import custom.sunday.com.sharedemo.HomeApplication;
import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.common.view.SwitchView;


/**
 * Created by zhongfei.sun on 2018/1/8.
 */

public abstract class SettingItemSwitch extends SettingItem {

    private SwitchView.OnSwitchStatusChangeListener mOnSwitchClickListener;

    public SettingItemSwitch(String title, String summary, String key) {
        mTitle = title;
        mSummary = summary;
        mPrefKey = key;
        mOnSwitchClickListener = new SwitchView.OnSwitchStatusChangeListener() {
            @Override
            public void onSwitchStatusChange(SwitchView switchView, int switchStatus) {
                setState(switchStatus == SwitchView.SWITCH_ON);
            }
        };
    }

    @Override
    public View createView() {
        View view = LayoutInflater.from(HomeApplication.getInstance()).inflate(
                R.layout.item_switch_setting,
                null,
                false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.summary = (TextView) view.findViewById(R.id.summary);
        viewHolder.switchView = (SwitchView) view.findViewById(R.id.switch_view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.title.setText(mTitle);
        if (TextUtils.isEmpty(mSummary)) {
            viewHolder.summary.setVisibility(View.GONE);
        } else {
            viewHolder.summary.setText(mSummary);
        }
        viewHolder.switchView.initSwitchStatus(getState());
        viewHolder.switchView.setOnSwitchStatusChangeListener(mOnSwitchClickListener);
    }

    @Override
    public abstract void refreshData();

    public abstract void syncNetwork();

    public static class ViewHolder {
        TextView title;
        TextView summary;
        SwitchView switchView;
    }
}
