package settings;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class Settings {
	/**
	 * 
	 */
	@Element
	private int AccelerometerSensivity;
	@Element
	private int GpsSensivity;
	@Element
	private boolean isAccel;
	@Element
	private boolean isGPS;
	@Element
	private int conntactNumber;
	@Element
	private int filmLength;
	@Element
	private boolean deviceHasAccelerometer;
	@Element
	private boolean deviceHasGPS;
	
	public Settings(){}
	
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
	
	public int getGpsSensivity() {
		return GpsSensivity;
	}
	
	public void setGpsSensivity(int gpsSensivity) {
		GpsSensivity = gpsSensivity;
	}
	
	public int getAccelerometerSensivity() {
		return AccelerometerSensivity;
	}
	
	public void setAccelerometerSensivity(int accelerometerSensivity) {
		AccelerometerSensivity = accelerometerSensivity;
	}
	
	public boolean isAccel() {
		return isAccel;
	}
	
	public void setAccel(boolean isAccel) {
		this.isAccel = isAccel;
	}
	
	public boolean isGPS() {
		return isGPS;
	}
	
	public void setGPS(boolean isGPS) {
		this.isGPS = isGPS;
	}
	public int getConntactNumber() {
		return conntactNumber;
	}

	public void setConntactNumber(int conntactNumber) {
		this.conntactNumber = conntactNumber;
	}

	public int getFilmLength() {
		return filmLength;
	}

	public void setFilmLength(int filmLength) {
		this.filmLength = filmLength;
	}
	
	@Override
	public String toString(){
		return ""+getAccelerometerSensivity()+" "+getConntactNumber()+" "+getFilmLength()+" "+getGpsSensivity()+" "+isAccel();
	}

	public boolean isDeviceHasAccelerometer() {
		return deviceHasAccelerometer;
	}

	public void setDeviceHasAccelerometer(boolean deviceHasAccelerometer) {
		this.deviceHasAccelerometer = deviceHasAccelerometer;
	}

	public boolean isDeviceHasGPS() {
		return deviceHasGPS;
	}

	public void setDeviceHasGPS(boolean deviceHasGPS) {
		this.deviceHasGPS = deviceHasGPS;
	}
}