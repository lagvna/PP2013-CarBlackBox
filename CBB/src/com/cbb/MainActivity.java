package com.cbb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Aktywnosc glownego menu aplikacji.
 * Stad mozna przejsc do poszczegolnych aktywnosci aplikacji, tj.
 * - rozpoczac rejestrowanie trasy
 * - przegladac nagrania
 * - zmienic ustawienia aplikacji
 * - wyswietlic informacje o aplikacji i autorach
 * @author lagvna
 *
 */
public class MainActivity extends Activity {
	
	Point p;
	
	/**
	 * Glowny konstruktor aktywnosci.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
	}
	
	/**
	 * Metoda obslugujaca przycisk.
	 * Rozpoczyna aktywnosc nagrywania trasy.
	 * @param v aktualny widok aplikacji
	 */
	public void recordMovie(View v)	{
		//Intent i = new Intent(MainActivity.this, RecActivity.class);
	    //MainActivity.this.startActivity(i);
		Intent intent = new Intent(MainActivity.this, LoadingScreen.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_right, R.anim.out_left);
	}
	
	/**
	 * Metoda obslugujaca przycisk.
	 * Rozpoczyna aktywnosc przegladania nagran.
	 * @param v aktualny widok aplikacji
	 */
	public void browseMovies(View v)	{
		Intent i = new Intent(MainActivity.this, LibraryActivity.class);
	    MainActivity.this.startActivity(i);
	    overridePendingTransition(R.anim.in_right, R.anim.out_left);
	}
	
	/**
	 * Metoda obslugujaca przycisk.
	 * Rozpoczyna aktywnosc dostosowania ustawien aplikacji.
	 * @param v aktualny widok aplikacji
	 */
	public void adjustSettings(View v)	{
		Intent i = new Intent(MainActivity.this, SettingsActivity.class);
	    MainActivity.this.startActivity(i);
	    overridePendingTransition(R.anim.in_right, R.anim.out_left);
	}
	
	/**
	 * Metoda obslugujaca przycisk.
	 * Rozpoczyna aktywnosc wyswietlenia informacji o aplikacji i autorach.
	 * @param v aktualny widok aplikacji
	 */
	public void showInfo(View v)	{
		Intent i = new Intent(MainActivity.this, CreditsActivity.class);
	    MainActivity.this.startActivity(i);
	    overridePendingTransition(R.anim.in_right, R.anim.out_left);
	}

	/**
	 * Metoda wywo�ywana po nacisinieciu przycisku pomocy
	 * @param view aktualny widok
	 */
	public void clickHelp(View view){
		if(p != null){
			showPopup(MainActivity.this, p);
		}
	}
	
	/**
	 * Nadpisana metoda wywolywana przy zmianie focusa w oknie
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	 
	   int[] location = new int[2];
	   ImageButton button = (ImageButton) findViewById(R.id.imageButton1);
	 
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
			int popupWidth = size.x * 3/4;
			int popupHeight = size.y * 2/3;
		 
		   // Inflate popup_layout.xml
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.popup_main, viewGroup);
		 
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
}
