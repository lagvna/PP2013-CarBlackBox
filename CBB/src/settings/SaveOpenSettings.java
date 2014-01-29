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


/**
 * Statyczna klasa niewymagajaca obiektow sluzaca do zapsywania ustawien
 * **Aby zapisac ustawienia potrzebujemy obiekt klasy Settings w nim ustawiamy dopowiednie ustawienia 
 * i wywolujemy metode save
 * **Aby odczytac ustawienia wywolujemy metode open metoda zwraca obiekt klasy Settings, 
 * z ktorego mozemy odczytac informacje
 * @author Lisu
 *
 */
public class SaveOpenSettings {
	/**
	 * Sciezka dostepu do pliku z ustawieniami
	 */
	private static final String settingXML = "/data/data/com.cbb/settings.xml";
	
	/**
	 * Metoda zapisujaca ustawienia na dysku
	 * @param s obiekt z ustawieniami ktore chcmey zapisac
	 */
	public static void save(Settings s){
		File f = new File(settingXML);
		Log.d("chuj","chujowy");
		Serializer serializer = new Persister();
		try {
			serializer.write(s, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("chuj2","chujowy");
	}
	
	/**
	 * Metoda odczytujaca zapisane ustawienia
	 * @return obiekt klasy Settings zawierajacy biezace ustawienia
	 * @throws Exception gdy nie uda otworzyc pliku z ustawieniami
	 */
	public static Settings open() throws Exception{
		Serializer serializer = new Persister();
		Log.d("chuj","chujowy");
		Settings example = serializer.read(Settings.class, new File(settingXML));
		Log.d("chuj","chujowy");
		return example;
	}
	
	/**
	 * Metoda wywolywana przy kazdym starcie programu jednak jedyna akcje 
	 * wykonuje gdy program jest uruchamiany pierwszy raz i nie posiada jeszcze zadnych ustawien
	 * powstaje wtedy plik z domyslnymi ustawieniami zalecanymi przez programiste
	 * @param context aktualny kontekst
	 */
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