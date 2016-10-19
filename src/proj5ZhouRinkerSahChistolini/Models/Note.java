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

/**
 * Holds all the information of a note
 */
public class Note {

    /** the instrument number */
    private int instrument;
    /** the duration of the note */
    private int duration;
    /** the pitch of the note */
    private int pitch;
    /** the start tick of the note */
    private int startTick;
    /** sets the default track to 0 */
    private final int TRACK=0;
    /** sets the default channel to 0 */
    private final int CHANNEL=0;

    /**
     * The constructor for Note
     * @param startTick
     * @param duration
     * @param pitch
     */
    public Note(int startTick, int duration, int pitch){
        this.startTick = startTick;
        this.duration = duration;
        this.pitch = pitch;
    }

    /**
     * gets the note's instrument
     * @return this note's instrument
     */
    public int getInstrument() {
        return instrument;
    }

    /**
     * Sets the instrument this note will be played with
     * @param instrument the instrument that this note will be played with
     */
    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    /**
     * gets the duration of the note
     * @return the duration of the note
     */
    public int getDuration() {
        return duration;
    }

    /**
     * sets how long this note will play for
     * @param duration how long this note will play
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * the pitch at which this note will be played
     * @return the pitch of this note
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * sets the pitch at which this note will be played
     * @param pitch the pitch at which this note will be played
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    /**
     * the tick at which this note will start playing
     * @return the starting tick
     */

    public int getStartTick() {
        return startTick;
    }

    /**
     * sets the tick at which this note will first be played
     * @param startTick the starting tick
     */
    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }
}
