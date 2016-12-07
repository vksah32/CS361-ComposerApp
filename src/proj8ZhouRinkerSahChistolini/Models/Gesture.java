package proj8ZhouRinkerSahChistolini.Models;

import java.util.Collection;

/**
 * Grouping class of playable sounds
 */
public class Gesture extends Playable{
    /** The direct children of the gesture*/
    private Collection<Playable> children;

    /**
     * Initialize a gesture with alloted children
     * @param children
     */
    public Gesture(Collection<Playable> children){
        this.children = children;
    }

    /**
     * Get the direct children of the gesture
     * @return the playable children children
     */
    public Collection<Playable> getChildren(){
        return this.children;
    }
    @Override
    /**
     * Returns XML formatted string of GroupRectangle and
     * its children
     * @return String representation of the object
     */
    public String toString() { return toXML(0); }

    /**
     * Returns XML formatted string of GroupRectangle and
     * its children
     * @param numTabs an int representing the indentation level
     *                to make the string more readable
     * @return String representation of the object
     */
    public String toXML(int numTabs) {
        String kids = "";
        for(Playable child : this.children) {
            kids += child.toXML(numTabs+1);
        }
        String tabbing = (numTabs > 0) ? String.format("%" + numTabs*4 + "s", " ") : "";
        return tabbing + "<Gesture>\n" +
                kids +
                tabbing + "</Gesture>\n";
    }
}
