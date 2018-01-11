package custom.sunday.com.sharedemo.setting;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongfei.sun on 2018/1/5.
 */

public class SettingItemScreen{
    private ListView mListView;
    private List<SettingItem> mItemList;
    private BaseAdapter mListAdapter;
    private Map<String,Integer> mTypeMap;
    public SettingItemScreen(@NotNull ListView listView, @NotNull List<SettingItem> itemList){
        mItemList = itemList;
        createType(mItemList);
        mListView = listView;
        mListAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mItemList.size();
            }


            @Override
            public int getItemViewType(int position) {
                String type = getItem(position).getClass().toString();
                return mTypeMap.get(type);
            }

            @Override
            public int getViewTypeCount() {
                return mTypeMap.size();
            }

            @Override
            public Object getItem(int position) {
                return mItemList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                SettingItem settingItem = mItemList.get(position);
                if(view == null){
                    view = settingItem.createView();
                }
                settingItem.bindView(view);
                return view;
            }
        };
        mListView.setAdapter(mListAdapter);
    }

    private void createType(List<SettingItem> mItemList){
        int i = 0;
        mTypeMap = new HashMap<>();
        for(SettingItem settingItem : mItemList){
            String type = settingItem.getClass().toString();
            if(mTypeMap.get(type) == null){
                mTypeMap.put(type,i++);
            }
        }
    }

    public void refresh(){
        int size = mItemList.size();
        for(int i = 0 ; i < size ; i++ ){
            SettingItem settingItem = mItemList.get(i);
            settingItem.refreshData();
        }
    }

    public void sync(){
        int size = mItemList.size();
        for(int i = 0 ; i < size ; i++ ){
            SettingItem settingItem = mItemList.get(i);
            settingItem.syncNetwork();
        }
    }

    public void refreshView(){
        mListAdapter.notifyDataSetChanged();
    }

}
