package settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;

//import com.thoughtworks.xstream.XStream;
import javax.xml.*;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;



public class SaveOpenSettings {
	
	private static final String settingXML = "/data/data/com.cbb/settings.xml";
	
	
	public static void save(Settings s){
		File f = new File(settingXML);
		
		Serializer serializer = new Persister();
		try {
			serializer.write(s, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static Settings open() throws Exception{
		Serializer serializer = new Persister();
		Settings example = serializer.read(Settings.class, new File(settingXML));
		return example;
	}
	
	public static void defaultSettings(Context context){
		if(new File(settingXML).exists())
			return;
		boolean isGps = true;
		boolean isAcc = true;
		final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		if(mgr == null)
			isGps = false;
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accelerometerSensor == null)
			isAcc = false;
		
		save(new Settings(20, 50, isAcc, isGps, 0, 60, isAcc, isGps));
	}

}