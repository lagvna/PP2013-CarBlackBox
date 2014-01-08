package com.example.crashdetector;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Detector{
	
	Activity act;
	
	//----GPS------//
	private boolean isGPS;
	private boolean gpsDet = false;
	private volatile int paramGPS;
	private LocationManager locMgr;
	private LocationListener locList;
	private double speed;
	private double PrevSpeed;
	
	//----accelerometer----//
	private boolean isAccel;
	private boolean accDet = false;
	private volatile int paramAccel;
	private SensorManager Sm;
	private SensorEventListener SEL;
	private boolean AccelFlag =false;
	private double currX;
	private double currY;
	private double currZ;
	private double prevX;
	private double prevY;
	private double prevZ;
	
	public Detector(Activity a, boolean GPS, int parGPS, boolean Accel, int parAccel){
		act = a;
		isGPS = GPS;
		if(isGPS)
			initGPS();
		paramGPS = parGPS;
		
		isAccel = Accel;
		if(isAccel)
			initAccel();
		paramAccel = parAccel;
	}
	
	private void cleanAcc(){
		 Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				accDet = false;
			}
		});
		t.start();
	}
	
	private void cleanGPS(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gpsDet = false;
			}
		});
		t.start();
	}
	
	private void initAccel(){
		SEL = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				if(AccelFlag){
					currX = Math.abs(prevX - event.values[0]);
					currY = Math.abs(prevY - event.values[1]);
					currZ = Math.abs(prevZ - event.values[2]);
					prevX = event.values[0];
					prevY = event.values[1];
					prevZ = event.values[2];
					double val = Math.sqrt(Math.pow(currZ, 2)+Math.pow(currX, 2)+Math.pow(currY, 2));
					if(val >= paramAccel){
						accDet = true;
						check();//1 warunek spelniony
						cleanAcc();
					}
					
				} else {
					AccelFlag = true;
					prevX = event.values[0];
					prevY = event.values[1];
					prevZ = event.values[2];
				}
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
		
		Sm = (SensorManager) act.getSystemService(Context.SENSOR_SERVICE);
		Sm.registerListener(SEL, Sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private void initGPS(){
		locMgr = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
		locList = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				speed = location.getSpeed();					
				//if(location.getAccuracy() < 15){
					
				//}
				
				speed *= 3.6;
				if((int)(PrevSpeed - speed) >= paramGPS){
					gpsDet = true;
					cleanGPS();
				}
			}
		};
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locList);
	}
	
	public void setGpsParam(int val){
		if(isGPS)
			paramGPS = val;
		//else
			//throw new Exception("Wrong Parameter");
	}
	
	public void setAccParam(int val){
		if(isAccel)
			paramAccel = val;
		//else
			//throw new Exception("Wrong Parameter");
	}
	
	private void check(){
		if((accDet || !isAccel) && (gpsDet || !isGPS)){
			((MainActivity)act).setAsCrash();
		}
	}
	
	public boolean wasAccident(){
		if((accDet || !isAccel) && (gpsDet || !isGPS))
			return true;
		return false;
	}
}
