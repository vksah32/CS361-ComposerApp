/**
 * File: CompositionPanelController.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj5ZhouRinkerSahChistolini.Models.Composition;
import proj5ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;
import proj5ZhouRinkerSahChistolini.Views.TempoLine;
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
    private Controller mainController;

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

    /** a list property that keeps track of the children of our compositionpane */
    private ListProperty<Node> childrenProperty = new SimpleListProperty();

    /** a boolean binding property that keeps track of whether or not any note is selected */
    private BooleanBinding selectedNotesBinding;

    /**
     * Constructs the Panel and draws the appropriate lines.
     */
    @FXML
    public void initialize() {
        this.drawLines();
        this.composition = new Composition();
        this.clickInPanelHandler = new ClickInPanelHandler(this);
        this.dragInPanelHandler = new DragInPanelHandler(
                this.compositionPanel,
                this.selectionRectangle,
                this
        );
        this.compositionPanel.toFront();
        this.actionController = new ActionController(this);

        //Bindings
        this.isPlaying.bind(this.tempoLine.getIsPlaying());

        //Bind obp to children and list to obp
        ObjectProperty<ObservableList<Node>> obp = new SimpleObjectProperty();
        obp.setValue(this.compositionPanel.getChildren());
        this.childrenProperty.bind(obp);

//        List<Node> something = this.compositionPanel.getChildren().stream().filter(child ->{
//            if (child instanceof SelectableRectangle){
//                ((SelectableRectangle) child).isSelected();
//            }
//            return false;}).collect(Collectors.toList());

        //Set up custom BooleanBinding for our selectedNotes
        this.selectedNotesBinding = Bindings.createBooleanBinding(() -> areNotesSelected(),
                this.compositionPanel.getChildren()
        );
    }

    /**
     * returns true if there is at least one selected note. False otherwise
     * @return true or false
     */
    private boolean areNotesSelected(){
        for(Node child : this.compositionPanel.getChildren()) {
            if (child instanceof SelectableRectangle) {
                if (((SelectableRectangle) child).isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * returns the children property
     * @return this.childrenProperty
     */
    public ListProperty<Node> getChildrenProperty() { return this.childrenProperty; }

    /**
     * returns the areNotesSelected property
     * @return this.areNotesSelected
     */
    public BooleanBinding getSelectedNotesBinding() {return this.selectedNotesBinding; }

    /**
     * returns whether or not the composition is playing
     * @return this.isPlaying
     */
    public BooleanProperty getIsPlayingProperty() {return this.isPlaying; }

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
     * gets the composition pane
     *
     * @return the pane which contains notes
     */
    public Pane getCompositionPane(){
        return this.compositionPanel;
    }

    /**
     * adds Rectangles
     * @param rectangle
     * @param selected
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
     */
    public Collection<Note> getNotesfromComposition(){
        return this.composition.getNotes();
    }



    public Collection<Note> getSelectedNotes(){
        Collection<Note> selected = new HashSet<>();
        for( Note n : this.composition.getNotes()){
                if (n.selectedProperty().getValue()){
                    selected.add(n);
                }
            }
        return selected;
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
//        for( Node node : this.compositionPanel.getChildren()) {
//            if (node instanceof SelectableRectangle) {
//                SelectableRectangle temp = (SelectableRectangle) node;
//                System.out.println(temp.isSelected());
//            }
//        }
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
        deleteSelected(this.getSelectedRectangles());
    }

    /**
     * removes the selected
     * @param selected list of selected rectangles
     *                 TODO REMOVE CONVOLUION HERE
     */
    public void deleteSelected(Collection<SelectableRectangle> selected) {
        //first remove from the panel
        for (Rectangle r: selected){
            this.compositionPanel.getChildren().remove(r);
        }
        //then remove from collection of rectangles
        for (SelectableRectangle rect : selected){
            if(rect instanceof NoteRectangle){
                //compare each noterectangle's property to find corresposnding note
                this.composition.getNotes().removeIf(n -> n.startTickProperty().getValue().equals(rect.xProperty().getValue()) &&
                        n.durationProperty().getValue().equals(rect.widthProperty().getValue())&&
                        n.pitchProperty().getValue().equals(rect.yProperty().getValue()) &&
                        n.getInstrument().getValue()==((NoteRectangle)rect).getInstrument() &&
                        n.selectedProperty().getValue().equals(((NoteRectangle)rect).selectedProperty().getValue()));
            }

        }
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
     * groups the selected rectangles
     */
    public void groupSelected(){
        if(!this.getSelectedRectangles().isEmpty()) {
            GroupRectangle gesture = new GroupRectangle(this.getSelectedRectangles());

            DragInNoteHandler handler = new DragInNoteHandler(gesture, this);
            // sets the handlers of these events to be the
            // specified methods in its DragInNoteHandler object
            gesture.setOnMousePressed(handler::handleMousePressed);
            gesture.setOnMouseDragged(handler::handleDragged);
            gesture.setOnMouseReleased(handler::handleMouseReleased);
            gesture.setOnMouseClicked(new ClickInNoteHandler(this));
            addRectangle(gesture, true);
        }
    }

    /**
     *Ungroups the selected group
     */
    public void ungroupSelected(){
        HashSet<GroupRectangle> selectedGroup = new HashSet<>();
        for (SelectableRectangle rec : this.getSelectedRectangles()){
            if (!rec.xProperty().isBound() && rec instanceof GroupRectangle){
                selectedGroup.add((GroupRectangle) rec);
            }
        }
        selectedGroup.forEach(GroupRectangle::unbindChildren);
        this.deleteSelected(new HashSet<>(selectedGroup));
    }

    /**
     * adds a new acton event to the undo stack
     *
     * @param action the action being preformed
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
        this.actionController.setAfterState(this.getSelectedRectangles());
//        if (!this.actionController.getBeforeSelectedState().equals(
//                this.actionController.getAfterSelectedState())) {
//            this.addAction(new SelectAction(this.actionController.getBeforeSelectedState(), this.actionController.getAfterSelectedState()));
//        }
        if (event.isStillSincePress()) { //differentiate from drag and drop
            if (isPlaying.getValue()) {
                this.stopComposition();
            } else {
                this.clickInPanelHandler.handle(event, this.mainController.getSelectedInstrument());
            }
        }
    }

    /**
     * handles when the mouse is pressed
     */
    @FXML
    public void handleMousePressed(MouseEvent event) {
        this.actionController.setBeforeState(this.getSelectedRectangles());
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
}
