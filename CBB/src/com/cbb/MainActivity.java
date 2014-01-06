package com.cbb;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
	}
	
	public void recordMovie(View v)	{
		Intent i = new Intent(MainActivity.this, RecActivity.class);
	    MainActivity.this.startActivity(i);
	}
	
	public void browseMovies(View v)	{
		Intent i = new Intent(MainActivity.this, LibraryActivity.class);
	    MainActivity.this.startActivity(i);
	}
	
	public void adjustSettings(View v)	{
		Intent i = new Intent(MainActivity.this, SettingsActivity.class);
	    MainActivity.this.startActivity(i);
	}
	
	public void showInfo(View v)	{
		Intent i = new Intent(MainActivity.this, CreditsActivity.class);
	    MainActivity.this.startActivity(i);
	}
}
