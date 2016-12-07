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

package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import proj8ZhouRinkerSahChistolini.Controllers.Actions.AddNoteAction;
import proj8ZhouRinkerSahChistolini.Models.Note;
import proj8ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * Handles when we click in the panel
 */
public class ClickInPanelHandler {

    /** The "main" composition Controller */
    private CompositionPanelController compController;

    /** the instrument panel controller */
    private InstrumentPanelController instController;

    /** the default rectangle width */
    private final int DEFAULT_RECTANGLE_WIDTH = 100;

    /** a boolean field that keeps track of whether a meta/shortcut is pressed */
    private boolean isMetaDown;

    /** selection states before click */
    private Collection<SelectableRectangle> before;

    /**
     * Creates a new ClickInNoteHandler
     * @param compController the main composition Controller
     */
    public ClickInPanelHandler(CompositionPanelController compController,
                               InstrumentPanelController instController) {
        this.compController = compController;
        this.instController = instController;
    }

    /**
     * handles when clicking in the panel
     * @param event
     */
    public void handle(MouseEvent event, int instId) {
        if (event.getButton() == MouseButton.SECONDARY){ return; }
        this.before = (
                this.compController.getSelectedRectangles()
        );
        this.isMetaDown = event.isShortcutDown();
        if (!isMetaDown) {
            this.compController.clearSelected();
        }
        addNote(event.getX()/this.compController.getZoomFactor().getValue(), event.getY()/this.compController.getZoomFactor().getValue()
                , this.DEFAULT_RECTANGLE_WIDTH, instId);
    }

    /**
     * Creates a note at the given x and y coordinates
     * and adds the note bar (Rectangle) to the CompositionPanel.
     *
     * @param x mouse x location
     * @param y mouse y location
     * @param instId the instrument object's id
     */
    public NoteRectangle addNoteRectangle(double x,
                                          double y,
                                          int instId){
        double pitch = Math.floor((y - 1) / 10) * 10 + 1;

        NoteRectangle rectangle = new NoteRectangle(x, pitch,
                this.DEFAULT_RECTANGLE_WIDTH,
                10,
                this.instController.getInstrument(instId).getValue(),
                this.instController.getInstrument(instId).getName(),
                this.instController
        );

        DragInNoteHandler handler = new DragInNoteHandler(rectangle, this.compController);

        // sets the handlers of these events to be the
        // specified methods in its DragInNoteHandler object
        rectangle.setOnMousePressed(handler::handleMousePressed);
        rectangle.setOnMouseDragged(handler::handleDragged);
        rectangle.setOnMouseReleased(handler::handleMouseReleased);
        rectangle.setOnMouseClicked(new ClickInNoteHandler(this.compController));

        // Create right click menu handlers
        ContextMenuFactory factory = new ContextMenuFactory(this.compController, rectangle);
        ContextMenu menu = factory.createPlayableRightClickMenu();
        factory.setUpListeners(menu);
        return rectangle;
    }

    /**
     * creates new Note and binds it to corresponding Note Rectangle
     * @param rectangle Note Rectangle to bind to
     * @param instId selected instrument
     * @return new Note
     */
    public Note createBoundNote(NoteRectangle rectangle, int instId){
        Note note = new Note(this.instController.getInstrument(instId));
        note.intrValProperty().addListener(e -> {
            note.setInstrument(
                    instController.getInstrument(note.getInstrumentValue())
            );
        });
        bindNotetoRectangle(note, rectangle);

        return note;
    }
    /**
     * Creates a note at the given x and y coordinates
     * and adds the note bar (Rectangle) to the CompositionPanel.
     *
     * @param x mouse x location
     * @param y mouse y location
     * @param instId the instrument object's id
     */
    public void addNote(double x, double y, double width, int instId) {
        NoteRectangle rectangle = this.addNoteRectangle(x,y,instId);
        Note note = createBoundNote(rectangle, instId);

        this.compController.addNoteRectangle(rectangle, true);
        this.compController.addNoteToComposition(note);

        //add the undoable action
        AddNoteAction actionPreformed = new AddNoteAction(
                rectangle,
                note,
                this.before,
                this.isMetaDown,
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
        note.intrValProperty().bind(rectangle.instrumentProperty());
        rectangle.volumeProperty().bind(note.volumeProperty());
    }
}



