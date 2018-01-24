package custom.sunday.com.sharedemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import custom.sunday.com.sharedemo.HomeApplication;
import custom.sunday.com.sharedemo.R;

public class MainActivity extends AppCompatActivity {
    public static final String[] TITLE_ARRAY= {
            "SharePreference管理",
            "Path",
            "RefreshLayout下拉刷新"};
    public static final Class[] CLASS_ARRAY = {
            SettingItemActivity.class,
            PathActivity.class,
            RefreshActivity.class
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return TITLE_ARRAY.length;
            }

            @Override
            public Object getItem(int position) {
                return TITLE_ARRAY[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if(view == null){
                    view = LayoutInflater.from(HomeApplication.getInstance()).inflate(R.layout.item_main_list,null,false);
                    Holder holder = new Holder();
                    holder.titleView = (TextView) view.findViewById(R.id.text_view);
                    view.setTag(holder);
                }
                Holder holder = (Holder) view.getTag();
                holder.titleView.setText(TITLE_ARRAY[position]);
                return view;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this,CLASS_ARRAY[position]));
            }
        });
    }


    private static final class Holder{
        TextView titleView;
    }


}
