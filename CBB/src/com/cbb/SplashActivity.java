 /*! \mainpage Car Black Box v1.0 strona główna...
 *
 * Program zrealizowany na potrzeby kursu Projekt Programistyczny 2013/2014. 
 * 
 * Dotyczy zagadnienia calosciowej realizacji aplikacji na system Android.
 * 
 * Jest to aplikacja na system Android, która działa na zasadzie czarnej skrzynki samochodu. Telefon
 * zawieszony na podstawce przytwierdzonej do szyby samochodu (podobnie jak nawigacja GPS)
 * rejestruje drogę przebytą przez samochód. W momencie wystąpienia nieoczekiwanego wydarzenia
 * jak stłuczka/wypadek/najechanie na sporą dziurę, zapisany zostaje ostatni kawałek nagrania.
 * 
 * Rejestratory drogi są obecnie szeroko dostępne w sprzedaży, jednak wymaga to dodatkowych
 * kosztów związanych z zakupem. Posiadając telefon z systemem operacyjnym Android można
 * skorzystać z funkcjonalności jego sensorów i dzięki temu wykorzystać go w dokładnie taki sam
 * sposób.
 * 
 * Algorytm rejestrowania trasy przebiega nastepujaco:
 * Tworzony jest nowy watek, ktory nagrywa na zmiane plik tmp1 oraz tmp2.
 * W miedzyczasie przechowywana jest informacja o tym, ktory plik jest aktualnie nagrywany.
 * W przypadku nieoczekiwanego zdarzenia, lub zakonczenia nagrywania przez uzytkownika
 * oba pliki sa przekazywane do klasy MovieAppender, gdzie nagranie jest laczone oraz 
 * przycinane do odpowiedniej dlugosci.
 * 
 * Dzieki temu urzadzenie zawsze pamieta na tyle materialu filmowego, aby stworzyc z tego nagranie
 * dlugoscia zgodne z ustawieniami, ale tez rozsadnie zarzadza pamiecia, aby nie zapelnic jej w calosci.
 */
package com.cbb;

import settings.SaveOpenSettings;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

/**
* Splash screen aplikacji.
* Wyswietla logo aplikacji, ladujac w miedzyczasie ustawienia domyslne, jesli
* jest taka potrzeba.
*
* @author lagvna
*/
public class SplashActivity extends Activity {	
	@Override
	/**
	 * Glowny konstruktor aktywnosci.
	 * Tworzy widok, sciaga ustawienia domyslne.
	 * Uruchamia watek trwajacy dwie sekundy, aby wyswietlic logo aplikacji.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		
		SaveOpenSettings.defaultSettings(getApplicationContext());
		
		/**
		 * Watek sluzacy przetrzymaniu splash screena przez dwie sekundy.
		 */
		final Thread mainThread = new Thread() {
	        @Override
	        public void run() {
	        	try	{
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        
	        	Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	        	SplashActivity.this.startActivity(intent);
	        	SplashActivity.this.finish();
	        }
		};

	    mainThread.start();
	}

	@Override
    public void onDestroy(){
    	super.onDestroy();
    }
}