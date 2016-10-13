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

package proj4MalionekLamChistoliniSah;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handles when we click in a note
 */
public class ClickInNoteHandler implements EventHandler<MouseEvent> {

    /** The compositionPanel object that this handler modifies */
    private CompositionPanel compositionPanel;

    /**
     * Creates a new ClickInNoteHandler
     * @param compositionPanel
     */
    public ClickInNoteHandler(CompositionPanel compositionPanel) {
        this.compositionPanel = compositionPanel;
    }

    /**
     * handles when clicking in a note
     * @param event
     */
    public void handle(MouseEvent event) {
        NoteRectangle rect = ((NoteRectangle) event.getSource());
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
                this.compositionPanel.clearSelected();
                rect.setSelected(true);
            }
        }
        //So that the border
        event.consume();
    }
}
