package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * Created by Remis on 10/31/2016.
 */
public class SelectAction implements Actionable{

    /** Collection of previously selected notes*/
    private Collection<SelectableRectangle> before;
    private Collection<SelectableRectangle> after;

    public SelectAction(Collection<SelectableRectangle> before, Collection<SelectableRectangle> after ){
        this.before = before;
        this.after = after;
    }

    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {
        recs.forEach(rec -> {
            if (this.after.contains(rec)) {
                ((SelectableRectangle)rec).setSelected(true);
            }
            else{
                ((SelectableRectangle)rec).setSelected(false);
            }
        });
    }

    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {
        recs.forEach(rec -> {
            if (this.before.contains(rec)) {
                ((SelectableRectangle)rec).setSelected(true);
            }
            else{
                ((SelectableRectangle)rec).setSelected(false);
            }
        });
    }
}
