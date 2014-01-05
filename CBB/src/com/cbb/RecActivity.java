package com.cbb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class RecActivity extends Activity implements SurfaceHolder.Callback {

    public static final String LOGTAG = "VIDEOCAPTURE";

    private MediaRecorder recorder;
    private SurfaceHolder holder;
    private CamcorderProfile camcorderProfile;
    private Camera camera;        
    
    boolean recording = false;
    boolean usecamera = true;
    boolean previewRunning = false;
    boolean buttonPressed = false;
    String currentName = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                            
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        camcorderProfile = CamcorderProfile.get(1, CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.activity_rec);

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
    	
        File dst = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb2.mp4");
		if(dst.exists())	{
			System.out.println("JES2!");
			System.out.println(dst.length());
		}
		File dst1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb1.mp4");
		if(dst1.exists())	{
			System.out.println("JEST1!");
			System.out.println(dst1.length());
		}
    }

    private void prepareRecorder(String fileName) {
    	recorder = new MediaRecorder();
        recorder.setPreviewDisplay(holder.getSurface());
            
        if(usecamera)	{
        	camera.unlock();
            recorder.setCamera(camera);
        }
        
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(camcorderProfile);

        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) { // low quality, nie trzeba camera id
        	try {
        		File newFile = File.createTempFile("first", ".3gp", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                recorder.setOutputFile(newFile.getAbsolutePath());
                System.out.println(newFile.getAbsolutePath());
            } catch (IOException e) {
            	Log.v(LOGTAG,"Couldn't create file");
                e.printStackTrace();
                finish();
            }
        } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {// tutaj wchodzimy w przypadku high quality
            try {
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+fileName+".mp4");
                System.out.println(newFile.getAbsolutePath());
                recorder.setOutputFile(newFile.getAbsolutePath());
            } catch (Exception e) {
            	Log.v(LOGTAG,"Couldn't create file");
                e.printStackTrace();
                finish();
            }
        } else {
        	try {
        		File newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
                recorder.setOutputFile(newFile.getAbsolutePath());
            } catch (IOException e) {
                    Log.v(LOGTAG,"Couldn't create file");
                    e.printStackTrace();
                    finish();
            }
        }
            
        try {
        	recorder.prepare();
        } catch (IllegalStateException e) {
        	e.printStackTrace();
            finish();
        } catch (IOException e) {
        	e.printStackTrace();
            finish();
        }
    }
    
    private void startRecording2()	{
    	recording = true;
    	recorder.start();
        Log.v(LOGTAG, "Recording Started");
    }
    
    private void proceedToNext()	{
    	recorder.stop();
    	try {
    		camera.reconnect();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        Log.v(LOGTAG, "Recording Stopped");
        prepareRecorder(nameChooser());
    }
    
    private void stopRecording()	{
    	recorder.stop();
    	try {
    		camera.reconnect();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        Log.v(LOGTAG, "Recording Stopped");
        //prepareRecorder(nameChooser());
    }
    
    private void recordVideo()	{
    	Thread recordingThread = new Thread() {
    			
    		@Override
    		public void run() {
    			while(true)	{
    				startRecording2(); // rozpoczynamy nagrywanie przez zadana ilosc sekund
    				for(int i = 0; i < 6; i++)	{
    					if(recording)	{ // dopoki nagrywanie true pobieramy odczyty GPS i Akcelerometru
    		       		// 	stad beda pobierane odczyty GPS, Akcelerometr
    						System.out.println("Nagrywam");
    						try { // tu gdzies trzeba bedzie dac recording na false
    							Thread.sleep(1000);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    					} else	{ // jak juz nie nagrywamy to break i zapisujemy film, gdyby gps/akcel cos pokazal
    						break;
    					}
    				}
    				if(!recording){ // jesli zostal klikniety guzik lub gps/akcel to wychodzimy z petli i juz nie kopiujemy ost nagrania
    					stopRecording(); // zapisujemy nagranie i nie zmieniamy nazwy
    					break;
    				}
    				proceedToNext(); // zapisujemy nagranie
    	        }
    	    }
    	};
        recordingThread.start();
    }
    
    private String nameChooser()	{
    	String tmp = null;
    	if(currentName == null)	{
    		currentName = "cbb1";
    	} else if(currentName.equals("cbb1"))	{
    		currentName = "cbb2";
    	} else if(currentName.equals("cbb2"))	{
    		currentName = "cbb1";
    	}
    	
    	tmp = currentName;
    	
    	return tmp;
    }
    
    public void startRecording(View v) { // obsluga buttona
    	if(!buttonPressed)	{
    		prepareRecorder(nameChooser());
    		buttonPressed = true;
    		recordVideo();
    	}
    	else	{
    		buttonPressed = false;
    		recording = false;
    	}
    }

    public void surfaceCreated(SurfaceHolder holder) {            
        Log.v(LOGTAG, "surfaceCreated");
        if(usecamera)	{
            camera = Camera.open();
                   
            try {
            	camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            }
            catch (IOException e) {
            	Log.e(LOGTAG,e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.v(LOGTAG, "surfaceChanged");
    	if (!recording && usecamera) {
        	if (previewRunning){
        		camera.stopPreview();
            }

        	try {
        		Camera.Parameters p = camera.getParameters();
                p.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
                p.setPreviewFrameRate(camcorderProfile.videoFrameRate);
                camera.setParameters(p);
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }    
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) { // poprawka zeby nie wywalalo bledu
    	Log.v(LOGTAG, "surfaceDestroyed");
        if (recording) {
        	recorder.stop();
            recording = false;
        }
        //recorder.release();
        if(usecamera)	{
        	previewRunning = false;
        	camera.release();
        }
        finish();
    }
}