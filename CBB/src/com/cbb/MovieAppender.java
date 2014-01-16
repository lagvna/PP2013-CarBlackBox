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

/**
 * Klasa sluzaca do zlaczenia dwoch filmow w calosc oraz dociecia ich do odpowiedniej dlugosci.
 * Wszystko odbywa sie jako zdarzenie asynchroniczne.
 */

public class MovieAppender extends AsyncTask<Void, Void, Void>	{
	
	Activity fatherActivity;
	boolean isInverted;
	Movie[] inMovies;
		  
	public MovieAppender(Activity fatherActivity, boolean isInverted) {
		this.fatherActivity = fatherActivity;
		this.isInverted = isInverted;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPreExecute() {
	    fatherActivity.showDialog(RecActivity.PLEASE_WAIT_DIALOG);
	}
	 
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
	 
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Void result) {
	    fatherActivity.removeDialog(RecActivity.PLEASE_WAIT_DIALOG);
	    Toast.makeText(fatherActivity, "Utworzono nagranie!", Toast.LENGTH_SHORT).show();
	}
	
    public void appendMovies() throws IOException {


        String f1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/cbb1.mp4";
        String f2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/cbb2.mp4";

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

        FileChannel fc = new RandomAccessFile(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb3.mp4"), "rw").getChannel();
        out.writeContainer(fc);
        fc.close();
    }
    
    public void trimMovies() throws IOException	{
    	Movie movie = MovieCreator.build(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb3.mp4"));

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        
        double duration = getDuration(String.format(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb3.mp4"));
        
        System.err.println("Dlugość filmu: " + duration);
        
        double endTime1 = duration;
        double startTime1 = duration - 30;

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