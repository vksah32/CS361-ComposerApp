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


    /**
     * Initialized ActionContoroller with empty stacks
     */
    public ActionController() {

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Reverts the state of the application to previous state
     */
    public void undo() {
        if (!this.undoStack.isEmpty()) {
            Actionable prevState = this.undoStack.pop();
            System.out.println(prevState.getClass().getName());
            prevState.unDoIt();
            this.redoStack.push(prevState);
        }
    }


    /**
     * Redoes the changes that have been undone
     */
    public void redo(){
        if (!this.redoStack.isEmpty()) {
            Actionable undidState = this.redoStack.pop();
            undidState.reDoIt();
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


}





