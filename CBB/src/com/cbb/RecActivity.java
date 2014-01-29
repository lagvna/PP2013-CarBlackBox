package com.cbb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import settings.SaveOpenSettings;
import settings.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Klasa sluzaca do nagrywania trasy w trybie ciaglym.
 * Algorytm zostal opisany na glownej stronie dokumentacji.
 * 
 * Klasa tworzy watek nagrywania, ktory na zmiane nagrywa w pliku tmpcbb1 oraz tmpcbb2.
 * W przypadku wykrycia przez klase Detector wypadku, nagranie zostaje przerwane i 
 * pliku sa laczone i docinane do zadanej dlugosci.
 * @author lagvna
 *
 */
public class RecActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {

	/** Nazwa loggera dla klasy*/
    public static final String LOGTAG = "VIDEOCAPTURE";
    /**ID dialogu wywolywanego podczac laczenia nagran*/
    public static final int PLEASE_WAIT_DIALOG = 1;
    
    /**Obiekt nagrywajacy filmy*/
    private MediaRecorder recorder;
    /**Obiekt surface view wyswietlajacego widok z kamery*/
    private SurfaceHolder holder;
    /**Obiekt ustawien dla kamery (jakosc itd)*/
    private CamcorderProfile camcorderProfile;
    /**Obiekt obslugujacy kamere urzadzenia*/
    private Camera camera;
    /** Pole wyswietlacza obrazu */
    private SurfaceView cameraView;
    /**Tymczasowy plik nagrania 1*/
    private File tmp1;
    /**Tymczasowy plik nagrania 2*/
    private File tmp2;
    /**Licznik przebiegow rejestratora. Okresla w jakiej kolejnosci laczyc filmy*/
    private int counter;
    /**Watek nagrywania trasy w trybie ciaglym*/
    private Thread recordingThread;
    /**Guziczek rec, pokazujacy, ze aktualnie jest tworzone nagranie*/
    private ImageButton recCir;
    /**Detektor obslugujacy GPS i akcelerometr. Bada czy mial miejsce wypadek*/
    private Detector sensorDetector;
    /**Aktualny widok aplikacji dla watkow pochodnych*/
    private View mView;
    /**Obiekt ustawien aplikacji*/
    private Settings s;
    /**Pole dlugosci nagrania*/
    private int movieLength;
    
    /**Flaga okreslajaca czy trwa nagrywanie*/
    boolean recording = false;
    /**Flaga okreslajaca czy kamera jest uzywana*/
    boolean usecamera = true;
    /**Flaga okreslajaca czy ma zostac wyswietlony podglad nagrywania*/
    boolean previewRunning = false;
    /**Flaga okreslajaca czy zostal nacisniety surfaceView (start/stop nagrywania)*/
    boolean surfacePressed = false;
    /**Flaga okreslajaca czy detektor wykryl wypadek*/
    boolean wasAccident = false;
    /**Nazwa aktualnie nagrywanego pliku tymczasowego*/
    String currentName = null;
    
    /**
     * Glowny konstruktor aktywnosci.
     * 
     * Ustawia pola klasy tj. guziczek rec, okreslajacy czy trwa nagranie,
     * ustawienia kamery, ustawienia aplikacji,
     * detektor wypadku, oraz tworzy pliki tymczasowe do zapisywania w nich nagran.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                            
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        camcorderProfile = CamcorderProfile.get(1, CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.activity_rec);
        
        recCir = (ImageButton) findViewById(R.id.redBut);
        recCir.setVisibility(View.GONE);
        
        try {
			s = SaveOpenSettings.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        movieLength = s.getFilmLength();
        sensorDetector = new Detector(this, s.isGPS(), s.getGpsSensivity(), s.isAccel(), s.getAccelerometerSensivity());
        
        cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        
        cameraView.setOnClickListener(this);
        
        counter = 0;
        tmp1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/tmpcbb1.mp4");
		tmp2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/tmpcbb2.mp4");
		
		Toast.makeText(this, "Stuknij ekran, aby rozpocząć nagrywanie", Toast.LENGTH_SHORT).show();

		AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int streamType = AudioManager.STREAM_SYSTEM;
        mgr.setStreamSolo(streamType, true);
        mgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mgr.setStreamMute(streamType, true);
    }

    /**
     * Metoda sluzaca do wyswietlania badz nie, koleczka informujacego o 
     * aktualnie trwajacym nagraniu.
     * 
     */
    private void changeCircleVisibility()	{
    	if(surfacePressed)	{
    		recCir.setVisibility(View.VISIBLE);
    		if(recCir.isShown())	{
    		}
    	} else	{
    		recCir.setVisibility(View.INVISIBLE);
    	}
    }
    
