package com.cbb;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class WatchingActivity extends Activity {
	VideoView vv;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		  setContentView(R.layout.activity_watching);

		  vv = (VideoView) this.findViewById(R.id.VideoView);
		  Uri videoUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb4.mp4");
		  vv.setVideoURI(videoUri);
		  vv.start();
	  }
}
