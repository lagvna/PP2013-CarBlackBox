
import java.util.*;


/**
 * Class SettingsActivity
 */
public class SettingsActivity {

  //
  // Fields
  //

  public String accidentMessage = "I'm having an accident...";
  public int contactNumber;
  public int emergencyNumber = 911;
  public int insurerNumber;
  public YouTubeAccount youtube;
  
  //
  // Constructors
  //
  public SettingsActivity () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of accidentMessage
   * @param newVar the new value of accidentMessage
   */
  public void setAccidentMessage ( String newVar ) {
    accidentMessage = newVar;
  }

  /**
   * Get the value of accidentMessage
   * @return the value of accidentMessage
   */
  public String getAccidentMessage ( ) {
    return accidentMessage;
  }

  /**
   * Set the value of contactNumber
   * @param newVar the new value of contactNumber
   */
  public void setContactNumber ( int newVar ) {
    contactNumber = newVar;
  }

  /**
   * Get the value of contactNumber
   * @return the value of contactNumber
   */
  public int getContactNumber ( ) {
    return contactNumber;
  }

  /**
   * Set the value of emergencyNumber
   * @param newVar the new value of emergencyNumber
   */
  public void setEmergencyNumber ( int newVar ) {
    emergencyNumber = newVar;
  }

  /**
   * Get the value of emergencyNumber
   * @return the value of emergencyNumber
   */
  public int getEmergencyNumber ( ) {
    return emergencyNumber;
  }

  /**
   * Set the value of insurerNumber
   * @param newVar the new value of insurerNumber
   */
  public void setInsurerNumber ( int newVar ) {
    insurerNumber = newVar;
  }

  /**
   * Get the value of insurerNumber
   * @return the value of insurerNumber
   */
  public int getInsurerNumber ( ) {
    return insurerNumber;
  }

  /**
   * Set the value of youtube
   * @param newVar the new value of youtube
   */
  public void setYoutube ( YouTubeAccount newVar ) {
    youtube = newVar;
  }

  /**
   * Get the value of youtube
   * @return the value of youtube
   */
  public YouTubeAccount getYoutube ( ) {
    return youtube;
  }

  //
  // Other methods
  //

  /**
   */
  public void callForHelp(  )
  {
  }


  /**
   * @return       int
   */
  public int getInsurerNumber(  )
  {
  }


  /**
   */
  public void _create_SettingActivity(  )
  {
  }


}
