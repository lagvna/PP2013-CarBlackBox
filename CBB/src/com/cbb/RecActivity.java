package com.cbb;

import java.io.File;
import java.io.IOException;

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
        
    }

    private void prepareRecorder() {
    	recorder = new MediaRecorder();
        recorder.setPreviewDisplay(holder.getSurface());
            
        if (usecamera) {
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
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/videocapture1.mp4");
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
            
        //recorder.setMaxDuration(60000); // 50 seconds
        //recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
            
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
    
    private void recordVideo()	{
    	if (recording) {
        	recorder.stop();
        	try {
        		camera.reconnect();
            } catch (IOException e) {
            	e.printStackTrace();
            }   
            recording = false;
            Log.v(LOGTAG, "Recording Stopped");
            prepareRecorder();
        } else {
        	recording = true;
            recorder.start();
            Thread recordingThread = new Thread() {
    			
    	        @Override
    	        public void run() {
    	        	for(int i = 0; i < 6; i++)	{
    	        		if(recording)	{
    		        		//while true
    		        		//for i 1:60
    		        		// stad beda pobierane odczyty GPS, Akcelerometr
    		        		System.out.println("Nagrywam");
    		        		try {
    							Thread.sleep(1000);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    		        	} else	{
    		        		break;
    		        	}
    	        	}
    	        }
    		};
            recordingThread.start();
            Log.v(LOGTAG, "Recording Started");
        }
    }
    
    public void startRecording(View v) { // obsluga buttona
        	recordVideo();
    }

    public void surfaceCreated(SurfaceHolder holder) {            
        	camera = Camera.open();
               
            try {
            	camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }          
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
            prepareRecorder();        
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
        	recorder.stop();
            recording = false;
        }
        recorder.release();
        if (usecamera) {
        	previewRunning = false;
            camera.release();
        }
        finish();
    }
}