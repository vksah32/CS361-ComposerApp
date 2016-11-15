/**
 * File: Controller.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */

package proj7ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import proj7ZhouRinkerSahChistolini.Models.Instrument;

/**
 * Handles menu GUI interactions with other controllers
 */
public class Controller {

    /** The compositionPanel object that is modified */
    @FXML
    public CompositionPanelController compositionPanelController;

    /** an instrumentController field which allows us to talk to the InstrumentPanel */
    @FXML
    private InstrumentPanelController instrumentPaneController;

    /** an editMenuController field which allows us to talk to the editMenu */
    @FXML
    private EditMenuController editMenuController;

    /** a controller to assist in bindings between the menus and controllers*/
    private BindingController bindingController;

    private ClipBoardController clipboardController;

    /**
     * All of our MenuItem are put into fields
     * Each of the following FXML elements are MenuItems defined in
     * our fxml. They are injected here into our controller so that
     * we can bind their disable properties
     */
    @FXML
    private MenuItem stopButton;
    @FXML
    public MenuItem startButton;

    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void initialize() {
        this.compositionPanelController.init(this);
        this.clipboardController = new ClipBoardController(
                this.compositionPanelController,
                this.compositionPanelController.getActionController());
        this.bindingController = new BindingController(
                this,
                this.compositionPanelController,
                this.clipboardController);
        this.editMenuController.init(this.compositionPanelController,
                                     this.bindingController,
                                     this.clipboardController);
        bindMenuItems();
    }

    /** Returns the currently selected instrument */
    public Instrument getSelectedInstrument() {
        return this.instrumentPaneController.getSelectedInstrument();
    }

    @FXML
    public void playComposition() { this.compositionPanelController.playComposition(); }

    @FXML
    public void stopComposition() { this.compositionPanelController.stopComposition(); }

    /**
     * Sets up the bindings for the menuButtons in order to disable them
     */
    public void bindMenuItems() {
        //stopButton
        this.stopButton.disableProperty().bind(
                this.compositionPanelController.getTempoLine().isPlayingProperty().not()
        );
        //startButton
        this.startButton.disableProperty().bind(
                this.bindingController.getChildrenProperty().sizeProperty().isEqualTo(0)
        );
    }
}

