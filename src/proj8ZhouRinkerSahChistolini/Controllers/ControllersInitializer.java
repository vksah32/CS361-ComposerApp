/**
 * File: ControllersInitializer.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */

package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;

/**
 * Handles menu GUI interactions with other controllers
 */
public class ControllersInitializer {

    /** The compositionPanel object that is modified */
    @FXML
    public CompositionPanelController compositionPanelController;

    /** an instrumentController field which allows us to talk to the InstrumentPanel */
    @FXML
    private InstrumentPanelController instrumentPaneController;

    /** an fileMenuController field which allows us to talk to the editMenu */
    @FXML
    private FileMenuController fileMenuController;

    /** an editMenuController field which allows us to talk to the editMenu */
    @FXML
    private EditMenuController editMenuController;

    /** an actionMenuController field which allows us to talk with the actionMenu */
    @FXML
    private ActionMenuController actionMenuController;


    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void initialize() {
        //Initialize CompositionPanelController
        this.compositionPanelController.init(this.instrumentPaneController);

        // Initialize the non-fxml tools
        XMLHandler XMLhandler = new XMLHandler(this.compositionPanelController);
        ClipBoardController clipboardController = new ClipBoardController(
                this.compositionPanelController,
                XMLhandler
        );
        BindingController bindingController = new BindingController(
                this.compositionPanelController,
                clipboardController
        );;

        //Set up the Menu Controllers with the needed tools
        initializeMenuControllers(XMLhandler, bindingController, clipboardController);
    }

    /**
     * Initializes the FileMenuController providing them with the
     * necessary connections
     * @param XMLhandler the global XMLhandler
     * @param bindingController  the global binding controller
     * @param clipboardController the global clipboard controller
     */
    private void initializeMenuControllers(XMLHandler XMLhandler,
                                           BindingController bindingController,
                                           ClipBoardController clipboardController) {
        //File Menu ControllersInitializer init
        this.fileMenuController.init(
                this.compositionPanelController,
                XMLhandler
        );
        //Initialize Edit ControllersInitializer
        this.editMenuController.init(
                this.compositionPanelController,
                bindingController,
                clipboardController
        );
        //Initialize Action ControllersInitializer
        this.actionMenuController.init(this.compositionPanelController,
                bindingController);
    }
}

