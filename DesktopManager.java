

/**
 * Class DesktopManager
 */
public class DesktopManager {

  //
  // Fields
  //

  public GUI gui;
  public XML settings;
  
  //
  // Constructors
  //
  public DesktopManager () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of gui
   * @param newVar the new value of gui
   */
  public void setGui ( GUI newVar ) {
    gui = newVar;
  }

  /**
   * Get the value of gui
   * @return the value of gui
   */
  public GUI getGui ( ) {
    return gui;
  }

  /**
   * Set the value of settings
   * @param newVar the new value of settings
   */
  public void setSettings ( XML newVar ) {
    settings = newVar;
  }

  /**
   * Get the value of settings
   * @return the value of settings
   */
  public XML getSettings ( ) {
    return settings;
  }

  //
  // Other methods
  //

  /**
   * @return       File[*]
   */
  public File[*] getFilm(  )
  {
  }


  /**
   * @return       File
   */
  public File mergeFilm(  )
  {
  }


}
