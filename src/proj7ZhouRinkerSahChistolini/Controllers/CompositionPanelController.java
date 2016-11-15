/**
 * File: CompositionPanelController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */

package proj7ZhouRinkerSahChistolini.Controllers;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import proj7ZhouRinkerSahChistolini.Controllers.Actions.Actionable;
import proj7ZhouRinkerSahChistolini.Controllers.Actions.GroupNoteAction;
import proj7ZhouRinkerSahChistolini.Controllers.Actions.UngroupNoteAction;
import proj7ZhouRinkerSahChistolini.Models.Note;
import proj7ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj7ZhouRinkerSahChistolini.Models.Composition;
import proj7ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj7ZhouRinkerSahChistolini.Views.SelectableRectangle;
import proj7ZhouRinkerSahChistolini.Views.TempoLine;

import java.util.HashSet;
import java.util.Collection;

import java.util.*;

/**
 * The pane in which all of the notes are stored and displayed.
 */
public class CompositionPanelController {

    /** a Pane to hold all the notes */
    @FXML
    private Pane compositionPanel;

    /** a Pane to hold all of the lines */
    @FXML
    private Pane staffPane;

    /** the tempoLine */
    @FXML
    private TempoLine tempoLine;

    /** The rectangle which appears when you select a group of notes*/
    @FXML
    private Rectangle selectionRectangle;

    /** a pointer to the main controller */
    Controller mainController;

    /** The clickInPaneHandler object */
    private ClickInPanelHandler clickInPanelHandler;

    /** The dragInPaneHandler object */
    private DragInPanelHandler dragInPanelHandler;

    /** The composition object */
    private Composition composition;

    /** Holds redo and undo states */
    private ActionController actionController;

    /** a boolean property that keeps track of whether the composition is being played */
    private BooleanProperty isPlaying = new SimpleBooleanProperty();

    private HashMap<NoteRectangle, Note> noteMap;

    /**
     * Constructs the Panel and draws the appropriate lines.
     */
    @FXML
    public void initialize() {
        this.drawLines();
        this.composition = new Composition();
        this.clickInPanelHandler = new ClickInPanelHandler(this);
        this.dragInPanelHandler = new DragInPanelHandler(
                this.selectionRectangle,
                this
        );
        this.compositionPanel.toFront();
        this.actionController = new ActionController();

        //bind to tempoLine
        this.isPlaying.bind(this.tempoLine.isPlayingProperty());
        noteMap = new HashMap<>();
    }

    /**
     * Initializes the controller with the parent controller
     */
    public void init(Controller controller) {
        this.mainController = controller;
    }

    /**
     * adds the NoteRectangles
     * @param rectangle
     * @param selected
     */
    public void addNoteRectangle(NoteRectangle rectangle, boolean selected){
        if(selected){
            rectangle.setSelected(true);
        }
        this.compositionPanel.getChildren().add(rectangle);
    }

    /**
     * adds Rectangles
     *
     * @param rectangle rectangles to add
     * @param selected if they are selected or not
     *
     */
    public void addRectangle(SelectableRectangle rectangle, boolean selected){
        if(selected){
            rectangle.setSelected(true);
        }
        this.compositionPanel.getChildren().add(rectangle);
    }

    /**
     * Draws 127 lines with the specified spacing and colors.
     */
    private void drawLines()  {
        for(int i = 1; i < 128; i++)
        {
            Line line = new Line(0, i*10+1, 2000,i*10+1);
            line.setId("lines");
            this.staffPane.getChildren().add(line);
        }
    }

    /**
     * gets the rectangles
     * @return a collection of SelectableRectangles
     *
     */
    public Collection<SelectableRectangle> getRectangles() {
        HashSet<SelectableRectangle> newSet = new HashSet<>();
        for (Node rec : this.compositionPanel.getChildren()) {
            if(rec instanceof SelectableRectangle){
                newSet.add((SelectableRectangle)rec);
            }
        }
        return newSet;
    }

    /**
     * gets the Note objects from the composition
     * @return the notes on Composition
     *
     */
    public Collection<Note> getNotesfromComposition(){
        return this.composition.getNotes();
    }

    /**
     * returns a collection consisting of the selected note objects
     * @return selected a Collection of Notes
     */
    public Collection<Note> getSelectedNotes() {return this.composition.getSelectedCompositionNotes();}

    public void addNotestoMap(Note note, NoteRectangle nr){
        this.noteMap.put(nr,note);
    }

    /**
     * gets the action controller
     *
     * @return the ActionController object
     */
    public ActionController getActionController() {
        return this.actionController;
    }

    /**
     * gets the selectedRectangles
     * @return a collection of selected notes
     */
    public Collection<SelectableRectangle> getSelectedRectangles() {
        HashSet<SelectableRectangle> selectedList = new HashSet<>();
        for(SelectableRectangle rectangle : this.getRectangles()){
            if(rectangle.isSelected()){
                selectedList.add(rectangle);
            }
        }
        return selectedList;
    }

    /**
     * adds the note to composition
     * @param note note to be added
     */
    public void addNoteToComposition(Note note){
        this.composition.appendNote(note);
    }

    /**
     * Plays the composition and initiates the animation.
     * Stops the current animation and plays a new one if one already exists.
     */
    public void playComposition() {
        this.stopComposition();
        this.composition.buildSong();

        this.beginAnimation();
        this.composition.play();
    }

    /**
     * Stops and clears the composition and destroys the animation if there is one.
     */
    public void stopComposition() {
        this.stopAnimation();
        this.composition.stop();
    }

