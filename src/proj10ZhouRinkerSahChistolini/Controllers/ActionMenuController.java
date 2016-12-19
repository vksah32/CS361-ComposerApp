/**
 * File: ActionMenuController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Nov 14, 2016
 */
package proj10ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Controller for the action menu.
 */
public class ActionMenuController {

    /**
     * All of our MenuItem are put into fields
     * Each of the following FXML elements are MenuItems defined in
     * our fxml. They are injected here into our controller so that
     * we can bind their disable properties
     */
    @FXML
    //field to control the stop button
    private MenuItem stopButton;
    @FXML
    //filed to control the start button
    public MenuItem startButton;

    /** reference to the CompositionPanelController */
    private CompositionPanelController compositionPanelController;
    /** reference to the BindingController */
    private BindingController bindingController;

    /**
     * Sets up the references to the necessary controllers this minion needs
     * to talk to
     */
    public void init(CompositionPanelController compController,
                     BindingController bindingController) {
        this.compositionPanelController = compController;
        this.bindingController = bindingController;
        this.bindMenuItems();
    }

    @FXML
    /**
     * Plays the composition
     */
    public void playComposition() { this.compositionPanelController.playComposition(); }

    @FXML
    /**
     * Stops the composition from playing
     */
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
