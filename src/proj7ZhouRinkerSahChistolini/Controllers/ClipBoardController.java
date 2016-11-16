/**
 * File: CompositionPanelController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Nov 15, 2016
 */

package proj7ZhouRinkerSahChistolini.Controllers;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import proj7ZhouRinkerSahChistolini.Models.Note;
import proj7ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj7ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj7ZhouRinkerSahChistolini.Views.SelectableRectangle;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.io.StringReader;
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
    /**
     * The Clipboard which all the data is sent to
     */
    private Clipboard board;
    /**
     * A reference to the main compositionController & instrument controller
     */
    private CompositionPanelController compController;
    private InstrumentPanelController instController;

    private ArrayList<ArrayList<SelectableRectangle>> parseStack;

    /**
     * Generates a new ClipBoardController initializing the references
     * needed to perform clipboard actions
     *
     * @param compController the associated CompositionPanelController
     * @param instController the associated instrumentpanelcontroller
     */
    public ClipBoardController(CompositionPanelController compController,
                               InstrumentPanelController instController) {
        this.compController = compController;
        this.instController = instController;
        this.board = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.parseStack = new ArrayList<>();
    }

    /**
     * returns a String which has been copied to the clipboard.
     * Throws an error if the copied element is unsupported
     *
     * @return result the result String
     */
    public String getClipboardContent() {
        try {
            return String.valueOf(this.board.getData(DataFlavor.stringFlavor));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * pastes the clipboard's contents onto the Pane
     */
    public void pasteSelected() {
        this.compController.clearSelected();
        String stringNotes = this.getClipboardContent();
        String[] lines = stringNotes.split("\n");
        Collection<SelectableRectangle> temp = this.compController.getRectangles();
        this.parseStack = new ArrayList<>();

        // Set up a try-catch block in order to safely fail when the clipboard has
        // an unmatched type
        try {
            parseString(lines, 0);
        } catch (Exception e) {
            return;
        }
        for (SelectableRectangle rec : this.compController.getRectangles()) {
            if (!temp.contains(rec)) {
                rec.setSelected(true);
            }
        }
    }

    /**
     * parsesThrough a string list and adds groups and notes to the compositionPanel
     *
     * @param lines the String representation of Notes
     */
    public int parseString(String[] lines, int brackets) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].equals("<GroupRectangle>")) {
                this.parseStack.add(new ArrayList<>());
                int skip = parseString(Arrays.copyOfRange(lines, i + 1, lines.length), brackets + 1);
                i += skip;
                if (brackets < 1) {
                    this.compController.clearSelected();
                }
            } else if (lines[i].equals("</GroupRectangle>")) {
                GroupRectangle temp = this.compController.createGroupRectangle(this.parseStack.get(brackets - 1));
                if (brackets > 1) {
                    this.parseStack.get(brackets - 2).add(temp);
                }
                this.parseStack.get(brackets - 1).clear();
                return i + 1;
            } else {
                if (lines[i].length() > 0) {
                    if (brackets > 0) {
                        this.parseStack.get(brackets - 1).add(addNotesFromString(lines[i]));
                        this.compController.clearSelected();
                    } else {
                        addNotesFromString(lines[i]);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * adds a note to the CompositionPane from a Note String representation
     *
     * @param note the String representation of the Note object
     */
    private NoteRectangle addNotesFromString(String note) {
        NoteRectangle rec = null;
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            SAXNoteHandler handler = new SAXNoteHandler();
            parser.parse(new InputSource(new StringReader(note)), handler);
            rec = handler.getRectangle();
        } catch (Exception e) {
            //Don't add a rectangle if it can't be parsed
            System.out.println("ERROR When Pasting: " + e);
        }
        Note n = this.compController.getClickInPanelHandler().addBoundNote(rec,
                rec.getInstrument());
        this.compController.addNotestoMap(n, rec);
        this.compController.addRectangle(rec, true);
        this.compController.addNoteToComposition(n);
        return rec;
    }

    /**
     * Generates a String representation of the selected notes and
     * adds them to the clipboard
     */
    public void copySelected() {
        String mainString = new String();

        //Collection<Note> selectedCompNote = this.getSelectedNotes();
        Collection<SelectableRectangle> noteRecs = (
                this.compController.getSelectedRectangles()
        );
        for (SelectableRectangle sr : noteRecs) {
            if (!sr.xProperty().isBound()) {
                mainString += sr.toString();
            }
        }
        this.addStringContent(mainString);
    }

    /**
     * adds new strings of data to the clip board
     *
     * @param cutCopyData string data to be added to the clipboard
     */
    public void addStringContent(String cutCopyData) {
        Transferable transferable = new StringSelection(cutCopyData);
        board.setContents(transferable, null);
    }

    /**
     * The SAX event handler which handles parsing through
     * our saved note strings
     */
    private class SAXNoteHandler extends DefaultHandler {
        private NoteRectangle rect;

        @Override
        //This is triggered when the start of a tag is found
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            switch (qName) {
                case "NoteRectangle":
                    this.rect = compController.getClickInPanelHandler()
                                              .addNoteRectangle(
                            Double.parseDouble(attributes.getValue("xpos")),
                            Double.parseDouble(attributes.getValue("ypos")),
                            Double.parseDouble(attributes.getValue("width")),
                            instController.getInstrument(Integer.parseInt(
                                    attributes.getValue("instValue"))
                            )
                    );
            }
        }

        /**
         * returns the contents of the rectangle field
         * @return NoteRectangle rect
         */
        public NoteRectangle getRectangle() {
            return this.rect;
        }
    }
}

