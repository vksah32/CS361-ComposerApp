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

package proj6ZhouRinkerSahChistolini.Controllers;

import javafx.scene.input.MouseEvent;
import proj6ZhouRinkerSahChistolini.Controllers.Actions.AddNoteAction;
import proj6ZhouRinkerSahChistolini.Models.Instrument;
import proj6ZhouRinkerSahChistolini.Models.Note;
import proj6ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj6ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

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
        this.isMetaDown = event.isShortcutDown();
        if (!isMetaDown) {
            this.compController.clearSelected();
        }
        addNote(event.getX(), event.getY(), this.DEFAULT_RECTANGLE_WIDTH,instrument);
    }


    public NoteRectangle addNoteRectangle(double x, double y, double width, Instrument instrument){
        double pitch = Math.floor((y - 1) / 10) * 10 + 1;

        NoteRectangle rectangle = new NoteRectangle(x, pitch,
                width,
                10, instrument.getColor(), instrument.getValue());

        DragInNoteHandler handler = new DragInNoteHandler(rectangle, this.compController);

        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        rectangle.setOnMousePressed(handler::handleMousePressed);
        rectangle.setOnMouseDragged(handler::handleDragged);
        rectangle.setOnMouseReleased(handler::handleMouseReleased);
        rectangle.setOnMouseClicked(new ClickInNoteHandler(this.compController));

        return rectangle;
    }

    public Note addBoundNote(NoteRectangle rectangle, Instrument instrument){
        Note note = new Note(instrument);
        bindNotetoRectangle(note, rectangle);

        return note;
    }
    /**
     * Creates a note at the given x and y coordinates
     * and adds the note bar (Rectangle) to the CompositionPanel.
     *
     * @param x mouse x location
     * @param y mouse y location
     * @param instrument the instrument object
     */
    public void addNote(double x, double y, double width, Instrument instrument) {
        Collection<SelectableRectangle> selectionStatusBeforeAdd = this.compController.getSelectedRectangles();

        NoteRectangle rectangle = this.addNoteRectangle(x,y,width,instrument);
        Note note = addBoundNote(rectangle, instrument);

        this.compController.addNoteRectangle(rectangle, true);
        this.compController.addNoteToComposition(note);

        this.compController.addNotestoMap(note,rectangle);
        //add the undoable action
        AddNoteAction actionPreformed = new AddNoteAction(
                rectangle,
                note,
                selectionStatusBeforeAdd,
                isMetaDown,
                this.compController
        );
        this.compController.addAction(actionPreformed);
    }

    /**
     * bind a note's properties to rectangle's properties
     * @param note note which to be binded
     * @param rectangle rectangle to bind the note to
     *
     */
    private void bindNotetoRectangle(Note note, NoteRectangle rectangle) {
        note.pitchProperty().bind(rectangle.yProperty() );
        note.durationProperty().bind(rectangle.widthProperty());
        note.startTickProperty().bind(rectangle.xProperty());
        note.selectedProperty().bind(rectangle.selectedProperty());

    }
}



