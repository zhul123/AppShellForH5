package com.capinfo.fysystem.views.popwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.capinfo.fysystem.R;

import java.util.List;

public class PopWinForList extends PopWindowMenu implements PopWindowMenu.OnLayoutCompletedListener {
    private ListView mListView;
    private Context mContext;

    public PopWinForList(Context context) {
        super(context, R.layout.common_pop_list);
        this.mContext = context;
        setOnLayoutCompletedLsn(this);
    }

    @Override
    public void onBindView(View v) {
        mListView = v.findViewById(R.id.lv_list);
    }

    public void setDatas(List<String> datas) {
        if(mListView != null) {
            mListView.setAdapter(new PopAdapter(datas, mContext));
        }
    }

    public void setOnItemClick(AdapterView.OnItemClickListener itemClickListener){
        if(mListView != null){
            mListView.setOnItemClickListener(itemClickListener);
        }
    }

    class PopAdapter extends BaseAdapter{
        private List<String> datas;
        private Context mContext;
        public PopAdapter(List datas,Context context){
            this.datas = datas;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 :datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mtextView;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_list, null);
                mtextView = convertView.findViewById(R.id.tv_item);
                convertView.setTag(mtextView);
            }else{
                mtextView = (TextView) convertView.getTag();
            }
            mtextView.setText(datas.get(position));
            return convertView;
        }
    }
}
