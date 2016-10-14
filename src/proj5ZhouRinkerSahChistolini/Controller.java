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
import javafx.application.Platform;

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


    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void initialize() {
        this.compositionPanelController.init(this);
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

    /**
     * deletes the selected notes
     */
    public void deleteSelectedNotes() {
        this.compositionPanelController.deleteSelectedNotes();
    }

    /**
     * selects all the notes
     */
    public void selectAllNotes() {
        this.compositionPanelController.selectAllNotes();
    }
}