    /**
     * Metoda przygotowujaca kamere do nagrania.
     * Ustawia kamere, tworzy plik.
     * @param fileName nazwa pliku, do ktorego ma zostac zapisane nagranie
     */
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

        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {// tutaj wchodzimy w przypadku high quality
            try {
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+fileName+".mp4");
                System.out.println(newFile.getAbsolutePath());
                recorder.setOutputFile(newFile.getAbsolutePath());
            } catch (Exception e) {
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
    
    /**
     * Listener od surfaceView. Dzieki niemu mozna stukajac w ekran
     * rozpoczac lub zakonczyc nagrywanie.
     * @param v aktualny widok aplikacji
     */
    @Override
	public void onClick(View v) {
    	mView = v;
    	/*if(recorder != null)	{ moze to wystarczy zeby sie nie wypierdalalo przy czestym klikaniu w ekran
    		
    	}*/
    	if(!surfacePressed)	{
    		cameraView.setClickable(false);
    		surfacePressed = true;
    		tmp1.delete();
    		tmp2.delete();
    		prepareRecorder(nameChooser());
        	changeCircleVisibility();
    		recordVideo();
    	}
    	else	{
    		surfacePressed = false;
    		recording = false;
    		
    		changeCircleVisibility();
    		
    		while(recordingThread.isAlive())	{}
    		
    		if(tmp2.exists())	{
    			if(counter%2 != 0)	{ // normalna kolejnosc
    				new MovieAppender(this, false).execute();
    				if(wasAccident){
						makeQuestion();
					} else	{
						cameraView.setClickable(true);
					}
    			}
    			else	{ // kolejnosc odwrocona
    				new MovieAppender(this, true).execute();
    				if(wasAccident){
						makeQuestion();
					} else	{
						cameraView.setClickable(true);
					}
    			}
    		} else	{
    			try {
        			File dst = File.createTempFile("cbb", ".mp4", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
					copy(tmp1, dst);
					if(wasAccident){
						makeQuestion();
					} else	{
						cameraView.setClickable(true);
					}
					Toast.makeText(this, "Utworzono nagranie!", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
	}
    
    /**
     * Metoda wywolywana w przypadku wykrycia wypadku.
     * Dzieki niej tworzony jest dialog, ktory pyta o to, czy uzytkownik chce wykonac telefon alarmowy.
     * Stosownie do odpowiedzi wywoluje aktywnosc pozwalajaca to uczynic pod numer alarmowy 112
     * lub numer ICE z ustawien.
     */
    private void makeQuestion()	{
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	 
    	builder.setTitle("Wykryto wypadek!");
    	builder.setMessage("Czy chcesz zadzwonić?");
    	 
    	builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
    	   @Override
    	   public void onClick(DialogInterface dialog, int which) {
    	         
    		   Intent i = new Intent(RecActivity.this, CallActivity.class);
    		   RecActivity.this.startActivity(i);
    	 
    	        dialog.dismiss();
    	   }
    	});
    	 
    	builder.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
    	   @Override
    	   public void onClick(DialogInterface dialog, int which) {    	 
    	        dialog.dismiss();
    	   }
    	});
    	
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x * 4/5;
		int height = size.y * 3/4;
    	
    	AlertDialog alert = builder.create();
    	alert.show();
    	alert.getWindow().setLayout(width, height);
    	cameraView.setClickable(true);
    }
    
    /**
     * Metoda sluzaca rozpoczeciu nagrywania poprzez uruchomienie watku dla obiektu nagrywajacego.
     */
    private void startRecording()	{
    	recording = true;
    	recorder.start();
        Log.v(LOGTAG, "Recording Started");
    }
    
    /**
     * Metoda sluzaca do zakonczenia nagrania.
     * Jesli nagrywanie nie zostalo przerwane przez uzytkownika, lub poprzez detektor wypadku,
     * to plik zostaje zapisany w nienaruszonej formie, a kolejna partia filmu nagrywana jest
     * do drugiego pliku.
     * 
     * W przypadku wykrycia wypadku lub zakonczenia nagrywania przez uzytkownika, kamera nie jest przygotowywana
     * do kolejnej partii filmu.
     * @param isNext
     */
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
    
    /**
     * Metoda wywolujaca watek nagrywajacy.
     * Watek ten na zmiane rejestruje do pliku tmp1 i tmp2, oraz co sekunde bada czy detektor nie podal
     * skrajnych wartosci swiadczacych o wypadku.
     * 
     */
    private void recordVideo()	{
    	recordingThread = new Thread() {
    		@Override
    		public void run() {
    			while(true)	{
    				startRecording(); // rozpoczynamy nagrywanie przez zadana ilosc sekund
    				int seconds = 1;
    				
    				for(int i = 0; i < movieLength; i++)	{
    					if(i == 1)	{
        					cameraView.setClickable(true);
        				}
    					wasAccident = sensorDetector.wasAccident();
    					if(recording && !wasAccident)	{ // dopoki nagrywanie true pobieramy odczyty GPS i Akcelerometru
    						System.out.println("Nagrywam"+seconds);
    						try {
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
    					cameraView.setClickable(false);
    					break;
    				}
    				stopRecording(true); // zapisujemy nagranie i tworzymy nowy plik
    	        }
    	    }
    	};
        recordingThread.start();
    }
    
    /**
     * Metoda konczaca nagranie w przypadku wykrycia przez detektor wypadku.
     */
    private void wasAccidentScanner()	{
    	runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	if(wasAccident)	{
            		onClick(mView);
            	}
            }
        });
    }
    
    /**
     * Metoda pomocnicza zwracajaca nazwe pliku, do ktorego aktualnie powinno byc zapisywane nagranie
     * @return nazwa pliku tymczasowego
     */
    private String nameChooser()	{
    	String tmp = null;
    	if(currentName == null)	{
    		currentName = "tmpcbb1";
    	} else if(currentName.equals("tmpcbb1"))	{
    		currentName = "tmpcbb2";
    	} else if(currentName.equals("tmpcbb2"))	{
    		currentName = "tmpcbb1";
    	}
    	
    	tmp = currentName;
    	
    	return tmp;
    }
    
    /**
     * Metoda kopiujaca nagranie w przypadku, gdyby nie zaszla potrzeba laczenia dwoch plikow.
     * tj. nagranie zostalo zakonczone na pliku tmp1.
     * @param src sciezka zrodla pliku
     * @param dst sciezka docelowa pliku
     * @throws IOException wyjatek bledu kopiowania
     */
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Dialog wyskakujacy jako zdarzenie asynchroniczne, dzieki ktoremu
     * dwa pliki z nagraniami sa laczone oraz docinane przez obiekt klasy MovieAppender.
     * 
     * @param dialogId ID dialogu, dzieki ktoremu dialog wie gdzie ma zwrocic odpowiednie dane swoich metod
     * @return zwraca wynik dialogu (toast) w przypadku stworzenia nagrania
     */
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
    
    /**
     * Metoda tworzaca widok dla kamery.
     * @param holder pomocnik widoku
     */
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

    /**
     * Metoda wywolywana, w chwili kiedy widok zostal zmieniony, czyli kiedy 
     * rozpoczyna sie ponowne nagranie.
     * @param holder pomocnik widoku
     * @param format format widoku
     * @param width szerokosc widoku
     * @param height wysokosc widoku
     */
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
    
    /**
     * Metoda wywolywana podczas zakonczenia aktywnosci i tym samym 
     * niszczenia widoku
     * @param holder pomocnik widoku
     */
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
    
    /**
     * Metoda wywolywana przy zakonczeniu aktywnosci.
     * Czeka az zakonczone zostana watki w aplikacji, a nastepnie przechodzi z powrotem do nadaktywnosci.
     */
    public void onBackPressed(){
    	recording = false;
    	if(recordingThread != null)	{
    		while(recordingThread.isAlive())	{}
    	}
    	
		AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int streamType = AudioManager.STREAM_SYSTEM;
		mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		streamType = AudioManager.STREAM_SYSTEM;
		mgr.setStreamSolo(streamType, false);
		mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		mgr.setStreamMute(streamType, false);
		RecActivity.this.finish();
	}
}