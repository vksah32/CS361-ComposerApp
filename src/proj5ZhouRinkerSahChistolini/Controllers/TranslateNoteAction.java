package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles translating by midified, x and y position of rectangles
 */
public class TranslateNoteAction implements Actionable {

    /** distance that notes have moved horizontally */
    private Double deltaX;
    /** rectangles that have mvoed */
    private Collection<SelectableRectangle> moved;
    /** distance that notes have moved vertically */
    private Double deltaY;

    /**
     * set up translate action
     *
     * @param moved list of rectangles that have moved
     * @param deltaX x distance that rectangles have moved
     * @param deltaY y distance that rectangles have moved
     *
     */
    public TranslateNoteAction(Collection<SelectableRectangle> moved, Double deltaX, Double deltaY){

        this.deltaX =deltaX;
        this.moved = moved;
        this.deltaY =deltaY;

    }


    /**
     * Add the deltaX to the x-variable and add the deltaY to
     * the y-variables in all moved rectangles
     *
     * @param recs
     * @param notes
     */
    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {

        for (SelectableRectangle rec : moved ){

            Double currentXval = rec.getX();
            Double currentYval = rec.getY();

            rec.setX(currentXval + this.deltaX);
            rec.setY(currentYval + this.deltaY);
        }

    }

    /**
     * Subtract the deltaX from the x-variables and deltaY from the y-variables.
     *
     * @param recs
     * @param notes
     */
    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {
        System.out.println("undo called");

        for (SelectableRectangle rec : moved ){

            Double currentXval = rec.getX();
            Double currentYval = rec.getY();

            rec.setX(currentXval - this.deltaX);
            rec.setY(currentYval - this.deltaY);
        }

    }

}
