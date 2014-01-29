package com.cbb;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LoadingScreen extends Activity {
	private final int WAIT_TIME = 1500;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_screen);
		findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

		new Handler().postDelayed(new Runnable(){ 
			@Override 
		    public void run() { 
	        	Intent mainIntent = new Intent(LoadingScreen.this,RecActivity.class); 
		    	LoadingScreen.this.startActivity(mainIntent); 
		    	LoadingScreen.this.finish(); 
			} 
		}, WAIT_TIME);
	}
}
