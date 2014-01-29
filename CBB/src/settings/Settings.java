package settings;

import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Klasa ustawien
 * obiekty tej klasy sluza do przerabiania na XML i zapisania ustawien
 * sluza rowniez gdy chcemy odczytac zapisane ustawienia 
 * @author Lisu
 *
 */
@Root
public class Settings {
	/**
	 * Czulosc akcelerometru
	 */
	@Element
	private int AccelerometerSensivity;
	/**
	 * Czulosc modulu GPS
	 */
	@Element
	private int GpsSensivity;
	/**
	 * Czy uzytkownik chce uzywac akcelerometru do wykrywania wypadku
	 */
	@Element
	private boolean isAccel;
	/**
	 * Czy uzytkownik chce uzywac GPS do wykrywania wypadku
	 */
	@Element
	private boolean isGPS;
	/**
	 * numer kontaktowy
	 */
	@Element
	private int conntactNumber;
	/**
	 * dlugosc filmu
	 */
	@Element
	private int filmLength;
	/**
	 * czy urzadzenie posiada akcelerometr
	 */
	@Element
	private boolean deviceHasAccelerometer;
	/**
	 * czy urzadzenie posiada GPS
	 */
	@Element
	private boolean deviceHasGPS;
	
	public Settings(){}
	
	/**
	 * Konstruktor ustawien	
	 * @param AccelerometerSensivity czulosc akceleromtru
	 * @param GpsSensivity czulosc GPS
	 * @param isAccel czy user chce uzywac akceleromtru
	 * @param isGPS czy user chce uzywac gps
	 * @param connatactNumber numer kontaktory usera
	 * @param filmLength dlugosc filmu
	 * @param deviceHasAccelerometer czy urzadzenie posiada akcelerometr
	 * @param deviceHasGPS czy urzadzenie posiada gps
	 */
	public Settings( int AccelerometerSensivity, int GpsSensivity, 
			 boolean isAccel, boolean isGPS,
			 int connatactNumber, int filmLength, boolean deviceHasAccelerometer, boolean deviceHasGPS){
		this.AccelerometerSensivity = AccelerometerSensivity;
		this.GpsSensivity  = GpsSensivity;
		this.isAccel = isAccel;
		this.isGPS = isGPS;
		this.filmLength = filmLength;
		this.conntactNumber = connatactNumber;
		this.deviceHasAccelerometer= deviceHasAccelerometer;
		this.deviceHasGPS = deviceHasGPS;
	}
	
	/**
	 * Metoda zwracajaca czulosc GPS
	 * @return parametr czulosci GPS
	 */
	public int getGpsSensivity() {
		return GpsSensivity;
	}
	
	/**
	 * Metoda ustawiajaca czulosc modulu GPS
	 * @param gpsSensivity parametr czulosci
	 */
	public void setGpsSensivity(int gpsSensivity) {
		GpsSensivity = gpsSensivity;
	}
	
	/**
	 * Metoda zwracajaca czulosc akcelerometru
	 * @return parametr czulosc akcelerometru
	 */
	public int getAccelerometerSensivity() {
		return AccelerometerSensivity;
	}
	
	/**
	 * Metoda ustawiajaca czulosc akcelerometru
	 * @param accelerometerSensivity parametr czulosc akcelerometru
	 */
	public void setAccelerometerSensivity(int accelerometerSensivity) {
		AccelerometerSensivity = accelerometerSensivity;
	}
	
	/**
	 * Metoda mowiaca o tym czy uzytkownik chce uzywac akcelerometru
	 * @return true jesli user chce uzywac akcelerometru false otherwise
	 */
	public boolean isAccel() {
		return isAccel;
	}
	
	/**
	 * Metoda ustawiajaca wybor uzytkownika czy chce wuywac akcelerometru
	 * @param isAccel true jesli user chce uzywac akcelerometru false otherwise
	 */
	public void setAccel(boolean isAccel) {
		this.isAccel = isAccel;
	}
	
	/**
	 * Metoda mowiaca o tym czu uzytkownik chce uzywac modulu GPS
	 * @return true jesli user chce uzywac GPS false otherwise
	 */
	public boolean isGPS() {
		return isGPS;
	}
	
	/**
	 * Metoda ustawiajaca wybor uzytkownika czy chce uzywac GPS
	 * @param isGPS true jesli user chce uzywac GPS false otherwise
	 */
	public void setGPS(boolean isGPS) {
		this.isGPS = isGPS;
	}
	
	/**
	 * Metoda zwracajaca numer kontaktowy uzytkownika
	 * @return numrk kontaktowy uzytkownika
	 */
	public int getConntactNumber() {
		return conntactNumber;
	}
	
	/**
	 * Metoda ustawiajaca nume kontaktowy uzytkownika
	 * @param conntactNumber numer kontaktowy do ustawienia
	 */
	public void setConntactNumber(int conntactNumber) {
		this.conntactNumber = conntactNumber;
	}

	/**
	 * Metoda zwracajaca ustawiona dlugosc filmu
	 * @return dllugosc filmu wsekundach
	 */
	public int getFilmLength() {
		return filmLength;
	}

	/**
	 * Metoda ustawiajaca dlugosc filmu
	 * @param filmLength parametr dlugosci filmu w sekundach
	 */
	public void setFilmLength(int filmLength) {
		this.filmLength = filmLength;
	}
	
	/**
	 * Zwraca obecny stan obiektu przykladowe pola w fomie stringu
	 * bylo przydatne przy tesotwaniu zostawione na wszelki wypadek
	 */
	@Override
	public String toString(){
		return ""+getAccelerometerSensivity()+" "+getConntactNumber()+" "+getFilmLength()+" "+getGpsSensivity()+" "+isAccel;
	}

	/**
	 * Czy urzadzenia posiada akcelrometr
	 * @return true jesli urzadzenie posiada akcelrometr false otherwise
	 */
	public boolean isDeviceHasAccelerometer() {
		return deviceHasAccelerometer;
	}

	/**
	 * Metoda ustawiajaca czy urzadzenie ma akcelerometr
	 * @param deviceHasAccelerometer true jsli ma false otherwise
	 */
	public void setDeviceHasAccelerometer(boolean deviceHasAccelerometer) {
		this.deviceHasAccelerometer = deviceHasAccelerometer;
	}

	/**
	 * Metoda ustwiajaca czy urzadzenie posiada GPS
	 * @return true jesli urzadzenie posiada GPS
	 */
	public boolean isDeviceHasGPS() {
		return deviceHasGPS;
	}
	
	/**
	 * Ustawienie czy urzadzenie posiada modul GPS
	 * @param deviceHasGPS true jesli urzadzenie posiada modul GPS false otherwise
	 */
	public void setDeviceHasGPS(boolean deviceHasGPS) {
		this.deviceHasGPS = deviceHasGPS;
	}
}