package proj8ZhouRinkerSahChistolini.Controllers.Actions;

import proj8ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * Stores the state after a selection action has occured
 * Can undo and redo this action
 */
public class SelectAction implements Actionable{

    /** Collection of previously selected notes */
    private Collection<SelectableRectangle> before;
    /** Collection of currently selected notes */
    private Collection<SelectableRectangle> after;
    /** Collection of all rectangles on the composition panel */
    private Collection<SelectableRectangle> recs;


    /**
     * Initilize the SelectAction event, when a selection occurs
     *
     * @param before state of selection before event
     * @param after state of selection after the event
     * @param comp refernece to composition controller
     *
     */
    public SelectAction(Collection<SelectableRectangle> before,
                        Collection<SelectableRectangle> after,
                        CompositionPanelController comp ){
        this.before = before;
        this.after = after;
        this.recs = comp.getRectangles();
    }


    /**
     * redo by restoring the composition panel nodes to the state after the action occured
     */
    @Override
    public void reDoIt() {
        recs.forEach(rec -> {
            if (this.after.contains(rec)) {
                rec.setSelected(true);
            }
            else{
                rec.setSelected(false);
            }
        });
    }

    /**
     * undo by restoring the compositon panel node to the state before action occured
     */

    @Override
    public void unDoIt() {
        recs.forEach(rec -> {
            if (this.before.contains(rec)) {
                rec.setSelected(true);
            }
            else{
                rec.setSelected(false);
            }
        });
    }
}
