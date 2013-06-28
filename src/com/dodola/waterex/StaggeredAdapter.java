package com.dodola.waterex;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodola.model.DuitangInfo;
import com.dodola.waterex.R;
import com.example.android.bitmapfun.util.ImageFetcher;

public class StaggeredAdapter extends BaseAdapter {
    private LinkedList<DuitangInfo> mInfos;
    ImageFetcher mImageFetcher;

    public StaggeredAdapter(Context context, ImageFetcher f) {
        mInfos = new LinkedList<DuitangInfo>();
        mImageFetcher = f;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        DuitangInfo duitangInfo = mInfos.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.infos_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.    getTag();

        
//        float iHeight = ((float) 200 / 183 * duitangInfo.getHeight());
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) duitangInfo.getHeight()));

        holder.contentView.setText(duitangInfo.getMsg());
        mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView contentView;
        TextView timeView;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void addItemLast(List<DuitangInfo> datas) {
        mInfos.addAll(datas);
    }

    public void addItemTop(List<DuitangInfo> datas) {
        for (DuitangInfo info : datas) {
            mInfos.addFirst(info);
        }
    }
}
