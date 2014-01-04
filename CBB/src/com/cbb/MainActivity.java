package com.cbb;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		CustomRow CustomRow_data[] = new CustomRow[4];
		
		CustomRow_data[0] = new CustomRow(getIcon("Nagrywaj"), "Nagrywaj");
		CustomRow_data[1] = new CustomRow(getIcon("Przeglądaj nagrania"), "Przeglądaj nagrania");
		CustomRow_data[2] = new CustomRow(getIcon("Ustawienia"), "Ustawienia");
		CustomRow_data[3] = new CustomRow(getIcon("Autorzy"), "Autorzy");
		
		
		ListViewAdapter adapter = new ListViewAdapter(this,
                R.layout.element, CustomRow_data);
 
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
        		int position, long id) {
        		if(position == 0)	{
        			Intent i = new Intent(MainActivity.this, RecActivity.class);
        			MainActivity.this.startActivity(i);
        		} else if(position == 1)	{
        			Intent i = new Intent(MainActivity.this, LibraryActivity.class);
        			MainActivity.this.startActivity(i);
        		} else if(position == 2)	{
        			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        			MainActivity.this.startActivity(i);
        		} else if (position == 3)	{
        			Intent i = new Intent(MainActivity.this, CreditsActivity.class);
        			MainActivity.this.startActivity(i);
        		}
        	}
        });
	}

	private int getIcon(String title)	{
		if(title.equals("Nagrywaj"))	{
			return R.drawable.jeden;
		} else if(title.equals("Przeglądaj nagrania"))	{
			return R.drawable.jeden;
		} else if(title.equals("Ustawienia"))		{
			return R.drawable.jeden;
		} else if(title.equals("Autorzy"))		{
			return R.drawable.jeden;
		}
		
		return R.drawable.ic_launcher;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
