package com.cbb;

import settings.SaveOpenSettings;
import settings.Settings;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Aktywnosc ustawien programu
 * urzytkownik moze wybrac ustawienia ktore pozniej zostana 
 * zapisane dp pliku XML i wykorzystane w innych aktywnosciach
 * 
 * User do wyboru ma :
 * -numer kontaktowy do bliskiej osoby
 * -czy chce korzystac z akcelerometru do wykrywania wypadkow
 * -czy chce korzysta z GPS do wykrywania wypadkow
 * -dlugosc filmu jaki chce nagrywac
 * @author Lisu
 *
 */
public class SettingsActivity extends Activity {
	/**
	 * flaga ustawiana na true gdy cos zostalo zmienione i trzeba zapisac ustawienia
	 */
	private boolean flag =false;
	/**
	 * Wspolrzedne przycisku pomocy
	 */
	private Point p;
	//////////////////////////////////////////////////////
	
	/**
	 * SeekBar dlugosci czasu nagrywania filmu
	 */
	private SeekBar bar;
	/**
	 * TextView dlugosci czasu nagrywania filmu
	 */
	private TextView textProgress;
	///////////////////////////////////////////////////////////
	
	/**
	 * przycisk wlaczania i wylaczania modulu GPS dla detektora
	 */
	private ToggleButton gpsSwitch;
	/**
	 * SeekBar do parametru czulosci GPS
	 */
	private SeekBar gpsSeekBar;
	/**
	 * TextView do wypisywania aktualnego stanu seekbara
	 */
	private TextView textgps;
	////////////////////////////////////////////////////
	
	/**
	 * przycisk wlaczajacy i wylaczajacy akcelerometr
	 */
	private ToggleButton accSwitch;
	/**
	 * seekbar parametru czulosci akcelerometru dla detektora
	 */
	private SeekBar accSeekBar;
	/**
	 * textview do wypisywania aktualnego stanu seekbara
	 */
	private TextView textacc;
	//////////////////////////////////////////////
	
	/**
	 * edytowalne pole do wpisania numeru telefonu
	 */
	private EditText textNumer;
	/**
	 * przycisk uaktualniajace numer telefonu z edittexta
	 */
	private Button Tbutton;
	
	/*****************************************************************************************/
	//---setttings---//
	/*****************************************************************************************/
	/**
	 * aktualnie ustawina dlugosc filmu
	 */
	private int len;
	/**
	 * aktualnie ustawiona czulosc GPS
	 */
	private int sensGPS;
	/**
	 * aktualnie ustawiona czulosc akcelerometru
	 */
	private int sensACC;
	/**
	 * Czy uzadzenie posiada modul GPS
	 */
	private boolean hasGPS;
	/**
	 * czy uzadzenie posiada akcelerometr
	 */
	private boolean hasACC;
	/**
	 * aktualnie ustawiony numer telefonu
	 */
	private int Telephon;
	/**
	 * czy uzytkownik chce uzywac modulu GPS
	 */
	private boolean useGPS;
	/**
	 * czy uzytkownik chceuzywac akcelerometru
	 */
	private boolean useACC;
	
	//-----------------------------------------------------------------//
	
