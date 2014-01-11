package com.cbb;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageButton;

public class RecActivity extends Activity implements SurfaceHolder.Callback {

    public static final String LOGTAG = "VIDEOCAPTURE";
    public static final int PLEASE_WAIT_DIALOG = 1;
    
    private MediaRecorder recorder;
    private SurfaceHolder holder;
    private CamcorderProfile camcorderProfile;
    private Camera camera;
    private File tmp1;
    private File tmp2;
    private int counter;
    private Thread recordingThread;
    private Button recButton;
    private ImageButton recCir;
    private Detector sensorDetector;
    private View mView;
    
    boolean recording = false;
    boolean usecamera = true;
    boolean previewRunning = false;
    boolean buttonPressed = false;
    boolean wasAccident = false;
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
        
        recButton = (Button) findViewById(R.id.recButton);
        recCir = (ImageButton) findViewById(R.id.redBut);
        changeButtonText();
        
        //tutaj trzeba zadbac czy w settingsach jest GPS i akcel na ON
        sensorDetector = new Detector(this, false, 50, true, 40);
        
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
    	
        counter = 0;
        tmp1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb1.mp4");
		tmp2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb2.mp4");
    }

    private void changeButtonText()	{
    	if(recButton.getText().equals("Start"))	{
    		recCir.setVisibility(View.VISIBLE);
    		recButton.setText("Stop");
    		
    	} else	{
    		recCir.setVisibility(View.INVISIBLE);
    		recButton.setText("Start");
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

        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) { // low quality, nie trzeba camera id, czy chcemy low quality?
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
    
    private void stopRecording(boolean isNext)	{
    	recorder.stop();
    	try {
    		camera.reconnect();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        Log.v(LOGTAG, "Recording Stopped");
        if(isNext)	{
        	prepareRecorder(nameChooser());
        }
    }
    
    private void recordVideo()	{
    	recordingThread = new Thread() {
    			
    		@Override
    		public void run() {
    			while(true)	{
    				startRecording2(); // rozpoczynamy nagrywanie przez zadana ilosc sekund
    				int seconds = 1;
    				for(int i = 0; i < 30; i++)	{
    					wasAccident = sensorDetector.wasAccident();
    					if(recording && !wasAccident)	{ // dopoki nagrywanie true pobieramy odczyty GPS i Akcelerometru
    		       		// 	stad beda pobierane odczyty GPS, Akcelerometr
    						System.out.println("Nagrywam"+seconds);
    						try { // tu gdzies trzeba bedzie dac recording na false
    							Thread.sleep(1000);
    							seconds++;
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    					} else	{ // jak juz nie nagrywamy to break i zapisujemy film, gdyby gps/akcel cos pokazal
    						break;
    					}
    				}
    				
    				counter++;
    				
    				if(!recording || wasAccident){ // jesli zostal klikniety guzik lub gps/akcel to wychodzimy z petli i juz nie kopiujemy ost nagrania
    					stopRecording(false); // zapisujemy nagranie i nie tworzymy nowego pliku
    					wasAccidentScanner();
    					break;
    				}
    				stopRecording(true); // zapisujemy nagranie i tworzymy nowy plik
    	        }
    	    }
    	};
        recordingThread.start();
    }
    
    private void wasAccidentScanner()	{
    	runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	if(wasAccident)	{
            		startRecording(mView);
            	}
            }
        });
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
    
    public void startRecording(View v) { // obsluga buttona, trzeba jeszcze obsluzyc przypadek gdy film < żądana dlugosc
    	
    	mView = v;
    	if(!buttonPressed)	{
    		changeButtonText();
    		tmp1.delete();
    		tmp2.delete();
    		prepareRecorder(nameChooser());
    		buttonPressed = true;
    		recordVideo();
    	}
    	else	{
    		changeButtonText();
    		buttonPressed = false;
    		recording = false;
    		
    		while(recordingThread.isAlive())	{}
    		
    		if(counter%2 != 0)	{ // normalna kolejnosc
    			new MovieAppender(this, false).execute();
    		}
    		else	{ // kolejnosc odwrocona
    			new MovieAppender(this, true).execute();
    		}
    	}
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {
 
        switch (dialogId) {
        case PLEASE_WAIT_DIALOG:
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Tworzenie nagrania");
            dialog.setMessage("Proszę czekać....");
            dialog.setCancelable(false);
            return dialog;
 
        default:
            break;
        }
        return null;
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
    
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.v(LOGTAG, "surfaceDestroyed");
        if (recording) {
        	recorder.stop();
            recording = false;
        }
        //recorder.release(); // zeby nie wywalalo bledu przy wyjsciu bez nagrywania
        if(usecamera)	{
        	previewRunning = false;
        	camera.release();
        }
        finish();
    }
    
    public void onBackPressed(){
    	recording = false;
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		RecActivity.this.finish();
	}
}