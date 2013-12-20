
import java.util.*;


/**
 * Class CrashListener
 */
public class CrashListener {

  //
  // Fields
  //

  public AccelometerSensorModule accelerometer;
  public GPS_Module GPS;
  
  //
  // Constructors
  //
  public CrashListener () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of accelerometer
   * @param newVar the new value of accelerometer
   */
  public void setAccelerometer ( AccelometerSensorModule newVar ) {
    accelerometer = newVar;
  }

  /**
   * Get the value of accelerometer
   * @return the value of accelerometer
   */
  public AccelometerSensorModule getAccelerometer ( ) {
    return accelerometer;
  }

  /**
   * Set the value of GPS
   * @param newVar the new value of GPS
   */
  public void setGPS ( GPS_Module newVar ) {
    GPS = newVar;
  }

  /**
   * Get the value of GPS
   * @return the value of GPS
   */
  public GPS_Module getGPS ( ) {
    return GPS;
  }

  //
  // Other methods
  //

  /**
   * @return       boolean
   */
  public boolean wasAccident(  )
  {
  }


  /**
   * @return       int
   */
  public int interpetAccelerationResults(  )
  {
  }


  /**
   * @return       int
   */
  public int interpretSpeedResult(  )
  {
  }


  /**
   */
  public void _create_CrashListener(  )
  {
  }


}
