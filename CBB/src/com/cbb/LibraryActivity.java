package com.cbb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class LibraryActivity extends ListActivity {
	  ArrayList<CustomRow> CustomRow_data;
	  ListViewAdapter adapter;
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  setContentView(R.layout.activity_library);
		  
		  CustomRow_data = new ArrayList<CustomRow>();
		  
		  createList();
		  
          adapter = new ListViewAdapter(this,
          R.layout.element, CustomRow_data);

          setListAdapter(adapter);
          ListView lv = getListView();
          lv.setTextFilterEnabled(true);
          registerForContextMenu(getListView());
          
          lv.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view,
                  int position, long id) {
                  Intent i = new Intent(LibraryActivity.this, WatchingActivity.class);
                  i.putExtra("title", CustomRow_data.get(position).title);
                  LibraryActivity.this.startActivity(i);
        	  }
          });
	  }
	  
	  private void createList()	{
		  File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/");
		  String[] names = f.list();
		  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		  CustomRow_data.clear();
          
		  for(int i = 0; i < names.length; i++)	{
			  if(names[i].startsWith("cbb-"))	{
				  File tmp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+names[i]);
				  String date = sdf.format(tmp.lastModified());
				  String title = names[i];
				  CustomRow_data.add(new CustomRow(R.drawable.jeden, title, date));
			  }
		  }
	  }
	  
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	          ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      AdapterView.AdapterContextMenuInfo info;
	      try {
	          info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	      } catch (ClassCastException e) {
	          Log.e("cbb", "bad menuInfo", e);
	          return;
	      }
	      
	      menu.setHeaderTitle("Menu");  
	      menu.add(0, v.getId(), 0, "Usuń");  
	  }

	  @Override
	  public boolean onMenuItemSelected(int featureId, MenuItem item) {
	      AdapterView.AdapterContextMenuInfo info;
	      try {
	          info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	      } catch (ClassCastException e) {
	          Log.e("cbb", "bad menuInfo", e);
	          return false;
	      }
	      
	      String toDel = CustomRow_data.get(info.position).title;

	      if(item.getTitle() == "Usuń"){
	    	  delete(toDel);
	      }  

		return false;
	  }
	  
	  public void delete(String d){
		  File tmp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+d);
		  tmp.delete();
		  adapter.notifyDataSetChanged();
		  createList();
		  
	        Toast.makeText(this, "Usunięto!", Toast.LENGTH_SHORT).show();  
	    }  
}