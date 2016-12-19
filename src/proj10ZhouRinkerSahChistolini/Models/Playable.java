package proj10ZhouRinkerSahChistolini.Models;

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

    /** Get leftmost X of self */
    public abstract double getX();

    /** Get rightmost X of self */
    public abstract double getRightX();

    /** Get width of the playable notes */
    public abstract double getWidth();

    /** Set volume of self and all children */
    public abstract void setVolume(int val);

    /** Get volume of the sound */
    public abstract int getVolume();

    /**
     * get a XML string representing the item
     * @param numTabs number of tabs to be used in XML
     * @return the string XML
     */
    public abstract String toXML(int numTabs);
}
