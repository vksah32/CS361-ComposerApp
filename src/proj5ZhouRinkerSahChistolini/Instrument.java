package proj5ZhouRinkerSahChistolini;

/**
 *  This class holds all the info for instruments
 *  @author Ed Zhou
 *  @author Alex Ranker
 */
public class Instrument {

    /** Name of the instrument */
    private String name;
    /** Instrument value to be played (0-127)*/
    private int instrument;
    /** Channel of the instrument */
    private int channel;
    /** Color of the instrument */
    private String color;

    /**
     * Initialize a new instrument
     * @param name name of the instrument
     * @param instrument instrument value (0-127)
     * @param channel midi channel associated with instrument (0-15)
     * @param color color associated with instrument
     */
    public Instrument(String name, int instrument, int channel, String color){
        this.name = name;
        this.instrument = instrument;
        this.channel = channel;
        this.color = color;
    }

    /**
     * Get color associated with the instrument
     * @return color string
     */
    public String getColor() {
        return color;
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