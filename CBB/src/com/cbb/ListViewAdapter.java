package com.cbb;

import com.cbb.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<CustomRow> {
		 
	    Context context;
	    int layoutResourceId;  
	    CustomRow data[] = null;
	 
	    public ListViewAdapter(Context context, int layoutResourceId, CustomRow[] data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        RowHolder holder = null;
	 
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	 
	            holder = new RowHolder();
	            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
	            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
	 
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (RowHolder)row.getTag();
	        }
	 
	        CustomRow object = data[position];
	        holder.txtTitle.setText(object.title);
	        holder.imgIcon.setImageResource(object.icon);
	 
	        return row;
    }
	 
	static class RowHolder	{
	    ImageView imgIcon;
	    TextView txtTitle;
	}
}