package custom.sunday.com.sharedemo.component.refreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.base.BaseActivity;

/**
 * Created by zhongfei.sun on 2018/1/24.
 */

public class RefreshActivity extends BaseActivity{
    private RefreshLayout mRefreshLayout;
    private ListView mListView;
    public static final String[] OP = {
            "RotateHeaderView,下拉看效果",
            "OnlyLoadingHeaderView,下拉看效果"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_layout);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh(true);
                    }
                },2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadMore(true);
                    }
                },2000);
            }
        });
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mRefreshLayout.setHeadView(new RotateHeaderView(getBaseContext()));
                        break;
                    case 1:
                        mRefreshLayout.setHeadView(new OnlyLoadingHeaderView(getBaseContext()));
                        break;
                }
                Toast.makeText(getBaseContext(),"修改成功",Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return OP.length;
            }

            @Override
            public Object getItem(int position) {
                return OP[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if(view == null){
                    view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_text_setting,null,false);
                    Holder holder = new Holder();
                    holder.textView = (TextView) view.findViewById(R.id.title);
                    view.setTag(holder);
                }
                Holder holder = (Holder) view.getTag();
                holder.textView.setText(OP[position]);
                return view;
            }
        });
    }

    public static class Holder{
        TextView textView;
    }
}
