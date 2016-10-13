/**
 * File: ClickInPanelHandler.java
 *
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini;

import javafx.scene.input.MouseEvent;

/**
 * Handles when we click in the panel
 */
public class ClickInPanelHandler {

    /** The panelToEdit object that this handler edits */
    CompositionPanel panelToEdit;

    /** the default rectangle width */
    private final int DEFAULT_RECTANGLE_WIDTH = 100;

    /** a boolean field that keeps track of whether a meta/shortcut is pressed */
    private boolean isMetaDown;

    /**
     * Creates a new ClickInNoteHandler
     * @param panelToEdit
     */
    public ClickInPanelHandler(CompositionPanel panelToEdit) {
        this.panelToEdit = panelToEdit;
    }

    /**
     * handles when clicking in the panel
     * @param event
     * @param instrument
     */
    public void handle(MouseEvent event, Instrument instrument) {
        isMetaDown = event.isShortcutDown();
        addNote(event.getX(), event.getY(), instrument);
    }

    /**
     * Creates a note at the given x and y coordinates
     * and adds the note bar (Rectangle) to the CompositionPanel.
     *
     * @param x mouse x location
     * @param y mouse y location
     */
    public void addNote(double x, double y, Instrument instrument) {
        double pitch = Math.floor((y - 1) / 10) * 10 + 1;
        NoteRectangle rectangle = new NoteRectangle(x, pitch,
                this.DEFAULT_RECTANGLE_WIDTH,
                10, instrument);
        DragInNoteHandler handler = new DragInNoteHandler(this.panelToEdit, rectangle);
        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        rectangle.setOnMousePressed(handler::handleMousePressed);
        rectangle.setOnMouseDragged(handler::handleDragged);
        rectangle.setOnMouseReleased(handler::handleMouseReleased);
        if (!isMetaDown) {
            this.panelToEdit.clearSelected();
        }
        rectangle.setOnMouseClicked(new ClickInNoteHandler(this.panelToEdit));
        this.panelToEdit.addRectangle(rectangle, true);
    }
}



