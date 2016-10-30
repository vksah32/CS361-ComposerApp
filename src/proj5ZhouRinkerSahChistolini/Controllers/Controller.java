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

package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import proj5ZhouRinkerSahChistolini.Models.Instrument;

/**
 * Handles all user GUI interactions and coordinates with the MidiPlayer
 * and Composition.
 */
public class Controller {

    /** The compositionPanel object that is modified */
    @FXML
    public CompositionPanelController compositionPanelController;

    /** an instrumentController field which allows us to talk to the InstrumentPanel */
    @FXML
    private InstrumentPanelController instrumentPaneController;

    /**
     * All of our MenuItem are put into fields
     */
    @FXML
    private MenuItem stopButton;
    @FXML
    public MenuItem startButton;
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem groupButton;
    @FXML
    private MenuItem ungroupButton;


    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void initialize() {
        this.compositionPanelController.init(this);
        bindMenuItems();
    }

    /** Returns the currently selected instrument */
    public Instrument getSelectedInstrument() {
        return this.instrumentPaneController.getSelectedInstrument();
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

    @FXML
    public void playComposition() { this.compositionPanelController.playComposition(); }

    @FXML
    public void stopComposition() { this.compositionPanelController.stopComposition(); }

    @FXML
    /**
     * deletes the selected notes
     */
    public void deleteSelectedNotes() {
        this.compositionPanelController.deleteSelectedNotes();
    }

    @FXML
    /**
     * selects all the notes
     */
    public void selectAllNotes() {
        this.compositionPanelController.selectAllNotes();
    }

    @FXML
    /**
     * group the selected notes
     */
    public void groupSelected() { this.compositionPanelController.groupSelected(); }

    @FXML
    /**
     * ungroup the selected notes
     */
    public void ungroupSelected() { this.compositionPanelController.ungroupSelected(); }

    @FXML
    /**
     * undoes the latest action
     */
    public void undo() { return; } //put stuff here

    @FXML
    /**
     * redoes the latest undo action
     */
    public void redo() { return; } //put stuff here too

    /**
     * returns whether or not the composition is playing
     */
    public void bindMenuItems() {
        this.stopButton.disableProperty().bind(
                this.compositionPanelController.getIsPlayingProperty().not()
        );
        this.startButton.disableProperty().bind(
                this.compositionPanelController.getChildrenProperty().sizeProperty().isEqualTo(1)
        );
    }
}
