package com.cbb;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryActivity extends ListActivity {

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  setContentView(R.layout.activity_library);
		  
		  CustomRow CustomRow_data[] = new CustomRow[5];
          
          CustomRow_data[0] = new CustomRow(R.drawable.jeden, "cbb4", "23-05-2003");
          CustomRow_data[1] = new CustomRow(R.drawable.jeden, "cbb5", "31-07-2012");
          CustomRow_data[2] = new CustomRow(R.drawable.jeden, "cbb6", "13-03-2013");
          CustomRow_data[3] = new CustomRow(R.drawable.jeden, "cbb7", "2-01-2014");
          CustomRow_data[4] = new CustomRow(R.drawable.jeden, "cbb8", "1-08-1990");
          
          
          ListViewAdapter adapter = new ListViewAdapter(this,
          R.layout.element, CustomRow_data);

          setListAdapter(adapter);
          ListView lv = getListView();
          lv.setTextFilterEnabled(true);
  
          lv.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
                  if(position == 0)        {
                          Intent i = new Intent(LibraryActivity.this, WatchingActivity.class);
                          LibraryActivity.this.startActivity(i);
                  }
        	  }
          });
	  }
}