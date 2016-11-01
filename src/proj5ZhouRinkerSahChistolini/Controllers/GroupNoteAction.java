package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Views.GroupRectangle;
import java.util.Collection;

/**
 * Manages redo and undo actions for grouping
 */
public class GroupNoteAction implements Actionable {

    /** group rectanlge to preform actions on */
    private GroupRectangle groupedRectangle;
    /** all of the children on the composition panel */
    private Collection<Node> panelChildren;


    /**
     * Initilize the groupNoteAction so that we can undo and redo actions for grouping
     *
     * @param group obect of group to be acted upon
     * @param comp refreence to all notes in composition pane
     *
     */
    public GroupNoteAction(GroupRectangle group,CompositionPanelController comp){
        this.groupedRectangle = group;
        this.panelChildren = comp.getCompositionPane().getChildren();

    }

    /**
     * redo the ground action by re-addding the group
     */
    @Override
    public void reDoIt() {
        groupedRectangle.bindSelection();
        panelChildren.add(groupedRectangle);

    }

    /**
     * undo the group by removing the group from the panel
     */

    @Override
    public void unDoIt() {

        groupedRectangle.unbindChildren();
        panelChildren.remove(groupedRectangle);

    }

}
