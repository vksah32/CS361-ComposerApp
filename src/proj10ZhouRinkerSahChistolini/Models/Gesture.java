package proj10ZhouRinkerSahChistolini.Models;

import java.util.Collection;
import java.util.Comparator;

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

    public void setVolume(int val){
        this.children.forEach(n->n.setVolume(val));
    }

    public int getVolume(){
        int total = 0;
        for (Playable child : this.children){
            total += child.getVolume();
        }
        return total/this.children.size();
    }

    /**
     * Get the left x of the gesture
     * @return
     */
    public double getX(){
        return this.children.stream().min(Comparator.comparing(Playable::getX)).get().getX();
    }

    /**
     * Get the right x of the gesture
     */
    public double getRightX(){
        return this.children.stream()
                .max(Comparator.comparing(n -> n.getX()+n.getWidth())).get().getRightX();
    }
    public double getWidth(){
        Playable left = this.children.stream().min(
            Comparator.comparing(Playable::getX)
        ).get();
        Playable right = this.children.stream().max(
            Comparator.comparing(n -> n.getX()+n.getWidth())
        ).get();
        return right.getX() + right.getWidth() - left.getX();
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
