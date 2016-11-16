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

    /** an actionMenuController field which allows us to talk with the actionMenu */
    @FXML
    private ActionMenuController actionMenuController;

    /** a controller to assist in bindings between the menus and controllers*/
    private BindingController bindingController;

    private ClipBoardController clipboardController;


    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void initialize() {
        this.compositionPanelController.init(this);
        this.clipboardController = new ClipBoardController(
                this.compositionPanelController,
                this.instrumentPaneController
        );
        this.bindingController = new BindingController(
                this,
                this.compositionPanelController,
                this.clipboardController);
        this.editMenuController.init(this.compositionPanelController,
                                     this.bindingController,
                                     this.clipboardController);
        this.actionMenuController.init(this.compositionPanelController,
                                       this.bindingController);
    }

    /** Returns the currently selected instrument */
    public Instrument getSelectedInstrument() {
        return this.instrumentPaneController.getSelectedInstrument();
    }
}

