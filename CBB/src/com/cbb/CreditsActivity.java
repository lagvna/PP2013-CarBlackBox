package com.cbb;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
/**
 * Aktywnosc informujaca o czym jest ten projekt, oraz o autorach.
 * Dodatkowo widnieja tam informacje o licencji oprogramowania.
 * @author lagvna
 *
 */
public class CreditsActivity extends Activity {
	/** Informacja o aplikacji*/
	private TextView info;
	/** Informacja o warunkach licencyjnych*/
	private TextView info2;
	/** Informacja o autorze 1*/
	private TextView authors1;
	/** Informacja o autorze 2*/
	private TextView authors2;

	@Override
	/**
	 * Glowny konstruktor aktywnosci.
	 * Pobiera i ustawia w szablonie informacje tekstowe o aplikacji
	 * i autorach.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_credits);
		
		info = (TextView) findViewById(R.id.dates_desc);
		info.setText("Car Black Box\nAplikacja napisana na Projekt Programistyczny 2013/2014\nna licencji open source");
		
		info2 = (TextView) findViewById(R.id.dates_desc2);
		info2.setText("Część ikonek jest na licencji linkware, ze strony: \n www.aha-soft.com");
		
		authors1 = (TextView) findViewById(R.id.place_desc);
		authors1.setText(Html.fromHtml("Piotr Lisowski \n" +
				"	<a href=\"http://www.math.uni.wroc.pl/~s258497/\"><br/>Strona domowa</a>"));
		authors1.setMovementMethod(LinkMovementMethod.getInstance());
		
		authors2 = (TextView) findViewById(R.id.place_desc2);
		authors2.setText(Html.fromHtml("Jarosław Mirek \n" +
				"	<a href=\"http://www.math.uni.wroc.pl/~s233021/\"><br/>Strona domowa</a>"));
		authors2.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
	
	public void onBackPressed(){
		CreditsActivity.this.finish();
		overridePendingTransition(R.anim.in_left, R.anim.out_right);
	}
}