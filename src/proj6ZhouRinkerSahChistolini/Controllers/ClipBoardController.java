package proj6ZhouRinkerSahChistolini.Controllers;
import proj6ZhouRinkerSahChistolini.Models.Instrument;
import proj6ZhouRinkerSahChistolini.Models.Note;
import proj6ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj6ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * Manages the cut or copied data to be added
 * to or from cleared the clip board.
 *
 */
public class ClipBoardController {
    /** The Clipboard which all the data is sent to */
    private Clipboard board;
    /** A reference to the main compositionController */
    private CompositionPanelController compController;
    /** A reference to the actionController */
    private ActionController actionController;

    /**
     * Generates a new ClipBoardController initializing the references
     * needed to perform clipboard actions
     * @param compController the associated CompositionPanelController
     * @param actionController the associated ActionController
     */
    public ClipBoardController(CompositionPanelController compController,
                               ActionController actionController){
        this.compController = compController;
        this.actionController = actionController;
        this.board =  Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * returns a String which has been copied to the clipboard.
     * Throws an error if the copied element is unsupported
     * @return result the result String
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public String getClipboardContent() throws IOException, UnsupportedFlavorException  {
            String result = (String) this.board.getData(DataFlavor.stringFlavor);
            return result;
    }


    /**
     *
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public void pasteSelected() throws IOException, UnsupportedFlavorException {
        this.compController.clearSelected();
        String stringNotes = this.getClipboardContent();
        parseString(stringNotes, new ArrayList<>());
    }

    /**
     * parsesThrough a string of Notes and adds them to the compositionPanel
     * @param stringNotes the String representation of Notes
     * @param alreadySelected a collection of alreadyselected notes
     */
    public void parseString(String stringNotes,
                            Collection<SelectableRectangle> alreadySelected){
        int x = stringNotes.indexOf('{');
        if (x != -1 ) {
            int y = stringNotes.lastIndexOf('}');

            String k = stringNotes.substring(0, x);
            String j = stringNotes.substring(y+1);
            parseString(stringNotes.substring(x + 1, y), alreadySelected);
            this.compController.groupSelected(this.compController.getSelectedRectangles());

            if (k.length() > 0) {
                addNotesFromString(k);
            }
            if (j.length() > 0) {
                addNotesFromString(j);
            }

        } else {
            for (String stringNote : stringNotes.split("\n")) {
                if (stringNote.length() > 0) {
                    String noteParameters[] = stringNote.split("\\s+");
                    for (String s : noteParameters) {
                    }
                    addPastedNotes(noteParameters);
                }
            }
        }
    }

    /**
     * adds a note to the CompositionPane from a Note String representation
     * @param note the String representation of the Note object
     */
    private void addNotesFromString(String note) {
        for (String stringNote : note.split("\n")) {
            if (stringNote.length() > 0) {
                String noteParameters[] = stringNote.split("\\s+");
                addPastedNotes(noteParameters);
            }
        }
    }

    /**
     * adds a
     * @param noteParameters a list of note parameters needed to reconstruct
     *                       the note. The parameters are as follows:
     *                       [xPosition, yPosition, Width, Instrument Name,
     *                       Instrument channel, instrument value]
     */
    public void addPastedNotes(String noteParameters[]){
        Instrument instrument = new Instrument(noteParameters[3],
                                               Integer.parseInt(noteParameters[5]),
                                               Integer.parseInt(noteParameters[4]));

        double xVal = Double.parseDouble(noteParameters[0]);
        double yVal = Double.parseDouble(noteParameters[1]);
        double width = Double.parseDouble(noteParameters[2]);

        NoteRectangle rectangle = this.compController.getClickInPanelHandler()
                                                     .addNoteRectangle(xVal,
                                                                       yVal,
                                                                       width,
                                                                       instrument);
        Note note = this.compController.getClickInPanelHandler().addBoundNote(rectangle,
                                                                              instrument);
        this.compController.addNotestoMap(note,rectangle);
        this.compController.addRectangle(rectangle,true);
        this.compController.addNoteToComposition(note);
    }

    /**
     * Generates a String representation of the selected notes and
     * adds them to the clipboard
     */
    public void copySelected(){
        String mainString = new String();

        //Collection<Note> selectedCompNote = this.getSelectedNotes();
        Collection<SelectableRectangle> noteRecs =  (
                this.compController.getSelectedRectangles()
        );
        for (SelectableRectangle sr : noteRecs){
            if(!sr.xProperty().isBound()) {
                mainString += this.compController.generateString(sr);
            }
        }
        this.addStringContent(mainString);
    }


    /**
     * adds new strings of data to the clip board
     *
     * @param cutCopyData string data to be added to the clipboard
     *
     */
    public void addStringContent(String cutCopyData){
        Transferable transferable = new StringSelection(cutCopyData);
        board.setContents(transferable, null);

    }
}
