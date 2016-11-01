package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

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

    public SelectAction(Collection<SelectableRectangle> before, Collection<SelectableRectangle> after, CompositionPanelController comp ){
        this.before = before;
        this.after = after;
        this.recs = comp.getRectangles();
    }

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