	//----------------------------------------------------------------//
	/**
	 * Metoda wywolywana przy starcie aktywnosci
	 * ustawia wysztstkie pola obiektu
	 * wlacza listenery itd
	 */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);   
        
        
        textProgress = (TextView)findViewById(R.id.movieLengthText);
        bar = (SeekBar)findViewById(R.id.movieLengthBar);
        
        gpsSwitch = (ToggleButton) findViewById(R.id.toggleButton1);
        gpsSeekBar = (SeekBar)findViewById(R.id.seekBar1);
        textgps =(TextView)findViewById(R.id.textV);
        
        accSwitch = (ToggleButton)findViewById(R.id.toggleButton2);
        accSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        textacc = (TextView)findViewById(R.id.textView2);
        
        textNumer = (EditText)findViewById(R.id.editText1);
        Tbutton = (Button)findViewById(R.id.button1);
        
        
        Settings ustawienia = null;
        try {
        	
			ustawienia = SaveOpenSettings.open();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("blad",e.toString());
			e.printStackTrace();
			textProgress.setText(e.getMessage());
		}
        
        len = ustawienia.getFilmLength();
        useGPS = ustawienia.isGPS();
        useACC = ustawienia.isAccel();
        Telephon = ustawienia.getConntactNumber();
        sensACC = ustawienia.getAccelerometerSensivity();
        sensGPS = ustawienia.getGpsSensivity();
        hasGPS = ustawienia.isDeviceHasGPS();
        hasACC = ustawienia.isDeviceHasAccelerometer();
        textNumer.setText(""+Telephon);
        textNumer.setSelected(false);
        
        textgps.setText("Czułość: "+sensGPS+" km/h");
        gpsSeekBar.setProgress(sensGPS-10);
        
        textacc.setText("Czułość: "+sensACC+" m/s^2");
        accSeekBar.setProgress(sensACC-10);
        
        textProgress.setText("Długość nagrania: " +ustawienia.getFilmLength()+ " sekund");
        bar.setProgress(ustawienia.getFilmLength()-10);
        bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				textProgress.setText("Długość nagrania: " + (int)(progress + 10 )+ " sekund");
				len = (int)(progress + 10);
				flag = true;
			}
		});
          
        gpsSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				textgps.setText("Czułość: "+(int)(progress + 10)+" km/h");
				sensGPS = (int)(progress + 10);
				flag = true;
			}
		});
        
        accSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				textacc.setText("Czułość: "+(int)(progress +10)+" m/s^2");
				sensACC = progress+10;
				flag = true;
			}
		});
        
        accSwitch.setChecked(true);
        gpsSwitch.setChecked(true);
        //sprawdzamy czy musimy wylaczyc aklelerometr 
        if(!hasACC || !useACC){
        	accSwitch.setChecked(false);
        	accSeekBar.setEnabled(false);
        }
        //sprawdzamy czy musimy wylaczyc gps
        if(!hasGPS || !useGPS){
        	gpsSwitch.setChecked(false);
        	gpsSeekBar.setEnabled(false);
        }
       
		
    }
	
	/**
	 * Metoda wywolywana przez przycisk od numeru telefonu 
	 * zapisuje aktualny stan z pola textEdit
	 * @param view aktualny widok aplikacji
	 */
	public void Klik(View view){
		try{
			int ok = Integer.parseInt(textNumer.getText().toString());
			Telephon = ok;
			flag =true;
		} catch(NumberFormatException e){
			Toast.makeText(this, "Zły nr telefonu", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Metoda wywo�ywana po nacisinieciu przycisku pomocy
	 * @param view aktualny widok
	 */
	public void clickHelp(View view){
		if(p != null){
			showPopup(SettingsActivity.this, p);
		}
	}
	
	/**
	 * Nadpisana metoda wywolywana przy zmianie focusa w oknie
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	 
	   int[] location = new int[2];
	   ImageButton button = (ImageButton) findViewById(R.id.imageHelp);
	 
	   // Get the x, y location and store it in the location[] array
	   // location[0] = x, location[1] = y.
	   button.getLocationOnScreen(location);
	 
	   //Initialize the Point with x, and y positions
	   p = new Point();
	   p.x = location[0];
	   p.y = location[1];
	}
	
	/**
	 * Metoda do pokazywania dymku z powiadomieniem
	 * @param context aktualne aktivity
	 * @param p punkt odniesienia od tego punktu bedzie liczone polozenie dymka
	 */
	@SuppressWarnings("deprecation")
	private void showPopup(final Activity context, Point p) {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int popupWidth = size.x * 1/2;
		int popupHeight = size.y * 2/3;
		
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
		 
		   // tworzenie PopupWindow
		   final PopupWindow popup = new PopupWindow(context);
		   popup.setContentView(layout);
		   popup.setWidth(popupWidth);
		   popup.setHeight(popupHeight);
		   popup.setFocusable(true);
		 
		   // przesuniecie wzgledem buttona help
		   int OFFSET_X = 15;
		   int OFFSET_Y = 70;
		 
		   // Clear the default translucent background
		   popup.setBackgroundDrawable(new BitmapDrawable());
		  
		   popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
		}
	
	/**
	 * Metoda wywolywana przez wlacznik GPS
	 * sprawdza czy mozna wlaczyc 
	 * oraz ustawia komponety z grupy GPS na enabled by mozna bylo ustawic czulosc
	 * oczywiscie moze tez wylaczyc modul i komponenty 
	 * @param view aktualny widok aplikacji
	 */
	public void GPSswitch(View view){
		
		if(!gpsSwitch.isChecked()){
			gpsSeekBar.setEnabled(false);
			useGPS = false;
			flag = true;
		} else {
			if(!hasGPS){
				Toast.makeText(this, "Nie można wykryć modułu GPS", Toast.LENGTH_LONG).show();
				gpsSwitch.setChecked(false);
				return;
			}
			gpsSeekBar.setEnabled(true);
			useGPS = true;
			flag = true;
		}
	}
	
	/**
	 * Metoda wywolywana przez wlacznik akcelerometru
	 * sprawdza czy mozna wlaczyc 
	 * oraz ustawia komponety z grupy akcelerometru na enabled by mozna bylo ustawic czulosc 
	 * oczywiscie moze tez wylaczyc modul i komponenty
	 * @param view aktualny widok aplikacji
	 */
	public void AccSwitch(View view){
		
		if(!accSwitch.isChecked()){
			accSeekBar.setEnabled(false);
			useACC = false;
			flag = true;
		} else {
			if(!hasACC){
				Toast.makeText(this, "Nie można wykryć akcelerometru", Toast.LENGTH_LONG).show();
				accSwitch.setChecked(false);
				return;
			}
			accSeekBar.setEnabled(true);
			useACC = true;
			flag = true;
		}
	}
	
	/**
	 * Wywolywana przy wychodzeniu z aktywnosci zapisuje stawienia do pliku xml
	 */
	@Override
	public void onBackPressed(){
		if(flag){
			SaveOpenSettings.save(new Settings(sensACC,sensGPS,useACC, useGPS,Telephon,len, hasACC, hasGPS));
			Toast.makeText(this, "Zmieniono ustawienia", Toast.LENGTH_LONG).show();
		}
		super.onBackPressed();
		overridePendingTransition(R.anim.in_left, R.anim.out_right);
	}
}