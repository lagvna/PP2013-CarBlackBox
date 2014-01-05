package com.cbb;

import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa sluzaca do zlaczenia dwoch filmow w calosc. Trzeba jeszcze dodac docinanie do minuty/czasu okreslonego w settingsach
 */
public class MovieAppender {
    public static void golo() throws IOException {


        String f1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb1.mp4";
        String f2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/cbb2.mp4";

        Movie[] inMovies = new Movie[]{
                MovieCreator.build(f1),
                MovieCreator.build(f2)};

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
}