package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles Extending by modifying the width of selected rectangles
 */
public class ExtendNoteAction implements Actionable {

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
     *
     */
    public ExtendNoteAction(Collection<SelectableRectangle> moved, Double deltaX){

        this.deltaX =deltaX;
        this.moved = moved;
        this.deltaY =deltaY;

    }


    /**
     * Add the deltaX to the rectangle width
     *
     * @param recs
     * @param notes
     */
    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {

        for (SelectableRectangle rec : moved ){

            Double currentWidth = rec.getWidth();
            rec.setWidth(currentWidth + this.deltaX);
        }

    }

    /**
     * Subtract the deltaX from the rectangle width
     *
     * @param recs
     * @param notes
     */
    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {

        for (SelectableRectangle rec : moved ){

            Double currentWidth = rec.getWidth();
            rec.setWidth(currentWidth - this.deltaX);

        }

    }

}
