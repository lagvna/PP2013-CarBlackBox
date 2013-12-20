
import java.util.*;


/**
 * Class MainMenu
 */
public class MainMenu {

  //
  // Fields
  //

  public SettingsActivity user;
  public XML savedSettings = null;
  
  //
  // Constructors
  //
  public MainMenu () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of user
   * @param newVar the new value of user
   */
  public void setUser ( SettingsActivity newVar ) {
    user = newVar;
  }

  /**
   * Get the value of user
   * @return the value of user
   */
  public SettingsActivity getUser ( ) {
    return user;
  }

  /**
   * Set the value of savedSettings
   * @param newVar the new value of savedSettings
   */
  public void setSavedSettings ( XML newVar ) {
    savedSettings = newVar;
  }

  /**
   * Get the value of savedSettings
   * @return the value of savedSettings
   */
  public XML getSavedSettings ( ) {
    return savedSettings;
  }

  //
  // Other methods
  //

  /**
   */
  public void startRec(  )
  {
  }


  /**
   * @return       SettingsActivity
   */
  public SettingsActivity setup(  )
  {
  }


  /**
   */
  public void _create_MainMenu(  )
  {
  }


}
