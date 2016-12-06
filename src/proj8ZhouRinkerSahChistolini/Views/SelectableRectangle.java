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

package proj8ZhouRinkerSahChistolini.Views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;

/**
 * Created by Edward on 10/18/2016.
 */
public abstract class SelectableRectangle extends Rectangle {
    /** keeps track of whether the rectangle is selected */
    protected SimpleBooleanProperty selected = new SimpleBooleanProperty();
    /** keeps track of whether the rectangle is bounded */

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
    }

    /**
     * set the x and y position of the selectable rectangle only if unbinded
     */
    public void setUnboundPosition(double x, double y) {
        this.setUnboundX(x);
        this.setUnboundY(y);
    }

    /**
     * set the x and y position of the selectable rectangle only if unbinded
     */
    public void setUnboundX(double x) {
        if (!this.xProperty().isBound()){
            this.setX(x);
        }
    }

    /**
     * set y position of the selectable rectangle only if unbinded
     */
    public void setUnboundY(double y) {
        if (!this.yProperty().isBound()){
            this.setY(y);
        }
    }

    /**
     * set the width of the selectable rectangle only if unbinded
     */
    public void setUnboundWidth(double width) {
        if (!this.widthProperty().isBound()){
            this.setWidth(width);
        }
    }

    /**
     * returns whether or not the rectangle is selected
     * @return selected boolean which represent whether or not
     * the rectangle is selected
     */
    public boolean isSelected() {
        return selected.getValue();
    }

    /**
     *  gets the selected property
     * @return a BooleanProperty
     */
    public BooleanProperty selectedProperty(){
        return this.selected;
    }

    /**
     * sets the selected field
     * @param bool a boolean representing whether or not the rectangle is
     * to be selected or not
     */
    public abstract void setSelected(boolean bool);

    /**
     * Returns a string representation of the object
     * which is indented numTabs times
     * @param numTabs the indentation level
     * @return String representation of the object
     */
    public String toXML(int numTabs) {
        return this.toXML(0);
    }

    /** Populate a given pane with this object*/
    public void populate(Pane pane, Transform transform){
        this.setSelected(true);
        this.getTransforms().add(transform);
        pane.getChildren().add(this);
    }
}
