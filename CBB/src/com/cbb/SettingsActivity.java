package com.cbb;

import android.os.Bundle;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnSeekBarChangeListener	{
	private SeekBar bar;
	private TextView textProgress;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);     
        
        bar = (SeekBar)findViewById(R.id.movieLengthBar);
        bar.setOnSeekBarChangeListener(this);
        textProgress = (TextView)findViewById(R.id.movieLengthText);
        
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {
    	textProgress.setText("Długość nagrania: " + (int)(progress + 10 )+ " sekund");
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	//textAction.setText("Ustawianie!");
    	
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	//textAction.setText("Gotowe");    	
    }
}