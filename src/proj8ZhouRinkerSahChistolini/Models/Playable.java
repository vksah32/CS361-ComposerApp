package proj8ZhouRinkerSahChistolini.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Item that can be selected and played in a composition
 */
public abstract class Playable {
    /** selectedproperty to track selectionOfrectangle **/
    private BooleanProperty selected;

    public Playable(){
        this.selected = new SimpleBooleanProperty();
    }
    /**
     * getter for selectedProperty
     * @return BooleanProperty  the selectedProperty
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public abstract double getX();

    public abstract double getRightX();

    public abstract double getWidth();

    public String toXML(int numTabs) {
        return this.toXML(0);
    }
}
