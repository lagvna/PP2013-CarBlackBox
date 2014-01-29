package com.cbb;

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

/**
 * Klasa Detector zajmuje sie wykrywaniem wypadkow
 * Detekcja odbywa sie przy pomocy 2-och opcjonalnych modolow- GPS oraz akcelerometr
 * 
 * detektor posiada sluchaczy wybrany modulow. Jesli dla parametrow podanych przy
 * konstrukcji obiektu zostanie wykryty wypadek w jednym z modulow, odpowiednia flaga przyjmuje
 * wartosc true. Jesli wszystkie flagi ustawione sa na true oznacza to ze detektor uwaza, ze byl wypadek.
 * Flaga w stanie true znajduje sie maksymalnie 4 sekundy potem wraca do wartosci false.
 * 
 * @author Lisu
 *
 */
public class Detector{
	/**
	 * Aktywnosc tworzaca detektor
	 */
	Activity act;
	
	//----GPS------//
	/**
	 * Czy uzytkownik chce i moze uzywac GPS
	 */
	private boolean isGPS;
	/**
	 * Flaga ustawiana na true jesli modul GPS wykryje wypadek
	 */
	private boolean gpsDet = false;
	/**
	 * parametr czulosci GPS podawany w konstruktorze
	 */
	private volatile int paramGPS;
	/**
	 * manager lokacji do obslugi GPS
	 */
	private LocationManager locMgr;
	/**
	 * sluchacz zmiany lokacji
	 */
	private LocationListener locList;
	/**
	 * obecna predkosc pojazdu zwrocona przez GPS
	 */
	private double speed;
	/**
	 * ostatnia odczytana przedkosc
	 */
	private double PrevSpeed;
	
	//----accelerometer----//
	/**
	 * Czy uzytkownik chce i moze uzywac akcelerometru
	 */
	private boolean isAccel;
	/**
	 *  Flaga ustawiana na true jesli akcelerometr wykryje wypadek
	 */
	private boolean accDet = false;
	/**
	 * parametr czulosci akcelerometru podawany w konstruktorze
	 */
	private volatile int paramAccel;
	/**
	 * manager obslugi akcelerometru
	 */
	private SensorManager Sm;
	/**
	 * sluchacz stanu akcelerometru
	 */
	private SensorEventListener SEL;
	/**
	 * zmienna ostawiona na false zeby przy pierwszym odczycie
	 * danych z akcelerometra wyniki zapisac jako poprzednie wartosci
	 * potem juz zmienna ma wartosc true
	 */
	private boolean AccelFlag =false;
	/**
	 * obecny stan akcelerometru wzgledem osi X
	 */
	private double currX;
	/**
	 * obecny stan akcelerometru wzgledem osi Y
	 */
	private double currY;
	/**
	 * obecny stan akcelerometru wzgledem osi Z
	 */
	private double currZ;
	/**
	 * ostatni odczytany stan akcelerometru wzgledem osi X
	 */
	private double prevX;
	/**
	 * ostatni odczytany stan akcelerometru wzgledem osi Y
	 */
	private double prevY;
	/**
	 * ostatni odczytany stan akcelerometru wzgledem osi Z
	 */
	private double prevZ;
	
	/**
	 * Glowny konstruktor klasy
	 * @param a obecnie aktywne Activity
	 * @param GPS true jesli uzytkownik chce wykrywac wypadki przy pomocu GPS false jesli nie chce
	 * @param parGPS parametr czulosci modulu GPS
	 * @param Accel true jesli uzytkownik chce wykrywac wypadki przy pomocu akcelerometru false jesli nie chce
	 * @param parAccel parametr czulosci akcelerometru
	 */
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
	
	/**
	 * Metoda wywolywana zawsze gdy akcelerometr wykryje wypadek
	 * Tworzy nowy watek czekajacy 4 sekundy po czym ustawiajacy 
	 * flage wykrycia wypadku przez akcelerometr znowu na false
	 */
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
	
	/**
	 * Metoda wywolywana zawsze gdy GPS wykryje wypadek
	 * Tworzy nowy watek czekajacy 4 sekundy po czym ustawiajacy 
	 * flage wykrycia wypadku przez GPS znowu na false
	 */
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
	
	/**
	 * Inicjacja akcelerometru oraz jego obslugi
	 * Metoda wywolywana przez konstruktor jesli uzytkownik chce uzwac akcelerometru
	 */
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
						//check();//1 warunek spelniony
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
	
	/**
	 * Inicjacja GPS oraz jego obslugi
	 * Metoda wywolywana przez konstruktor jesli uzytkownik chce uzwac GPS
	 */
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
	
	/**
	 * Zmiana parametru czulosci GPS
	 * @param val parametr czulosci jaki chcemy ustawic
	 */
	public void setGpsParam(int val){
		if(isGPS)
			paramGPS = val;
		//else
			//throw new Exception("Wrong Parameter");
	}
	
	/**
	 * Zmiana parametru czulosci akcelerometru
	 * @param val parametr czulosci jaki chcemy ustawic
	 */
	public void setAccParam(int val){
		if(isAccel)
			paramAccel = val;
		//else
			//throw new Exception("Wrong Parameter");
	}
	
	/*private void check(){
		if((accDet || !isAccel) && (gpsDet || !isGPS)){
			((MainActivity)act).setAsCrash();
		}
	}*/
	
	/**
	 * Metoda pytajaca detektor czy nastapil wypadek
	 * @return true jasli detektor wykryl wypadek false otherwise 
	 */
	public boolean wasAccident(){
		if((accDet || !isAccel) && (gpsDet || !isGPS))
			return true;
		return false;
	}
}