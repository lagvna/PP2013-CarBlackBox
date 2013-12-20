

/**
 * Class GUI
 */
public class GUI {

  //
  // Fields
  //

  public Buttons[*] buttons;
  public JPanel[*] panels;
  public Listeners[*] listeners;
  
  //
  // Constructors
  //
  public GUI () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of buttons
   * @param newVar the new value of buttons
   */
  public void setButtons ( Buttons[*] newVar ) {
    buttons = newVar;
  }

  /**
   * Get the value of buttons
   * @return the value of buttons
   */
  public Buttons[*] getButtons ( ) {
    return buttons;
  }

  /**
   * Set the value of panels
   * @param newVar the new value of panels
   */
  public void setPanels ( JPanel[*] newVar ) {
    panels = newVar;
  }

  /**
   * Get the value of panels
   * @return the value of panels
   */
  public JPanel[*] getPanels ( ) {
    return panels;
  }

  /**
   * Set the value of listeners
   * @param newVar the new value of listeners
   */
  public void setListeners ( Listeners[*] newVar ) {
    listeners = newVar;
  }

  /**
   * Get the value of listeners
   * @return the value of listeners
   */
  public Listeners[*] getListeners ( ) {
    return listeners;
  }

  //
  // Other methods
  //

  /**
   */
  public void _create_GUI(  )
  {
  }


}
