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

package proj6ZhouRinkerSahChistolini.Views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a musical note in the gui interface
 */
public class NoteRectangle extends SelectableRectangle {

    /**int value for the instrument**/
    private int instrument;

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
                         String name, int instrument ) {
        super(x, y, width, height);
        this.getStyleClass().add("note");
        this.getStyleClass().add(name.toLowerCase().replace(" ", "-"));
        this.instrument = instrument;
    }

    /**
     * gets the instrument
     * @return the instrument
     */
    public int getInstrument(){
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
        this.selected.set(selected);
    }

    /**
     * getter for selectedProperty
     * @return selectedProperty
     */
    public BooleanProperty selectedProperty() {
        return this.selected;
    }
}
