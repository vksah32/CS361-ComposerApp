/**
 * File: ActionController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */
package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

/**
 * Crontrols the actions preformed in the composition pane
 */
public class ActionController {

    /** List to hold undone action states */
    private ObservableList<Actionable> undoList;
    /** List to hold redone action states */
    private ObservableList<Actionable> redoList;

    /**
     * Initialized ActionController with empty stacks
     */
    public ActionController() {
        List<Actionable> uList = new ArrayList<>();
        List<Actionable> rList= new ArrayList<>();
        this.undoList = FXCollections.observableList(uList);
        this.redoList = FXCollections.observableList(rList);
    }

    /**
     * Reverts the state of the application to previous state
     */
    public void undo() {
        if (!this.undoList.isEmpty()) {
            Actionable prevState = this.undoList.get(undoList.size() - 1);
            prevState.unDoIt();
            //remove after doing it to attain newest state for bindings
            this.undoList.remove(undoList.size() - 1);
            this.redoList.add(prevState);
        }
    }


    /**
     * Redoes the changes that have been undone
     */
    public void redo(){
        if (!this.redoList.isEmpty()) {
            Actionable undidState = this.redoList.get(redoList.size() - 1);
            undidState.reDoIt();
            this.redoList.remove(redoList.size() - 1);
            this.undoList.add(undidState);
        }
    }

    /**
     * Each new action must clear the redo stack,
     * then adds the action to the undo stack
     */
    public void addAction(Actionable action) {
        this.redoList.clear();
        this.undoList.add(action);
    }

    /**
     * Get and observable undolist
     * @return the undo list
     */
    public ObservableList<Actionable> getUndoList(){
        return this.undoList;
    }

    /**
     * Get and observable redo list
     * @return the redo list
     */
    public ObservableList<Actionable> getRedoList() {
        return this.redoList;
    }
}





