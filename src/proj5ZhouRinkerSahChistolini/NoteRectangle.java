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

package proj5ZhouRinkerSahChistolini;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents a musical note in the gui interface
 */
public class NoteRectangle extends Rectangle {

    /** the instrument of the note */
    private Instrument instrument;

    /** keeps track of whether the rectangle is selected */
    private boolean selected;

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
     * returns a boolean value to see if the rectangle is selected
     * @return true if the rectangle is selected, false otherwise
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * sets the selection of the rectangle
     * @param selected
     */
    public void setSelected(boolean selected){
        if(selected){
            this.setStrokeWidth(3);
            this.setStroke(Color.RED);
        }
        else{
            this.setStrokeWidth(1);
            this.setStroke(Color.BLACK);
        }
        this.selected = selected;
    }
}
