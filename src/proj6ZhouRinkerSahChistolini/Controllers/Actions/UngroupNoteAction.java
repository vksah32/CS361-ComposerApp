package proj6ZhouRinkerSahChistolini.Controllers.Actions;

import javafx.scene.Node;
import proj6ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj6ZhouRinkerSahChistolini.Views.GroupRectangle;

import java.util.Collection;

/**
 * Adds an action for that is capible of ungrouping
 */
public class UngroupNoteAction implements Actionable {

    /** all group rectangled to be ungrouped */
    private Collection<GroupRectangle> groupedRectangles;
    /** reference to the children of the composition panel */
    private Collection<Node> panelChildren;


    /**
     * Initilize the Ungroup redo and undo capibilities
     *
     * @param group all groups needed to be ungrouped
     * @param comp reference to the composition panel
     */
    public UngroupNoteAction(Collection<GroupRectangle> group,CompositionPanelController comp){
        this.groupedRectangles = group;
        this.panelChildren = comp.getCompositionPane().getChildren();

    }

    /**
     * undo by re-adding the groups to the panel
     */
    @Override
    public void unDoIt() {
        for (GroupRectangle group : this.groupedRectangles){
            group.bindChildren();
            panelChildren.add(group);
        }


    }

    /**
     * redo by removing the groups from the panel
     */
    @Override
    public void reDoIt() {
        for ( GroupRectangle group : this.groupedRectangles){
            group.unbindChildren();
            panelChildren.remove(group);
        }


    }

}
