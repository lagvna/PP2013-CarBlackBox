package com.cbb;

import settings.SaveOpenSettings;
import settings.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
/**
 * Aktywnosc sluzaca do wykonania polaczenia telefonicznego.
 * 
 * W razie wykrycia przez czujniki wypadku po zapytaniu aktywnosc ta jest wywolywana
 * dzieki czemu mozna zadzwonic pod numer 112, lub na swoj numer ICE.
 * 
 * @author lagvna
 *
 */
public class CallActivity extends Activity {
	/** Obiekt ustawien aplikacji, z ktorych pobierany jest numer ICE */
	Settings s;
	/** Numer telefonu ICE*/
	String contactPhoneNo;
	@Override
	/**
	 * Glowny konstruktor aktywnosci.
	 * Pobiera numer ICE.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_call);
		
		try {
			s = SaveOpenSettings.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		contactPhoneNo = Integer.toString(s.getConntactNumber());
	}
	
	/**
	 * Metoda sluzaca wykonania polaczenia telefonicznego pod numer 112
	 * @param v biezacy widok aplikacji
	 */
	public void callEmergency(View v)	{
		try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:112"));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	         e.printStackTrace();
	    }
	}
	
	/**
	 * Metoda sluzaca wykonania polaczenia telefonicznego pod wlasny numer ICE
	 * @param v biezacy widok aplikacji
	 */
	public void callContact(View v)	{
		try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:+48"+contactPhoneNo));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	         e.printStackTrace();
	    }
	}
}
