/**
 * File: CompositionPanelController.java
 * @author Victoria Chistolini
 * @author Alex Rinker
 * @author Ed Zhou
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: October 18, 2016
 */

package proj5ZhouRinkerSahChistolini.Views;

import javafx.beans.binding.ObjectBinding;
import javafx.scene.shape.Rectangle;

/**
 * Created by Edward on 10/18/2016.
 */
public abstract class SelectableRectangle extends Rectangle {
    /** keeps track of whether the rectangle is selected */
    protected boolean selected;
    /** keeps track of whether the rectangle is bounded */
    protected boolean bounded;

    /**
     * Contructor which gets rid of the parent constructor since
     * we don't want to call it
     */
    public SelectableRectangle(){}

    /**
     * Constructor for the SelectableRectangle
     * Initializes all of the fields with the input values
     * @param x the x position of the b-l corner of the rectangle
     * @param y the y position of the b-l corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public SelectableRectangle(double x, double y,
                               double width, double height) {
        super(x, y, width, height);
        bounded = false;
    }

    /**
     * returns whether or not the rectangle is selected
     * @return selected boolean which represent whether or not
     * the rectangle is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * sets the selected field
     * @param bool a boolean representing whether or not the rectangle is
     * to be selected or not
     */
    public abstract void setSelected(boolean bool);

    /**
     * returns whether or not the rectangle is bounded
     * @return bounded boolean which represent whether or not
     * the rectangle is bounded
     */
    public boolean isBounded(){
        return bounded;
    }

    /**
     * sets the bounded field
     * @param bounded a boolean representing whether or not the rectangle is
     * to be bounded or not
     */
    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }

}
