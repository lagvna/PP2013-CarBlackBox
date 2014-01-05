package com.cbb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.VideoView;

/*
 * Poki co jest to aktywnosc, ktora pozwala na odtworzenie filmu, a nie jak sugeruje
 * nazwa do przegladania nagran. Z czasem ta aktywnosc bedzie juz gotowa do obejrzenia wybranego
 * filmu, a wlasciwa zawierajaca list view z filmami sie doda.
 */
public class LibraryActivity extends Activity {
	  VideoView vv;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		  setContentView(R.layout.activity_library);

		  vv = (VideoView) this.findViewById(R.id.VideoView);
		  Uri videoUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb4.mp4");
		  // Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Test.m4v");
		  vv.setVideoURI(videoUri);
		  vv.start();
	  }
}
