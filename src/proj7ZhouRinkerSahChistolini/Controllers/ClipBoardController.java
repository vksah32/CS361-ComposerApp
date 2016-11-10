package proj7ZhouRinkerSahChistolini.Controllers;
import proj7ZhouRinkerSahChistolini.Models.Instrument;
import proj7ZhouRinkerSahChistolini.Models.Note;
import proj7ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj7ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj7ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private ArrayList<ArrayList<SelectableRectangle>> parseStack;

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
        this.parseStack = new ArrayList<>();
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
        String[] lines = stringNotes.split("\n");
        Collection<SelectableRectangle> temp = this.compController.getRectangles();
        this.parseStack = new ArrayList<>();
        parseString(lines, 0, 0);
        for(SelectableRectangle rec : this.compController.getRectangles()){
            if(!temp.contains(rec)){
                rec.setSelected(true);
            }
        }
    }

    /**
     * parsesThrough a string list and adds groups and notes to the compositionPanel
     * @param lines the String representation of Notes
     */
    public int parseString(String[] lines, int add, int brackets){
        for(int i=0; i<lines.length; i++){
            if (lines[i].equals("{")) {
                this.parseStack.add(new ArrayList<>());
                int skip = parseString(Arrays.copyOfRange(lines, i+1, lines.length), i, brackets+1);
                i += skip;
                if (brackets < 1 ){this.compController.clearSelected();}
            } else if (lines[i].equals("}")){
                GroupRectangle temp = this.compController.createGroupRectangle(this.parseStack.get(brackets - 1));
                if(brackets > 1){
                    this.parseStack.get(brackets-2).add(temp);
                }
                this.parseStack.get(brackets-1).clear();
                return i+1;
            } else {
                if (lines[i].length() > 0) {
                    if(brackets > 0){
                        this.parseStack.get(brackets -1).add(addNotesFromString(lines[i]));
                        this.compController.clearSelected();
                    }else{
                        addNotesFromString(lines[i]);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * adds a note to the CompositionPane from a Note String representation
     * @param note the String representation of the Note object
     */
    private NoteRectangle addNotesFromString(String note) {
        NoteRectangle rec = null;
        for (String stringNote : note.split("\n")) {
            if (stringNote.length() > 0) {
                String noteParameters[] = stringNote.split("\\s+");
                rec = addPastedNotes(noteParameters);
            }
        }
        return rec;
    }

    /**
     * adds a
     * @param noteParameters a list of note parameters needed to reconstruct
     *                       the note. The parameters are as follows:
     *                       [xPosition, yPosition, Width, Instrument Name,
     *                       Instrument channel, instrument value]
     */
    public NoteRectangle addPastedNotes(String noteParameters[]){
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
        return rectangle;
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
