package com.cbb;

/**
 * Klasa niestandardowego rekordu dla ListView.
 * Obiekt definiuje jak wyglada rekord na liscie.
 * @author lagvna
 *
 */
public class CustomRow {
	/** ID ikony */
	public int icon;
	/** Tytul rekordu*/
	public String title;
	/** Data stworzenia pliku/rekordu */
	public String date;
	
	/**Pusty konstruktor*/
	public CustomRow()	{
		
	}
	/**
	 * Glowny konstruktor rekordu
	 * 
	 * @param icon id ikony
	 * @param title nazwa pliku
	 * @param data stworzenia pliku
	 * */
	public CustomRow(int icon, String title, String date)	{
		this.icon = icon;
		this.title = title;
		this.date = date;
	}
}