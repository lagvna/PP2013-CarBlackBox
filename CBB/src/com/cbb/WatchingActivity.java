package com.cbb;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Klasa sluzaca do ogladania nagrania wykonanego przez aplikacje.
 * @author lagvna
 *
 */
public class WatchingActivity extends Activity {
	/**Widok dla odtwarzacza filmu*/
	VideoView vv;
	/**Nazwa odtwarzanego pliku*/
	String name;
	
	/**
	 * Glowny konstruktor aktywnosci.
	 * 
	 * Tworzy widok dla odtwarzacza oraz rozpoczyna odtwarzanie wybranego filmu.
	 */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		  setContentView(R.layout.activity_watching);

		  name = getName();
		  
		  vv = (VideoView) this.findViewById(R.id.VideoView);
		  Uri videoUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/" + name);
		  vv.setVideoURI(videoUri);
		  vv.start();
	  }
	  
	  /**
	   * Metoda pomocnicza pobierajaca nazwe nagrania z extrasow.
	   * @return zwraca nazwe filmu
	   */
	  private String getName()	{
		  Bundle extras = getIntent().getExtras();
		  String name = extras.getString("title");
		
		  return name;
	  }
}