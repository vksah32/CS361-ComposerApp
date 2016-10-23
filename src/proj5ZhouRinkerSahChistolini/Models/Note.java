/**
 * File: Note.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini.Models;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Holds all the information of a note
 */
public class Note {

    /** the instrument number */
    private Instrument instrument;
    /** the duration of the note */
    private DoubleProperty duration;
    /** the pitch of the note */
    private DoubleProperty pitch;



    /** the start tick of the note */
    private DoubleProperty startTick;
    /** sets the default track to 0 */
    private final int TRACK=0;
    /** sets the default channel to 0 */
    private final int CHANNEL=0;



    /**
     * The constructor for Note

     */
    public Note(int instrument){
        this.startTick = new SimpleDoubleProperty();
        this.duration = new SimpleDoubleProperty();
        this.pitch = new SimpleDoubleProperty();
        this.instrument = instrument;

    }

    /**
     * gets the note's instrument
     * @return this note's instrument
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Sets the instrument this note will be played with
     * @param instrument the instrument that this note will be played with
     */
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    /**
     * gets the duration of the note
     * @return the duration of the note
     */
    public int getDuration() {
        return this.durationProperty().intValue();
    }

    /**
     * sets how long this note will play for
     * @param duration how long this note will play
     */
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }

    /**
     * the pitch at which this note will be played
     * @return the pitch of this note
     */
    public int getPitch() {
        return 127-this.pitchProperty().intValue();
    }

    /**
     * sets the pitch at which this note will be played
     * @param pitch the pitch at which this note will be played
     */
//    public void setPitch(int pitch) {
//        this.pitch = pitch;
//    }

    /**
     * the tick at which this note will start playing
     * @return the starting tick
     */
//
    public int getStartTick() {
        return this.startTickProperty().intValue();
    }
//
//    /**
//     * sets the tick at which this note will first be played
//     * @param startTick the starting tick
//     */
//    public void setStartTick(int startTick) {
//        this.startTick = startTick;
//    }

    public DoubleProperty durationProperty() {
        return duration;
    }

    public DoubleProperty pitchProperty() {
        return pitch;
    }

    public DoubleProperty startTickProperty() {
        return startTick;
    }
}
