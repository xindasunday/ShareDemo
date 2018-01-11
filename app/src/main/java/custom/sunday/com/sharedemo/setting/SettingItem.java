package custom.sunday.com.sharedemo.setting;

import android.view.View;

/**
 * Created by zhongfei.sun on 2018/1/10.
 */

public abstract class SettingItem {
    protected String mPrefKey;
    protected String mTitle;
    protected String mSummary;

    public String getKey() {
        return mPrefKey;
    }

    public void setKey(String key) {
        mPrefKey = key;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String text) {
        mSummary = text;
    }

    public abstract View createView();

    public boolean getState() {
        return getBooleanValue();
    }

    public void setState(boolean isOn) {
        setBooleanValue(isOn);
    }

    public abstract void bindView(View view);

    //将服务器拉下来
    public abstract void refreshData();

    //将SP上传服务器
    public abstract void syncNetwork();

    //SP是否有值，如果没有值，需要从服务器拉取
    public boolean isSPUnknow() {
        return SettingKeyValue.isSPUnknow(mPrefKey);
    }

    public boolean getBooleanValue() {
        return SettingKeyValue.getBooleanValue(mPrefKey);
    }

    public void setBooleanValue(boolean value) {
        SettingKeyValue.setBooleanValue(mPrefKey, value);
    }
}
