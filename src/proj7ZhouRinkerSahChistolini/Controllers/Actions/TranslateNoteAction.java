package proj7ZhouRinkerSahChistolini.Controllers.Actions;

import proj7ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj7ZhouRinkerSahChistolini.Views.SelectableRectangle;

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
    /** all rectangles on the composition panel */
    private Collection<SelectableRectangle> recs;

    /**
     * set up translate action
     *
     * @param moved list of rectangles that have moved
     * @param deltaX x distance that rectangles have moved
     * @param deltaY y distance that rectangles have moved
     * @param comp  the composition panel
     *
     */
    public TranslateNoteAction(Collection<SelectableRectangle> moved,
                               Double deltaX, Double deltaY,
                               CompositionPanelController comp){
        this.deltaX =deltaX;
        this.moved = moved;
        this.deltaY =deltaY;
        this.recs = comp.getRectangles();
    }


    /**
     *
     * Add the deltaX to the x-variable and add the deltaY to
     * the y-variables in all moved rectangles
     *
     */
    @Override
    public void reDoIt() {
        moved.forEach(rec -> {
            if(recs.contains(rec)){
                Double currentXval = rec.getX();
                Double currentYval = rec.getY();

                rec.setUnboundX(currentXval + this.deltaX);
                rec.setUnboundY(currentYval + this.deltaY);

            }
        });

    }

    /**
     *
     * Subtract the deltaX from the x-variables and deltaY from the y-variables.
     *
     */
    @Override
    public void unDoIt() {
        moved.forEach(rec -> {
            if(recs.contains(rec)){
                Double currentXval = rec.getX();
                Double currentYval = rec.getY();

                rec.setUnboundX(currentXval - this.deltaX);
                double actualNewY = currentYval - this.deltaY;
                rec.setUnboundY(actualNewY);
            }
        });


    }

}
