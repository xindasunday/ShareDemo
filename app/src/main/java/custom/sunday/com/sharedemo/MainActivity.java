package custom.sunday.com.sharedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import custom.sunday.com.sharedemo.setting.SettingItem;
import custom.sunday.com.sharedemo.setting.SettingItemScreen;
import custom.sunday.com.sharedemo.setting.SettingItemSwitch;
import custom.sunday.com.sharedemo.setting.SettingItemText;
import custom.sunday.com.sharedemo.setting.SettingKeyValue;

public class MainActivity extends AppCompatActivity {
    private FriendDetail mFriendDetail;
    private SettingItemText mNameRemarkSettingItem;
    private SettingItemScreen mSettingItemScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //demo使用
        mFriendDetail = new FriendDetail();
        ListView listView = (ListView) findViewById(R.id.list_view);
        List<SettingItem> settingItems = new ArrayList<>();
        mNameRemarkSettingItem = new SettingItemText(
                "设置备注",
                "随意填入",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击响应", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public void refreshData() {
                //如果当前summary需要更新，调用
                mSettingItemScreen.refreshView();
            }
        };
        settingItems.add(mNameRemarkSettingItem);
        settingItems.add(new SettingItemSwitch(
                getResources().getString(R.string.friend_detail_black_friend_title),
                getResources().getString(R.string.friend_detail_black_friend_text),
                SettingKeyValue.KEY_BOOLEAN_BLACK
        ) {
            @Override
            public void refreshData() {
                if (isSPUnknow() && mFriendDetail != null) {
                    setState(mFriendDetail.isBlackFriend());
                    mSettingItemScreen.refreshView();
                }
            }

            @Override
            public void syncNetwork() {
                boolean isBlack = getState();
                if (mFriendDetail != null && mFriendDetail.isBlackFriend() != isBlack) {
                    //将isBlack同步到网络
                }
            }
        });
        settingItems.add(new SettingItemSwitch(
                getResources().getString(R.string.friend_detail_see_phone),
                null,
                SettingKeyValue.KEY_BOOLEAN_SHOW_NUMBER
        ) {

            @Override
            public void refreshData() {
                if (mFriendDetail != null && isSPUnknow()) {
                    setState(mFriendDetail.isPhoneVisible());
                    mSettingItemScreen.refreshView();
                }
            }

            @Override
            public void syncNetwork() {
                boolean isVisible = getState();
                if (mFriendDetail != null && mFriendDetail.isPhoneVisible() != isVisible) {
                    //将isVisible同步到网络
                }
            }
        });
        mSettingItemScreen = new SettingItemScreen(listView, settingItems);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingItemScreen.refresh();
    }

    @Override
    protected void onDestroy() {
        mSettingItemScreen.sync();
        super.onDestroy();
    }

}
