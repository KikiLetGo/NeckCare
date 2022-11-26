package com.elexlab.neckcare.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elexlab.neckcare.R;
import com.elexlab.neckcare.datasource.models.App;

import java.util.List;

public class WriteListAdapter extends BaseAdapter {
    private Context context;
    private List<App> writeApps;

    public WriteListAdapter(Context context, List<App> writeApps) {
        this.context = context;
        this.writeApps = writeApps;
    }

    @Override
    public int getCount() {
        int count =  writeApps == null?0:writeApps.size();
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_writelist,null);
            ImageView ivAppIcon = convertView.findViewById(R.id.ivAppIcon);
            TextView tvAppName = (TextView) convertView.findViewById(R.id.tvAppName);
            viewHolder.ivAppIcon= ivAppIcon;
            viewHolder.tvAppName= tvAppName;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        App app = writeApps.get(position);
        viewHolder.tvAppName.setText(app.getName());
        viewHolder.ivAppIcon.setImageDrawable(app.getIcon());
        return convertView;
    }


    private class ViewHolder{
        public ImageView ivAppIcon;
        public TextView tvAppName;
    }
}
