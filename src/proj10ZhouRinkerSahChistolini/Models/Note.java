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

package proj10ZhouRinkerSahChistolini.Models;

import javafx.beans.property.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Holds all the information of a note
 */
public class Note extends Playable{

    /** the instrument that the note plays */
    private Instrument instrument;



    /** the duration of the note */
    private IntegerProperty duration;
    /** the pitch of the note */
    private DoubleProperty pitch;
    /** the start tick of the note */
    private DoubleProperty startTick;
    /** Instrument binding to rectangle*/
    private IntegerProperty instrVal;
    private IntegerProperty volume;

    /**
     * The constructor for Note
     */
    public Note(Instrument instrument, int volume){
        super();
        this.startTick = new SimpleDoubleProperty();
        this.duration = new SimpleIntegerProperty();
        this.pitch = new SimpleDoubleProperty();
        this.instrVal= new SimpleIntegerProperty();
        this.volume = new SimpleIntegerProperty();
        this.instrument = instrument;
        this.volume.set(volume);
    }

    /**
     * gets the note's instrument
     * @return this note's instrument
     */
    public Instrument getInstrument() {
        return instrument;
    }

    public String getInstruemntName(){
        return this.instrument.getName();
    }

    public int getInstrumentValue(){
        return this.instrVal.intValue();
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

    public double getX(){return this.getStartTick();}
    public double getWidth(){return this.getDuration();}
    public double getRightX(){return this.getX()+this.getWidth();}

    /**
     * Gets the duration property
     * @return DoubleProperty duration
     */
    public IntegerProperty durationProperty() {
        return duration;
    }

    /**
     * Gets the volume property
     * @return IntegerProperty volume
     */
    public IntegerProperty volumeProperty() { return this.volume; }

    public int getVolume(){
        return this.volumeProperty().intValue();
    }
    /**
     * Gets the volume property
     * @return IntegerProperty volume
     */
    public void setVolume(int val) { this.volume.set(max(0, min(val, 127))); }

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
     * Gets the intrVal property
     * @return DoubleProperty instrVal
     */
    public IntegerProperty intrValProperty() {return this.instrVal;}

    @Override
    /**
     * x,y,width, name, channel, integer representing MIDI instrucment
     * @return
     */
    public String toString() {return toXML(0);}

    /**
     * Returns XML formatted string of Note object
     * @param numTabs an int representing the indentation level
     *                to make the string more readable
     * @return String representation of the object
     */
    public String toXML(int numTabs) {
        String tabbing = (numTabs > 0) ? String.format("%" + numTabs*4 + "s", " ") : "";
        return tabbing +
                "<Note " +
                "xpos=\"" + this.startTickProperty().intValue()    +"\" "+
                "ypos=\"" + this.pitchProperty().intValue() +"\" "+
                "width=\"" + this.durationProperty().getValue()     + "\" " +
                "instValue=\"" + this.instrument.getValue()  +"\" " +
                "volume=\"" + this.getVolume()  +"\" " +
                "/>\n";
    }

}