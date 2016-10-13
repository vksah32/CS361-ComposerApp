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
    private int instrument;

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
                         String instrument) {
        super(x, y, width, height);
        this.setInstrument(instrument);
    }

    /**
     * sets the rectangles color and instrument number given the instrument
     * @param instrument
     */
    public void setInstrument(String instrument){
        if (instrument.equals("Harpsichord")){
            this.setFill(Color.GREEN);
            this.instrument = 6;}
        else if (instrument.equals("Marimba")) {
            this.setFill(Color.BLUE);
            this.instrument = 12;
        }
        else if (instrument.equals("Organ")) {
            this.setFill(Color.GOLD);
            this.instrument = 19;
        }
        else if (instrument.equals("Accordion")){
            this.setFill(Color.PURPLE);
            this.instrument = 21;
        }
        else if (instrument.equals("Guitar")) {
            this.setFill(Color.DEEPSKYBLUE);
            this.instrument = 24;
        }
        else if (instrument.equals("Violin")) {
            this.setFill(Color.BLACK);
            this.instrument = 40;
        }
        else if (instrument.equals("French Horn")) {
            this.setFill(Color.SANDYBROWN);
            this.instrument = 60;
        }
        else{
            this.setFill(Color.GREY);
            this.instrument = 0;
        }
    }

    /**
     * gets the instrument
     * @return the instrument
     */
    public int getInstrument(){
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
