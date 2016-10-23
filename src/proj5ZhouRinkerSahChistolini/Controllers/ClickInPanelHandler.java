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

package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.input.MouseEvent;
import proj5ZhouRinkerSahChistolini.Models.Instrument;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.NoteRectangle;

/**
 * Handles when we click in the panel
 */
public class ClickInPanelHandler {

    /** The "main" composition Controller */
    private CompositionPanelController compController;

    /** the default rectangle width */
    private final int DEFAULT_RECTANGLE_WIDTH = 100;

    /** a boolean field that keeps track of whether a meta/shortcut is pressed */
    private boolean isMetaDown;

    /**
     * Creates a new ClickInNoteHandler
     * @param compController the main composition Controller
     */
    public ClickInPanelHandler(CompositionPanelController compController) {
        this.compController = compController;
    }

    /**
     * handles when clicking in the panel
     * @param event
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
        DragInNoteHandler handler = new DragInNoteHandler(rectangle, this.compController);
        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        rectangle.setOnMousePressed(handler::handleMousePressed);
        rectangle.setOnMouseDragged(handler::handleDragged);
        rectangle.setOnMouseReleased(handler::handleMouseReleased);
        if (!isMetaDown) {
            this.compController.clearSelected();
        }
        rectangle.setOnMouseClicked(new ClickInNoteHandler(this.compController));
        Note note = new Note(instrument);
        note.pitchProperty().bind(rectangle.yProperty().divide(10) );
        note.durationProperty().bind(rectangle.widthProperty());
        note.startTickProperty().bind(rectangle.xProperty());
        rectangle.setNote(note);
        this.compController.addNoteRectangle(rectangle, true);
        this.compController.addNoteToComposition(note);


    }
}


