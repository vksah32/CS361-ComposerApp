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

package proj10ZhouRinkerSahChistolini.Controllers;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import proj10ZhouRinkerSahChistolini.Controllers.Actions.AddNoteAction;
import proj10ZhouRinkerSahChistolini.Models.Note;
import proj10ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj10ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * Handles when we click in the panel
 */
public class ClickInPanelHandler {

    /** The "main" composition Controller */
    private CompositionPanelController compController;

    /** the instrument panel controller */
    private InstrumentPanelController instController;

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
     * @param instId
     * @param width the width of the note
     * @param volume the volume of the note
     */
    public void handle(MouseEvent event, int instId, int width, int volume) {
        if (event.getButton() == MouseButton.SECONDARY){ return; }
        this.before = (
                this.compController.getSelectedRectangles()
        );
        this.isMetaDown = event.isShortcutDown();
        if (!isMetaDown) {
            this.compController.clearSelected();
        }
        addNote(event.getX()/this.compController.getZoomFactor().getValue(),
                event.getY()/this.compController.getZoomFactor().getValue()
                , width, instId, volume
        );

        this.compController.getPropPanelController().populatePropertyPanel();

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
                                          int instId,
                                          int width){
        double pitch = Math.floor((y - 1) / 10) * 10 + 1;

        int instPosition = instController.getInstrument(instId).getId();
        NoteRectangle rectangle = new NoteRectangle(x, pitch,
                width,
                10,
                this.instController.getInstrument(instId).getValue(),
                this.instController.getStyleMappings().get(instPosition),
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
     * @param volume the volume of the note
     * @return new Note
     */
    public Note createBoundNote(NoteRectangle rectangle, int instId, int volume){
        Note note = new Note(this.instController.getInstrument(instId), volume);
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
     * @param volume the volume of the note
     */
    public void addNote(double x, double y, int width, int instId, int volume) {
        NoteRectangle rectangle = this.addNoteRectangle(x,y,instId, width);
        Note note = createBoundNote(rectangle, instId, volume);

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



