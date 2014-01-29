package com.cbb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
/**
 * Klasa sluzaca utworzeniu niestandardowego widoku listowego, skad mozna wybrac
 * interesujacy nas film do obejrzenia.
 * Ponadto klasa umozliwia usuwanie filmow na zasadzie wywolania menu
 * po dluzszym kliknieciu w rekord.
 * 
 * @author lagvna
 *
 */
@SuppressLint("SimpleDateFormat")
public class LibraryActivity extends ListActivity {
	private Point p;
	/** Lista customowych rekordow ListView */
	  ArrayList<CustomRow> CustomRow_data;
	  /** Adapter dla ListView*/
	  ListViewAdapter adapter;
	  @Override
	  /**
	   * Glowny konstruktor aktywnosci.
	   * Tworzy liste filmow, wraz z customowym ListView.
	   * Zapelnia je danymi.
	   * 
	   */
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
	  
	  /**
	   * Metoda zapelniajaca listView filmami z pamieci urzadzenia,
	   * nagranymi przy uzyciu aplikacji.
	   */
	  private void createList()	{
		  File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/");
		  String[] names = f.list();
		  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		  CustomRow_data.clear();
          
		  for(int i = 0; i < names.length; i++)	{
			  if(names[i].startsWith("cbb"))	{
				  File tmp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+names[i]);
				  String date = sdf.format(tmp.lastModified());
				  String title = names[i];
				  CustomRow_data.add(new CustomRow(R.drawable.jeden, title, date));
			  }
		  }
	  }
	  
	  @Override
	  /**
	   * Metoda tworzaca menu, pozwalajace po dluzszym kliknieciu w rekord
	   * na usuniecie nagrania.
	   */
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
	  
	  /**
	   * Metoda wywolujaca aktywnosci dla aktualnie wybranego nagrania.
	   */
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
	  
	  /**
	   * Metoda usuwajaca film po stringu reprezentujacym jego nazwe.
	   * Po wywolaniu odswieza ListView, aby usunac rekord z listy.
	   * @param d nazwa pliku
	   */
	  public void delete(String d){
		  File tmp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+d);
		  tmp.delete();
		  adapter.notifyDataSetChanged();
		  createList();
		  
	        Toast.makeText(this, "Usunięto!", Toast.LENGTH_SHORT).show();  
	    }  

		/**
		 * Metoda wywo�ywana po nacisinieciu przycisku pomocy
		 * @param view aktualny widok
		 */
		public void clickHelp(View view){
			if(p != null){
				showPopup(LibraryActivity.this, p);
			}
		}
		
		/**
		 * Nadpisana metoda wywolywana przy zmianie focusa w oknie
		 */
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
		 
		   int[] location = new int[2];
		   ImageButton button = (ImageButton) findViewById(R.id.imageHP);
		 
		   // Get the x, y location and store it in the location[] array
		   // location[0] = x, location[1] = y.
		   button.getLocationOnScreen(location);
		 
		   //Initialize the Point with x, and y positions
		   p = new Point();
		   p.x = location[0];
		   p.y = location[1];
		}
		
		/**
		 * Metoda do pokazywania dymku z powiadomieniem
		 * @param context aktualne aktivity
		 * @param p punkt odniesienia od tego punktu bedzie liczone polozenie dymka
		 */
		private void showPopup(final Activity context, Point p) {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int popupWidth = size.x * 1/2;
			int popupHeight = size.y * 1/2;
			 
			   // Inflate popup_layout.xml
			   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
			   LayoutInflater layoutInflater = (LayoutInflater) context
			     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   View layout = layoutInflater.inflate(R.layout.popup_przeg, viewGroup);
			 
			   // tworzenie PopupWindow
			   final PopupWindow popup = new PopupWindow(context);
			   popup.setContentView(layout);
			   popup.setWidth(popupWidth);
			   popup.setHeight(popupHeight);
			   popup.setFocusable(true);
			 
			   // przesuniecie wzgledem buttona help
			   int OFFSET_X = 15;
			   int OFFSET_Y = 70;
			 
			   // Clear the default translucent background
			   popup.setBackgroundDrawable(new BitmapDrawable());
			  
			   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
			}
		
		public void onBackPressed(){
			LibraryActivity.this.finish();
			overridePendingTransition(R.anim.in_left, R.anim.out_right);
		}
		
}