package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

public class SelectAllAction implements Actionable{

    /** Collection of previously selected notes*/
    private Collection<SelectableRectangle> selectedRectangles;

    public SelectAllAction(Collection<SelectableRectangle> selected ){
        this.selectedRectangles = selected;
    }

    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {
        recs.forEach(rec -> {
            if (rec instanceof SelectableRectangle) {
                ((SelectableRectangle)rec).setSelected(true);
            }
        });
    }

    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {
        recs.forEach(rec -> {
            if (this.selectedRectangles.contains(rec)) {
                ((SelectableRectangle)rec).setSelected(true);
            }
            else{
                ((SelectableRectangle)rec).setSelected(false);
            }
        });
    }
}
