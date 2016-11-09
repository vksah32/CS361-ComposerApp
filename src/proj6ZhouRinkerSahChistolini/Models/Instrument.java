/**
 *  This class holds all the info for instruments
 *  @author Ed Zhou
 *  @author Alex Ranker
 */

package proj6ZhouRinkerSahChistolini.Models;

public class Instrument {

    /** Name of the instrument */
    private String name;
    /** Instrument value to be played (0-127)*/
    private int instrument;
    /** Channel of the instrument */
    private int channel;

    /**
     * Initialize a new instrument
     * @param name name of the instrument
     * @param instrument instrument value (0-127)
     * @param channel midi channel associated with instrument (0-15)
     */
    public Instrument(String name, int instrument, int channel){
        this.name = name;
        this.instrument = instrument;
        this.channel = channel;
    }

    /**
     * Get channel associated with the instrument
     * @return channel int
     */
    public int getChannel() {
        return channel;
    }

    /**
     * Get value associated with the instrument
     * @return instrument int
     */
    public int getValue() {
        return instrument;
    }

    /**
     * Get name associated with the instrument
     * @return name string
     */
    public String getName() {
        return name;
    }
}