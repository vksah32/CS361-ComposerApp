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

import javafx.scene.shape.Rectangle;

/**
 * Created by Remis on 10/18/2016.
 */
public abstract class SelectableRectangle extends Rectangle{
    /** keeps track of whether the rectangle is selected */
    protected boolean selected;
    protected boolean bounded;

    public SelectableRectangle(){}

    public SelectableRectangle(double x, double y,
                               double width, double height) {
        super(x, y, width, height);
        bounded = false;
    }

    public boolean isSelected() {
        return selected;
    }
    public abstract void setSelected(boolean bool);

    public boolean isBounded(){
        return bounded;
    }
    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }

}
