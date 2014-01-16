package com.cbb;

import settings.SaveOpenSettings;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		SaveOpenSettings.defaultSettings(getApplicationContext());
		
		final Thread mainThread = new Thread() {
	        @Override
	        public void run() {
	        	try	{
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        
	        	Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	        	SplashActivity.this.startActivity(intent);
	        	SplashActivity.this.finish();
	        }
		};

	    mainThread.start();
	}

	@Override
    public void onDestroy(){
    	super.onDestroy();
    }
}