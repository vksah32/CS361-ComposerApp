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

package proj8ZhouRinkerSahChistolini.Models;

import javafx.beans.property.*;

/**
 * Holds all the information of a note
 */
public class Note {

    /** the instrument that the note plays */
    private Instrument instrument;
    /** the duration of the note */
    private DoubleProperty duration;
    /** the pitch of the note */
    private DoubleProperty pitch;
    /** the start tick of the note */
    private DoubleProperty startTick;
    /**selectedproperty to track selectionOfrectangle**/
    private BooleanProperty selected;

    /**
     * The constructor for Note
     */
    public Note(Instrument instrument){
        this.startTick = new SimpleDoubleProperty();
        this.duration = new SimpleDoubleProperty();
        this.pitch = new SimpleDoubleProperty();
        this.selected = new SimpleBooleanProperty();
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
     * the pitch at which this note will be played
     * @return the pitch of this note
     */
    public int getPitch() {
        return 127-this.pitchProperty().intValue()/10;
    }


    /**
     * the tick at which this note will start playing
     * @return the starting tick
     */
    public int getStartTick() {
        return this.startTickProperty().intValue();
    }

    /**
     * Gets the duration property
     * @return DoubleProperty duration
     */
    public DoubleProperty durationProperty() {
        return duration;
    }

    /**
     * Gets the pitch property
     * @return DoubleProperty pitch
     */
    public DoubleProperty pitchProperty() {
        return pitch;
    }

    /**
     * Gets the startTick property
     * @return DoubleProperty startTick
     */
    public DoubleProperty startTickProperty() {
        return startTick;
    }

    /**
     * getter for selectedProperty
     * @return BooleanProperty  the selectedProperty
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * x,y,width, name, channel, integer representing MIDI instrucment
     * @return
     */
    public String toString() {

        String noteString = this.startTick.intValue()    +" "+
                            this.pitchProperty().intValue() +" "+
                            this.duration.getValue()     +" " +
                            this.instrument.getName()    + " " +
                            this.instrument.getChannel() + " " +
                            this.instrument.getValue()   +"\n";

        return noteString;
    }
}