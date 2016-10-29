/**
 * File: ClickInNoteHandler.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * Handles when we click in a note
 */
public class ClickInNoteHandler implements EventHandler<MouseEvent> {

    /** The main compositionController */
    private CompositionPanelController compController;

    /**
     * Creates a new ClickInNoteHandler
     * @param compController the main composition controller
     */
    public ClickInNoteHandler(CompositionPanelController compController) {
        this.compController = compController;
    }

    /**
     * handles when clicking in a note
     * @param event
     */
    public void handle(MouseEvent event) {
        SelectableRectangle rect = ((SelectableRectangle) event.getSource());
        // control-clicking
        if (event.isShortcutDown()) {
            if (rect.isSelected()) {
                rect.setSelected(false);
            } else {
                rect.setSelected(true);
            }
        }
        // clicking
        else {
            if (!rect.isSelected()) {
                this.compController.clearSelected();
                rect.setSelected(true);
            }
        }
        //So that the border
        event.consume();
    }
}
