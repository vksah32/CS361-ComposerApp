/**
 * File: NoteRectangle.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini.Views;

import proj5ZhouRinkerSahChistolini.Models.Instrument;

/**
 * Represents a musical note in the gui interface
 */
public class NoteRectangle extends SelectableRectangle{

    /** the instrument of the note */
    private Instrument instrument;

    /**
     * The constructor of the NoteRectangle
     * @param x
     * @param y
     * @param width
     * @param height
     * @param instrument
     */
    public NoteRectangle(double x, double y,
                         double width, double height,
                         Instrument instrument) {
        super(x, y, width, height);
        this.setStyle(String.format("-fx-fill: %s", instrument.getColor()));
        this.getStyleClass().add("note");
        this.instrument = instrument;
    }

    /**
     * gets the instrument
     * @return the instrument
     */
    public Instrument getInstrument(){
        return this.instrument;
    }

    /**
     * sets the selection of the rectangle
     * @param selected
     */
    public void setSelected(boolean selected){
        if(selected){
            this.getStyleClass().removeAll("note");
            this.getStyleClass().add("selected-note");
        }
        else{
            this.getStyleClass().removeAll("selected-note");
            this.getStyleClass().add("note");
        }
        this.selected = selected;
    }

}
