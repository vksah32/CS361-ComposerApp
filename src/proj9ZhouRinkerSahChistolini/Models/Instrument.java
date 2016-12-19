/**
 *  This class holds all the info for instruments
 *  @author Ed Zhou
 *  @author Alex Ranker
 */

package proj9ZhouRinkerSahChistolini.Models;

public class Instrument {

    /** Name of the instrument */
    private String name;
    /** Instrument value to be played (0-127)*/
    private int instrument;
    /** Channel of the instrument */
    private int channel;
    /** The id of the instrument */
    private int id;

    /**
     * Initialize a new instrument
     * @param name name of the instrument
     * @param instrument instrument value (0-127)
     * @param channel midi channel associated with instrument (0-15)
     */
    public Instrument(String name, int instrument, int channel, int id){
        this.name = name;
        this.instrument = instrument;
        this.channel = channel;
        this.id = id;
    }

    /**
     * Get channel associated with the instrument
     * @return channel int
     */
    public int getChannel() {
        return this.channel;
    }

    /**
     * Get value associated with the instrument
     * @return instrument int
     */
    public int getValue() {
        return this.instrument;
    }

    /**
     * Get name associated with the instrument
     * @return name string
     */
    public String getName() {
        return this.name;
    }

    /**
     * toString method
     */
    public String toString(){
        return this.name;
    }

    /**
     * sets the id to the input value
     */
    public void setId(int id) { this.id = id;}

    /**
     * returns the id
     */
    public int getId() { return this.id;}
}