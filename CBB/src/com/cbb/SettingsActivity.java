package com.cbb;

import settings.*;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {
	private boolean flag =false;
	
	private SeekBar bar;
	private TextView textProgress;
 
	private ToggleButton gpsSwitch;
	private SeekBar gpsSeekBar;
	private TextView textgps;
	
	private ToggleButton accSwitch;
	private SeekBar accSeekBar;
	private TextView textacc;
	
	private EditText textNumer;
	private Button Tbutton;
	
	//---setttings---//
	private int len;
	private int sensGPS;
	private int sensACC;
	private boolean hasGPS;
	private boolean hasACC;
	private int Telephon;
	private boolean useGPS;
	private boolean useACC;
	
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
        
        gpsSwitch = (ToggleButton)findViewById(R.id.toggleButton1);
        gpsSeekBar = (SeekBar)findViewById(R.id.seekBar1);
        textgps =(TextView)findViewById(R.id.textView1);
        
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
			e.printStackTrace();
			textProgress.setText(e.getMessage());
		}
        
        useGPS = ustawienia.isGPS();
        useACC = ustawienia.isAccel();
        Telephon = ustawienia.getConntactNumber();
        sensACC = ustawienia.getAccelerometerSensivity();
        sensGPS = ustawienia.getGpsSensivity();
        hasGPS = ustawienia.isDeviceHasGPS();
        hasACC = ustawienia.isDeviceHasAccelerometer();
        textNumer.setText(""+Telephon);
        
        textgps.setText(""+sensGPS);
        gpsSeekBar.setProgress(sensGPS);
        
        textacc.setText(""+sensACC);
        accSeekBar.setProgress(sensACC);
        
        textProgress.setText("Długość nagrania: " +ustawienia.getFilmLength()+ " sekund");
        bar.setProgress(ustawienia.getFilmLength());
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
				textProgress.setText("D�ugo�� nagrania: " + (int)(progress + 10 )+ " sekund");
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
				textgps.setText(""+(int)(progress + 10));
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
				textacc.setText(""+(int)(progress +10));
				sensACC = progress+10;
				flag = true;
			}
		});
        
        if(!hasACC || useACC){
        	accSwitch.setChecked(true);
        	accSeekBar.setEnabled(false);
        }
        
        if(!hasGPS || useGPS){
        	gpsSwitch.setChecked(true);
        	gpsSeekBar.setEnabled(false);
        }
    }
    
	public void Klik(View view){
		try{
			int ok = Integer.parseInt(textNumer.getText().toString());
			Telephon = ok;
			flag =true;
		} catch(NumberFormatException e){
			Toast.makeText(this, "Z�y nr telefonu", Toast.LENGTH_LONG).show();
		}
	}
	
	public void GPSswitch(View view){
		
		if(gpsSwitch.isChecked()){
			gpsSeekBar.setEnabled(false);
			flag = true;
		} else {
			if(!hasGPS){
				Toast.makeText(this, "Nie mo�na wykry� modu�u GPS", Toast.LENGTH_LONG).show();
				gpsSwitch.setChecked(false);
				return;
			}
			gpsSeekBar.setEnabled(true);
			useGPS = true;
		}
	}
	
	public void AccSwitch(View view){
		
		if(accSwitch.isChecked()){
			accSeekBar.setEnabled(false);
			flag = true;
		} else {
			if(!hasACC){
				Toast.makeText(this, "Nie mo�na wykry� akcelerometru", Toast.LENGTH_LONG).show();
				accSwitch.setChecked(false);
				return;
			}
			accSeekBar.setEnabled(true);
			useACC = true;
		}
	}
	
	@Override
	public void onBackPressed(){
		if(flag){
			SaveOpenSettings.save(new Settings(sensACC,sensGPS,useACC, useGPS,Telephon,len, hasACC, hasGPS));
			Toast.makeText(this, "Zmieniono ustawienia", Toast.LENGTH_LONG).show();
		}
		super.onBackPressed();
	}
}