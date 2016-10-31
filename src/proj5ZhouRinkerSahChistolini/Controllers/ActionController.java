package proj5ZhouRinkerSahChistolini.Controllers;

import proj5ZhouRinkerSahChistolini.Models.Note;

import java.util.Collection;
import java.util.Stack;
import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * Crontrols the actions preformed in the composition pane
 */
public class ActionController {

    /** stack to hold undone action states */
    private Stack<Actionable> undoStack;
    /** stack to hold redone action states */
    private Stack<Actionable> redoStack;

    /** refrence to selectable rectangles list */
    private Collection<Node> graphicRectList;
    /** refrence to Note in Model rectangle list */
    private Collection<Note> modelNoteList;

    private Collection<SelectableRectangle> beforeSelectedState;
    private Collection<SelectableRectangle> afterSelectedState;

    /**
     * Initialized ActionContoroller with empty stacks
     */
    public ActionController(CompositionPanelController compPane) {

        this.graphicRectList = compPane.getCompositionPane().getChildren();
        this.modelNoteList = compPane.getNotesfromComposition();

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Reverts the state of the application to previous state
     */
    public void undo() {
        System.out.println(undoStack);
        if (!this.undoStack.isEmpty()) {
            Actionable prevState = this.undoStack.pop();
            prevState.unDoIt(this.graphicRectList, this.modelNoteList);
            this.redoStack.push(prevState);
        }
    }


    /**
     * Redoes the changes that have been undone
     */
    public void redo(){
        System.out.println(undoStack);
        if (!this.redoStack.isEmpty()) {
            Actionable undidState = this.redoStack.pop();
            undidState.reDoIt(this.graphicRectList, this.modelNoteList);
            this.undoStack.push(undidState);
        }
    }

    /**
     * Each new action must clear the redo stack,
     * then adds the action to the undo stack
     */
    public void addAction(Actionable action) {
        this.redoStack.clear();
        this.undoStack.push(action);
    }

    public void setBeforeState(Collection<SelectableRectangle> state) {
        this.beforeSelectedState = state;
    }
    public void setAfterState(Collection<SelectableRectangle> state) {
        this.afterSelectedState = state;
    }

    public Collection<SelectableRectangle> getAfterSelectedState() {
        return afterSelectedState;
    }

    public Collection<SelectableRectangle> getBeforeSelectedState() {
        return beforeSelectedState;
    }
}




