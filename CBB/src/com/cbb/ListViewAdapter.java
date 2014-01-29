package com.cbb;

import java.util.ArrayList;

import com.cbb.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Klasa adaptera dla niestandardowego ListView, przy uzyciu listy tablicowej.
 * @author lagvna
 *
 */
public class ListViewAdapter extends ArrayAdapter<CustomRow> {
		 /** Kontekst listView */
	    Context context;
	    /** Id szablonu zawartosci */
	    int layoutResourceId;  
	    /** Lista tablicowa  z danymi listView*/
	    ArrayList<CustomRow> data = null;
	 
	    public ListViewAdapter(Context context, int layoutResourceId, ArrayList<CustomRow> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }
	    
	    /**
	     * Metoda sluzaca do poboru danych dla konkretnego rekordu w ListView
	     */
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
	            holder.txtDate = (TextView)row.findViewById(R.id.txtDate);
	 
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (RowHolder)row.getTag();
	        }
	 
	        CustomRow object = data.get(position);
	        holder.txtTitle.setText(object.title);
	        holder.txtDate.setText(object.date);
	        holder.imgIcon.setImageResource(object.icon);
	 
	        return row;
    }
	/**
	 * Klasa przechowujaca dane o polach danego rekordu.
	 * @author lagvna
	 *
	 */
	static class RowHolder	{
	    ImageView imgIcon;
	    TextView txtTitle;
	    TextView txtDate;
	}
}