package custom.sunday.com.sharedemo.setting;

import android.content.Context;
import android.content.SharedPreferences;

import custom.sunday.com.sharedemo.HomeApplication;


/**
 * Created by zhongfei.sun on 2018/1/10.
 */

public final class SettingKeyValue {
    public static final String SP_NAME = "setting";
    public static final String KEY_VALUE_TRUE = "1";
    public static final String KEY_VALUE_FALSE = "0";
    public static final String KEY_VALUE_UNKNOW = "999";
    public static final String KEY_BOOLEAN_BLACK = "black_friend";
    public static final String KEY_BOOLEAN_SHOW_NUMBER = "phoneNumberVisibleToFriendsSwitch";

    public static final boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = HomeApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "0").equals(KEY_VALUE_TRUE);
    }

    public static final void setBooleanValue(String key, boolean value) {
        SharedPreferences sharedPreferences = HomeApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value ? KEY_VALUE_TRUE : KEY_VALUE_FALSE).apply();
    }

    public static final boolean isSPUnknow(String key) {
        SharedPreferences sharedPreferences = HomeApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, KEY_VALUE_UNKNOW).equals(KEY_VALUE_UNKNOW);
    }
}
