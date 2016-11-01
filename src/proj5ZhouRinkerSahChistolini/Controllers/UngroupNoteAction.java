package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Views.GroupRectangle;

import java.util.Collection;

/**
 * Adds an action for that is capible of ungrouping
 */
public class UngroupNoteAction implements Actionable {


    private Collection<GroupRectangle> groupedRectangles;
    private Collection<Node> panelChildren;


    public UngroupNoteAction(Collection<GroupRectangle> group,CompositionPanelController comp){
        this.groupedRectangles = group;
        this.panelChildren = comp.getCompositionPane().getChildren();

    }

    @Override
    public void unDoIt() {
        for (GroupRectangle group : this.groupedRectangles){
            group.bindSelection();
            panelChildren.add(group);
        }


    }


    @Override
    public void reDoIt() {
        for ( GroupRectangle group : this.groupedRectangles){
            group.unbindChildren();
            panelChildren.remove(group);
        }


    }

}