    /**
     * Instantiates the line and transition fields and begins the animation based on
     * the length of the composition.
     */
    public void beginAnimation() {
        double maxX = 0;
        for(SelectableRectangle rectangle: this.getRectangles()){
            maxX = Math.max(maxX, rectangle.getX() + rectangle.getWidth());
        }
        this.tempoLine.updateTempoLine(maxX);
        this.tempoLine.playAnimation();
    }

    /**
     * Stops the animation and removes the line from the composition panel.
     */
    public void stopAnimation() {
        this.tempoLine.stopAnimation();
    }

    /**
     * clears the selection of the rectangles
     */
    public void clearSelected() {
        for(SelectableRectangle rectangle: this.getRectangles()){
            if(rectangle.isSelected()){
                rectangle.setSelected(false);
            }
        }
    }

    /**
     * removes the selected rectangles
     */
    public void deleteSelectedNotes() {
        this.deleteSelected(this.getSelectedRectangles());
    }

    /**
     * removes the selected
     * @param selected list of selected rectangles
     */
    public void deleteSelected(Collection<SelectableRectangle> selected) {
        //first remove from the panel
        for (Rectangle r: selected){
            this.compositionPanel.getChildren().remove(r);
        }
        //remove selected notes from composition
        this.composition.getNotes().removeIf(n ->
                        n.selectedProperty().getValue().equals(true));
    }

    /**
     * selects all the notes in the composition
     */
    public void selectAllNotes(){
        for (SelectableRectangle rectangle: getRectangles()){
            rectangle.setSelected(true);
        }
    }

    /**
     * Creates a Group Rectangle on the canvas
     * returns null otherwise
     * @return GroupRectangle
     */
    public void groupSelected(Collection<SelectableRectangle> selectRect){
        if(!selectRect.isEmpty()) {
            GroupRectangle gesture = createGroupRectangle(selectRect);
            this.addAction(new GroupNoteAction(gesture, this));
        }
    }

    /** Adds the GroupRectangle to the panel while indicating an action */
    public GroupRectangle createGroupRectangle(Collection<SelectableRectangle> selectRect){
        GroupRectangle gesture = new GroupRectangle(selectRect);
        DragInNoteHandler handler = new DragInNoteHandler(gesture, this);
        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        gesture.setOnMousePressed(handler::handleMousePressed);
        gesture.setOnMouseDragged(handler::handleDragged);
        gesture.setOnMouseReleased(handler::handleMouseReleased);
        gesture.setOnMouseClicked(new ClickInNoteHandler(this));
        addRectangle(gesture, true);
        return gesture;
    }

    /**
     *  Ungroups the selected group
     */
    public void ungroupSelected(Collection<SelectableRectangle> selectRect) {

        HashSet<GroupRectangle> selectedGroup = new HashSet<>();
        for (SelectableRectangle rec : selectRect){
            if (!rec.xProperty().isBound() && rec instanceof GroupRectangle){
                selectedGroup.add((GroupRectangle) rec);
            }
        }
        selectedGroup.forEach(GroupRectangle::unbindChildren);
        this.addAction(new UngroupNoteAction(selectedGroup,this));
        this.deleteSelected(new HashSet<>(selectedGroup));

    }

    public String generateString(SelectableRectangle sr) {
            String noteRep = new String();

            if (sr instanceof GroupRectangle) {
              noteRep += "{\n";
                HashSet<SelectableRectangle> children = ((GroupRectangle) sr).getChildren();
                for(SelectableRectangle child : children) {

                    noteRep += this.generateString(child);

                }
                noteRep += "}\n";

            } else if (sr instanceof NoteRectangle) {
                    Note note = this.noteMap.get(sr);
                noteRep += note.toString();
            }

            return noteRep;
        }


    /**
     * adds a new acton event to the undo stack
     *
     * @param action the action being preformed
     *
     */
    public void addAction(Actionable action){

        this.actionController.addAction(action);
    }

    /**
     * Handles mouse click events, extracts x,y coordinates
     * relative to note, gets the name of the instrument, and creates a new
     * note and adds it to the composition and compositionPanel.
     *
     * @param event a mouse click event.
     */
    @FXML
    public void handleMouseClick(MouseEvent event) {

        if (event.isStillSincePress()) { //differentiate from drag and drop
            if (isPlaying.getValue()) {
                this.stopComposition();
            } else {
                this.clickInPanelHandler.handle(
                        event, this.mainController.getSelectedInstrument()
                );
            }
        }
    }

    /**
     * handles when the mouse is pressed
     */
    @FXML
    public void handleMousePressed(MouseEvent event) {
        this.dragInPanelHandler.handleMousePressed(event);}

    /**
     * handles when the mouse is dragged
     */
    @FXML
    public void handleDragged(MouseEvent event) {
        dragInPanelHandler.handleDragged(event);
    }

    /**
     * handles when the mouse is released after dragging
     */
    @FXML
    public void handleDragReleased(MouseEvent event) {
        dragInPanelHandler.handleDragReleased(event);
    }

    /**
     * returns the Pane's tempoline
     * @return a tempoLine
     */
    public TempoLine getTempoLine() {
        return this.tempoLine;
    }

    /**
     * gets the composition pane
     *
     * @return the pane which contains notes
     */
    public Pane getCompositionPane(){
        return this.compositionPanel;
    }

    /** Get the click in panel handler*/
    public ClickInPanelHandler getClickInPanelHandler(){
        return this.clickInPanelHandler;
    }

    public HashMap<NoteRectangle, Note> getNoteMap() {
        return noteMap;
    }
}