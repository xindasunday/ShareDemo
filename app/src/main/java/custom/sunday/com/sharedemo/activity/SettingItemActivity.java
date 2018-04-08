package custom.sunday.com.sharedemo.activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.component.setting.FriendDetail;
import custom.sunday.com.sharedemo.component.setting.SettingItem;
import custom.sunday.com.sharedemo.component.setting.SettingItemScreen;
import custom.sunday.com.sharedemo.component.setting.SettingItemSwitch;
import custom.sunday.com.sharedemo.component.setting.SettingItemText;
import custom.sunday.com.sharedemo.component.setting.SettingKeyValue;
import sunday.com.easyrefreshlayout.ClassicsFootView;
import sunday.com.easyrefreshlayout.FootView;
import sunday.com.easyrefreshlayout.RefreshLayout;
import sunday.com.easyrefreshlayout.RefreshListener;
import sunday.com.easyrefreshlayout.RotateHeaderView;

public class SettingItemActivity extends AppCompatActivity {
    private FriendDetail mFriendDetail;
    private SettingItemText mNameRemarkSettingItem;
    private SettingItemScreen mSettingItemScreen;
    private RefreshLayout mRefreshLayout;
    private View mErrorView;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingitem);
        //demo使用
        mFriendDetail = new FriendDetail();
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        FootView mFootView = new ClassicsFootView(this);
        mRefreshLayout.setFootView(mFootView);

        mRefreshLayout.setHeadView(new RotateHeaderView(this));
        //mErrorView = LayoutInflater.from(this).inflate(R.layout.layout_network_error,null,false);
        mRefreshLayout.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh(true);
                        //mRefreshLayout.showErrorView(mErrorView);
                    }
                },2000);
            }

            @Override
            public void loadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadMore(true);
                    }
                },2000);
            }
        });
        mListView = (ListView) findViewById(R.id.list_view);
        List<SettingItem> settingItems = new ArrayList<>();
        mNameRemarkSettingItem = new SettingItemText(
                "设置备注",
                "随意填入",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingItemActivity.this, "点击响应", Toast.LENGTH_SHORT).show();
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
        mSettingItemScreen = new SettingItemScreen(mListView, settingItems);
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
