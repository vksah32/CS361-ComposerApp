package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * Manages redo and undo actions for grouping
 */
public class GroupNoteAction implements Actionable {


    private GroupRectangle groupedRectangle;
    private Collection<Node> panelChildren;


    public GroupNoteAction(GroupRectangle group,CompositionPanelController comp){
        this.groupedRectangle = group;
        this.panelChildren = comp.getCompositionPane().getChildren();

    }

    @Override
    public void reDoIt() {
        groupedRectangle.bindSelection();
        panelChildren.add(groupedRectangle);

    }


    @Override
    public void unDoIt() {

        groupedRectangle.unbindChildren();
        panelChildren.remove(groupedRectangle);

    }

}
