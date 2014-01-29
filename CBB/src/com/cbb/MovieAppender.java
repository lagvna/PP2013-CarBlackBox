package com.cbb;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.coremedia.iso.boxes.TimeToSampleBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.RandomAccessFile;

import settings.SaveOpenSettings;
import settings.Settings;

/**
 * Klasa sluzaca do zlaczenia dwoch filmow w calosc oraz dociecia ich do odpowiedniej dlugosci.
 * Wszystko odbywa sie jako zdarzenie asynchroniczne.
 */

public class MovieAppender extends AsyncTask<Void, Void, Void>	{
	/**Nadaktywnosc, do ktorej ma zostac zwrocone dzialanie przycinania filmow*/
	Activity fatherActivity;
	/**Zmienna mowiaca, czy algorytm powinien laczyc filmy w naturalnej czy tez odwroconej kolejnosci*/
	boolean isInverted;
	/**Tablica kawalkow nagrac do zlaczenia i przyciecia*/
	Movie[] inMovies;
	/**Obiekt przechowujacy informacje z ustawien, w szczegolnosci pobiera informacje o zadanej dlugosci nagrania*/
	Settings s;
		  
	/**
	 * Glowny konstruktor klasy
	 * Pobiera ustawienia aplikacji.
	 * @param fatherActivity informacja o nadaktywnosci, tj. gdzie ma zostac zwrocony rezultat dialogu
	 * @param isInverted informacja czy laczyc filmy w naturalnej czy odwroconej kolejnosci
	 */
	public MovieAppender(Activity fatherActivity, boolean isInverted) {
		this.fatherActivity = fatherActivity;
		this.isInverted = isInverted;
		try {
			s = SaveOpenSettings.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda wywolujaca dialog proszacy o poczekanie az stworzy sie i zapisze gotowe
	 * nagranie w nadaktywnosci.
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute() {
	    fatherActivity.showDialog(RecActivity.PLEASE_WAIT_DIALOG);
	}
	 
	/**
	 * Metoda okreslajaca co sie dzieje w tle dialogu.
	 * Dwa nagrania sa laczone ze soba, a nastepnie docinane do odpowiedniej dlugosci.
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
	    try {
	    	appendMovies();
	    	trimMovies();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	 /**
	  * Metoda okreslajaca co sie stanie po zakonczeniu dialogu.
	  * Wywolany zostanie toast w nadaktywnosci, mowiacy o tym, ze nagranie sie powiodlo
	  * i zostalo zapisane.
	  */
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Void result) {
	    fatherActivity.removeDialog(RecActivity.PLEASE_WAIT_DIALOG);
	    Toast.makeText(fatherActivity, "Utworzono nagranie!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Metoda sluzaca do zlaczenia dwoch nagran w calosc.
	 * W wyniku otrzymujemy plik tmpcbb3.mp4, ktory nastepnie poddawany jest przycieciu
	 * do zadanej dlugosci.
	 * @throws IOException wyjatek w przypadku braku/bledu pliku.
	 */
    public void appendMovies() throws IOException {


        String f1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/tmpcbb1.mp4";
        String f2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/tmpcbb2.mp4";

        System.out.println("Plik1: "+f1);
        System.out.println("Plik1: "+f2);
        
        if(isInverted)	{
        	inMovies = new Movie[]{
                    MovieCreator.build(f1),
                    MovieCreator.build(f2)};
        }
        else	{
        	inMovies = new Movie[]{
                    MovieCreator.build(f2),
                    MovieCreator.build(f1)};
        }

        List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();

        for (Movie m : inMovies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                	videoTracks.add(t);
                }
            }
        }

        Movie result = new Movie();

        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }

        Container out = new DefaultMp4Builder().build(result);

        FileChannel fc = new RandomAccessFile(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/tmpcbb3.mp4"), "rw").getChannel();
        out.writeContainer(fc);
        fc.close();
    }
    
    /**
     * Metoda sluzaca obcieciu nagrania przygotowanego przez metode appendMovies, do zadanej dlugosci
     * @throws IOException wyjatek w przypadku bledu/braku pliku
     */
    public void trimMovies() throws IOException	{
    	Movie movie = MovieCreator.build(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/tmpcbb3.mp4"));

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        
        double duration = getDuration(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/tmpcbb3.mp4"));
        int length = s.getFilmLength();
        
        System.err.println("Dlugość filmu: " + duration);
        
        double endTime1 = duration;
        double startTime1 = duration - length;

        boolean timeCorrected = false;

        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                startTime1 = correctTimeToSyncSample(track, startTime1, false);
                endTime1 = correctTimeToSyncSample(track, endTime1, true);

                timeCorrected = true;
            }
        }

        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            double lastTime = 0;
            long startSample1 = -1;
            long endSample1 = -1;

            for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
                TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
                for (int j = 0; j < entry.getCount(); j++) {


                    if (currentTime > lastTime && currentTime <= startTime1) {
                        startSample1 = currentSample;
                    }
                    if (currentTime > lastTime && currentTime <= endTime1) {
                        endSample1= currentSample;
                    }
                    lastTime = currentTime;
                    currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                    currentSample++;
                }
            }
            movie.addTrack(new AppendTrack(new CroppedTrack(track, startSample1, endSample1)));
        }
        
        Container out = new DefaultMp4Builder().build(movie);
        File dst = File.createTempFile("cbb", ".mp4", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        String title = dst.getName();
        System.out.println(title);
        FileOutputStream fos = new FileOutputStream(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+title));
        FileChannel fc = fos.getChannel();
        out.writeContainer(fc);

        fc.close();
        fos.close();
    }
    
    protected double getDuration(String filename) throws IOException	{
        IsoFile isoFile = new IsoFile(filename);
        
        double lengthInSeconds = (double)
                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
        isoFile.close();
        
        return lengthInSeconds;
    }
    /**
     * Metoda synchronizujaca dzwiek z filmem. Jej wywolanie jest niezbedne, szczegolnie jesli filmy docinamy
     * w niestandardowych czasach (tj. np. nie poczatek z koncem)
     * @param track aktualna sciezka
     * @param cutHere sekunda dociecia
     * @param next czy istnieje nastepna sciezka
     * @return zwraca czasy dostosowujace dzwiek do filmu
     */
    private double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
            TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
            for (int j = 0; j < entry.getCount(); j++) {
                if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                    timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
                }
                currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}