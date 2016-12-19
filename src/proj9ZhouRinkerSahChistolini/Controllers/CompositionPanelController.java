/**
 * File: CompositionPanelController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Nov 15, 2016
 */

package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.Actionable;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.GroupNoteAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.UngroupNoteAction;
import proj9ZhouRinkerSahChistolini.Models.Gesture;
import proj9ZhouRinkerSahChistolini.Models.Playable;
import proj9ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj9ZhouRinkerSahChistolini.Models.Composition;
import proj9ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;
import proj9ZhouRinkerSahChistolini.Views.TempoLine;
import javafx.beans.binding.DoubleBinding;

import javax.sound.midi.Sequence;
import java.util.HashSet;
import java.util.Collection;

import static java.lang.Math.min;
import static java.lang.Math.max;

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

    /** a reference to the ScrollPane */
    @FXML
    private ScrollPane scrollPane;

    /** the tempoLine */
    @FXML
    private TempoLine tempoLine;

    /**  group that wraps around the composition panel and the staffpanel*/
    @FXML
    private Group groupToScale;

    /** The rectangle which appears when you select a group of notes*/
    @FXML
    private Rectangle selectionRectangle;

    /** a pointer to the instrument Controller*/
    private InstrumentPanelController instController;

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

    /** current composition scale used for transformations*/
    private Scale scale = new Scale(1,1);

    /** the default width of the notes in the composition*/
    private int noteWidth = 100;

    /** current zoom, bound to zoom property in the zoomHandler */
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1);

    /** current width of the composition */
    private double width = 2000.0;
    private double height = 1280.0;

    /** property panel controller reference */
    private PropertyPanelController propPanel;

    /**
     * Constructs the Panel and draws the appropriate lines.
     */
    @FXML
    public void initialize() {
        this.drawLines();
        this.composition = new Composition();
        this.dragInPanelHandler = new DragInPanelHandler(
                this.selectionRectangle,
                this
        );


        this.compositionPanel.toFront();
        //adds the scale transformation to the group
        this.groupToScale.getTransforms().add(scale);

        // TODO: 12/11/16 FIX THIS BROKEN BINDING
        //this.bindScrollPane();

        //make sure the tempoline doesn't get too big/small
        this.tempoLine.getTransforms().add(scale);

        //bind to tempoLine
        this.isPlaying.bind(this.tempoLine.isPlayingProperty());

        // creates binding for zoom
        this.zoomFactor.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue
            ){
                scale.setX(newValue.doubleValue());
                scale.setY(newValue.doubleValue());
            }
        });
    }

    /**
     * Constrains the scrollpane based on the dimentions
     * of the compositionpanel
     */
    private void bindScrollPane() {
        DoubleBinding widthBinding = Bindings.createDoubleBinding(
                () -> (width * this.zoomFactor.getValue()),
                this.zoomFactor
        );

        DoubleBinding heightBinding = Bindings.createDoubleBinding(
                () -> (height * this.zoomFactor.getValue()),
                this.zoomFactor
        );

        this.scrollPane.hmaxProperty().bind(widthBinding);
        this.scrollPane.vmaxProperty().bind(heightBinding);
    }


    /**
     * Initializes the controller with the parent controller
     */
    public void init(InstrumentPanelController instController, PropertyPanelController propPanel) {
        this.instController = instController;
        this.actionController = new ActionController();
        this.clickInPanelHandler = new ClickInPanelHandler(this, this.instController);
        this.propPanel=propPanel;
    }

    /**
     * adds the NoteRectangles
     * @param rectangle note to be added to the composition panel
     * @param selected if rectangle is selected
     */
    public void addNoteRectangle(NoteRectangle rectangle, boolean selected){
        if(selected){
            rectangle.setSelected(true);
        }
        rectangle.getTransforms().add(scale);
        rectangle.getTransparency().getTransforms().add(scale);
        this.compositionPanel.getChildren().add(rectangle);
        this.compositionPanel.getChildren().add(rectangle.getTransparency());
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
        rectangle.getTransforms().add(scale);
        this.compositionPanel.getChildren().add(rectangle);
    }

    /**
     * Fills given pane with the given collection of selectable rectangles
     * @param rectangles collection of selectable rectangles to add
     */
    public void populateCompositionPanel(Collection<SelectableRectangle> rectangles){
        for (SelectableRectangle rec : rectangles){
            rec.populate(this.compositionPanel, this.scale);
            rec.toFront();
        }
    }


    /**
     * get composition
     * @return get composition
     */
    public Composition getComposition() {
        return composition;
    }

    /**
     * Draws 127 lines with the specified spacing and colors.
     */
    public void drawLines()  {
        staffPane.getChildren().clear();
        staffPane.getChildren().add(tempoLine);
        staffPane.getChildren().add(selectionRectangle);
        for(int i = 1; i < 128; i++)
        {
            Rectangle rec = new Rectangle(0, i*10+1, this.width, 10);
            rec.setFill(null);
            rec.setStroke(Color.GRAY);
            rec.getTransforms().add(scale);
            this.staffPane.getChildren().add(rec);
            if(i%12 == 7) { //Draw special bars for C notes
                rec = new Rectangle(0, i*10+1, this.width, 10);
                rec.getStyleClass().add("c-note");
                if(i==67) {rec.getStyleClass().add("middle-c");}
                rec.getTransforms().add(scale);
                this.staffPane.getChildren().add(rec);
            }
        }

        //vertical lines at every 400 pixel
        for(int i = 0; i < 5; i++)
        {
            Rectangle rec = new Rectangle(400*i, 0, 400, 1280);
            rec.setFill(null);
            rec.setStroke(Color.GRAY);
            rec.getTransforms().add(scale);
            this.staffPane.getChildren().add(rec);

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
    public Collection<Playable> getNotesfromComposition(){
        return this.composition.getNotes();
    }

    /**
     * returns a collection consisting of the selected note objects
     * @return selected a Collection of Notes
     */
    public Collection<Playable> getSelectedNotes() {return this.composition.getSelectedCompositionNotes();}


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
    public void addNoteToComposition(Playable note){
        this.composition.appendPlayable(note);
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
     * Play a sections of the composition
     * @param notes
     */
    public void playSection(Collection<Playable> notes){
        this.stopComposition();
        this.composition.buildSong(notes);
        this.beginAnimation(notes);
        this.composition.play();
    }

    /**
     * gets the current composition zoom
     * @return
     */
    public DoubleProperty getZoomFactor() {
        return zoomFactor;
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
        for(Playable note: this.getNotesfromComposition()){
            maxX = max(maxX, note.getX() + note.getWidth());
        }
        this.tempoLine.updateTempoLine(maxX,
                                       zoomFactor.getValue(),
                                       this.composition.getTempo());
        this.tempoLine.playAnimation();
    }

    /**
     * Instantiate line and transition fields and begins the animation based on notes
     * @param notes the notes to be played
     */
    public void beginAnimation(Collection<Playable> notes) {
        double maxX = 0;
        double minX = Integer.MAX_VALUE;
        for(Playable note: notes){
            maxX = max(maxX, note.getX() + note.getWidth());
            minX = Math.min(minX, note.getX());
        }
        this.tempoLine.updateTempoLine(minX,
                                       maxX,
                                       zoomFactor.getValue(),
                                       this.composition.getTempo());
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
            if (r instanceof NoteRectangle){
                this.compositionPanel.getChildren().remove(((NoteRectangle) r).getTransparency());
            }
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
            Gesture modelGesture = createGesture(gesture, this.composition.getSelectedCompositionNotes());
            this.addRectangle(gesture, true);
            this.addNoteToComposition(modelGesture);
            this.addAction(new GroupNoteAction(gesture, this));
        }
    }

    /** creates a grouprectangle with children
     * @param selectRect the rectangles to be children
     * @return the created GroupRectangle
     */
    public GroupRectangle createGroupRectangle(Collection<SelectableRectangle> selectRect){
        GroupRectangle gesture = new GroupRectangle(selectRect);
        DragInNoteHandler handler = new DragInNoteHandler(gesture, this);
        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        gesture.setOnMousePressed(handler::handleMousePressed);
        gesture.setOnMouseDragged(handler::handleDragged);
        gesture.setOnMouseReleased(handler::handleMouseReleased);
        gesture.setOnMouseClicked(new ClickInNoteHandler(this));

        // Create right click menu handlers
        ContextMenuFactory factory = new ContextMenuFactory(this, gesture);
        ContextMenu menu = factory.createPlayableRightClickMenu();
        factory.setUpListeners(menu);
        return gesture;
    }

    /**
     * Create a bound gesture with associated children
     * @param groupRectangle The group rectangle the gesture is based on
     * @param sounds The children of the gesture
     * @return The created gesture
     */
    public Gesture createGesture(GroupRectangle groupRectangle, Collection<Playable> sounds){
        Gesture gesture = new Gesture(sounds);
        gesture.selectedProperty().bind(groupRectangle.selectedProperty());
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
                        event,
                        this.instController.getSelectedInstrument(),
                        this.noteWidth,
                        this.composition.getVolume()
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


    public PropertyPanelController getPropPanelController(){
        return this.propPanel;
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

    public InstrumentPanelController getInstrumentPanelController(){
        return this.instController;
    }

    /** Get the click in panel handler*/
    public ClickInPanelHandler getClickInPanelHandler(){
        return this.clickInPanelHandler;
    }

    /**
     * updates the compositionPanelFields with the given data
     * @param noteWidth default width of a note
     * @param volume default volume of a note
     * @param tempo default tempo of a note
     */
    public void updatePreferences(int noteWidth, int volume, int tempo) {
        if(noteWidth != 0) { this.noteWidth = noteWidth; }
        this.composition.setVolume(max(0, min(volume, 127)));
        this.composition.setTempo(max(tempo, 1));
    }


    /**
     * resets the entire composition to a fresh slate
     */
    public void reset() {
        selectAllNotes();
        deleteSelectedNotes();
        this.actionController.clearLists();
    }

    /**
     * set pane width size
     * @param width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Get preferred width of the pane
     * @return
     */
    public double getWidth() {
        return width;
    }

    public Sequence getSequence(){
        return composition.getSequence();
    }
    /**
     * returns the default width of the notes
     * @return noteWidth (int)
     */
    public int getNoteWidth() {
        return this.noteWidth;
    }

    public int getNoteVolume() {
        return this.composition.getVolume();
    }

    public int getCompositionTempo() {
        return this.composition.getTempo();
    }
}