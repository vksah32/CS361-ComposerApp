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

package proj10ZhouRinkerSahChistolini.Controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import proj10ZhouRinkerSahChistolini.Controllers.Actions.SelectAction;
import proj10ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

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
     * @param event the mouse click
     *
     */
    public void handle(MouseEvent event) {
        if (event.isStillSincePress()) {
            Collection<SelectableRectangle> before = (
                this.compController.getSelectedRectangles()
            );

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

            // add a new action
            Collection<SelectableRectangle> after = (
                this.compController.getSelectedRectangles()
            );
            if (!before.equals(after)) {
                this.compController.addAction(
                    new SelectAction(before, after, this.compController)
                );
            }
            this.compController.getPropPanelController().populatePropertyPanel();
            //stop the event so that the border pane doesn't get the action
            event.consume();
        }
    }
}
