/**
 * File: Controller.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini;

import javafx.fxml.FXML;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Handles all user GUI interactions and coordinates with the MidiPlayer
 * and Composition.
 */
public class Controller {

    /** The compositionPanel object that is modified */
    @FXML
    public CompositionPanel compositionPanel;

    /** The composition object */
    private Composition composition;

    /** The clickInPaneHandler object */
    private ClickInPanelHandler clickInPanelHandler;

    /** The dragInPaneHandler object */
    private DragInPanelHandler dragInPanelHandler;

    /** The line object */
    private Line line;

    /** The transition object */
    private TranslateTransition transition;

    /** a boolean field that keeps track of whether the composition is being played */
    private boolean isPlaying;

    /** The Object which converts all of the rectangles into musical Notes*/
    RectangleToNoteConverter converter;

    /** an instrumentController field which allows us to talk to the InstrumentPanel */
    @FXML
    private  InstrumentPanelController instrumentPaneController;

    /**
     * initializes the controller
     */
    @FXML
    public void initialize() {
        this.composition = new Composition();
        this.clickInPanelHandler = new ClickInPanelHandler(this.compositionPanel);
        this.dragInPanelHandler = new DragInPanelHandler(this.compositionPanel);
        this.converter = new RectangleToNoteConverter(this.compositionPanel,this.composition);
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
            if (isPlaying) {
                this.stopComposition();
            } else {
                this.clickInPanelHandler.handle(event, instrumentPaneController.getSelectedInstrument());
            }
        }
    }

    /**
     * handles when the mouse is pressed
     */
    @FXML
    public void handleMousePressed(MouseEvent event) {
        this.dragInPanelHandler.handleMousePressed(event);
    }

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
     * Instantiates the line and transition fields and begins the animation based on
     * the length of the composition.
     */
    public void beginAnimation() {
        ArrayList<NoteRectangle> rectangles = this.compositionPanel.getRectangles();
        double maxX = 0;
        for(NoteRectangle rectangle: rectangles){
            maxX = Math.max(maxX, rectangle.getX() + rectangle.getWidth());
        }
        this.line = new Line(0, 0, 0, 1280);
        this.line.setId("playLine");
        this.compositionPanel.getChildren().add(this.line);
        this.transition = new TranslateTransition(new Duration(maxX * 10), this.line);
        this.transition.setToX(maxX);
        this.transition.setInterpolator(Interpolator.LINEAR);
        this.transition.play();
        this.transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                compositionPanel.getChildren().remove(line);
            }
        });
    }

    /**
     * Stops the animation and removes the line from the composition panel.
     */
    public void stopAnimation() {
        this.transition.stop();
        this.compositionPanel.getChildren().remove(line);
    }

    /**
     * Plays the composition and initiates the animation.
     * Stops the current animation and plays a new one if one already exists.
     */
    @FXML
    public void playComposition() {
        this.isPlaying = true;

        if (this.transition != null) {
            this.composition.stop();
        }
        //only plays when there are rectangles
        if (this.compositionPanel.getRectangles().size() > 0) {
            this.converter.loadComposition();
            this.beginAnimation();
            this.composition.play();
        }
    }

    /**
     * Stops and clears the composition and destroys the animation if there is one.
     */
    @FXML
    public void stopComposition() {
        this.isPlaying = false;

        if (this.transition != null) {
            this.stopAnimation();
        }
        this.composition.stop();
    }

    /**
     * Ensures that all processes are killed on the
     * destruction of the window.
     */
    @FXML
    public void cleanUpOnExit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * deletes the selected notes
     */
    public void deleteSelectedNotes() {
        this.compositionPanel.deleteSelectedNotes();

    }

    /**
     * selects all the notes
     */
    public void selectAllNotes() {
        this.compositionPanel.selectAllNotes();
    }

}
