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
package proj10ZhouRinkerSahChistolini.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proj10ZhouRinkerSahChistolini.Controllers.Actions.Actionable;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Crontrols the actions preformed in the composition pane
 */
public class ActionController {

    /** Cached list to check if unsaved actions*/
    private List<Actionable> saveList;

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
        this.saveList = new ArrayList<>();
    }

    /**
     * Reverts the state of the application to previous state
     */
    public void undo() {
        if (!this.undoList.isEmpty()) {
            Actionable prevState = peekUndo();
            prevState.unDoIt();
            //remove after doing it to attain newest state for bindings
            popUndo();
            this.redoList.add(prevState);
        }
    }

    /**
     * Redoes the changes that have been undone
     */
    public void redo(){
        if (!this.redoList.isEmpty()) {
            Actionable undidState = peekRedo();
            undidState.reDoIt();
            popRedo();
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
     * returns a ListProperty of the actions in the undoList
     * @return ListProperty undoActions
     */
    public ListProperty<Actionable> getUndoActionsProperty() {
        //Bind obp to children and list to obp
        ObjectProperty<ObservableList<Actionable>> obp = new SimpleObjectProperty();
        obp.setValue(this.undoList);
        ListProperty<Actionable> undoActions = new SimpleListProperty();
        undoActions.bind(obp);
        return undoActions;
    }

    /**
     * returns a ListProperty of the actions in the redoList
     * @return ListProperty redoActions
     */
    public ListProperty<Actionable> getRedoActionsProperty() {
        //Bind obp to children and list to obp
        ObjectProperty<ObservableList<Actionable>> obp = new SimpleObjectProperty();
        obp.setValue(this.redoList);
        ListProperty<Actionable> redoActions = new SimpleListProperty();
        redoActions.bind(obp);
        return redoActions;
    }

    /**
     * returns a BooleanBinding representing whether or not the undoList is empty
     * @return BooleanBinding
     */
    public BooleanBinding isUndoEmpty() {
        BooleanBinding emptyUndoBinding = Bindings.createBooleanBinding(() ->
                this.undoList.size() < 1,
                this.undoList
        );
        return emptyUndoBinding;
    }

    /**
     * returns a BooleanBinding representing whether or not the redoList is empty
     * @return BooleanBinding
     */
    public BooleanBinding isRedoEmpty() {
        BooleanBinding emptyRedoBinding = Bindings.createBooleanBinding(() ->
                this.redoList.size() < 1,
                this.redoList
        );
        return emptyRedoBinding;
    }

    /**
     * returns the top element of the undoList
     * throws an emptyStackException
     * @return undoList[0]
     */
    public Actionable peekUndo() throws EmptyStackException {
        if(undoList.size() > 0) { return undoList.get(undoList.size() - 1); }
        throw new EmptyStackException();
    }

    /**
     * returns the top element of the redoList
     * @return redoList[0]
     */
    public Actionable peekRedo() throws EmptyStackException {
        if(redoList.size() > 0) { return redoList.get(redoList.size() - 1); }
        throw new EmptyStackException();
    }

    /**
     * removes and returns the top element of the undoList
     * @return undoList[0]
     */
    public Actionable popUndo() throws EmptyStackException {
        if(undoList.size() > 0) { return undoList.remove(undoList.size() - 1); }
        throw new EmptyStackException();
    }

    /**
     * removes and returns the top element of the redoList
     * @return redoList[0]
     */
    public Actionable popRedo() throws EmptyStackException {
        if(redoList.size() > 0) { return redoList.remove(redoList.size() - 1); }
        throw new EmptyStackException();
    }

    /**
     * clears the undo and redo lists
     */
    public void clearLists() {
        this.undoList.clear();
        this.redoList.clear();
    }

    /** update the saveList */
    public void saveList(){
        this.saveList = new ArrayList<>();
        undoList.forEach(n-> this.saveList.add(n));
    }

    /**
     * Checks if state is saved
     * @return boolean state
     */
    public boolean isSaved(){
        if (this.saveList.size() == this.undoList.size()){
            for (int i=0; i<this.saveList.size();i++){
                if (!this.saveList.get(i).equals(this.undoList.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}





