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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

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

        Collection<SelectableRectangle> temp = this.compController.getRectangles();

        // Set up a try-catch block in order to safely fail when the clipboard has
        // an unmatched type
        try {
            stringToComposition(stringNotes);
        } catch (Exception e) {
            return;
        }
        for (SelectableRectangle rec : this.compController.getRectangles()) {
            if (!temp.contains(rec)) {
                rec.setSelected(true);
            }
        }
    }

    /** Initiates a parse on a given string to create rectangles
     * Throws an exception if string is invalid form
     */
    public void stringToComposition(String xmlString)
            throws SAXException, ParserConfigurationException, IOException {

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(true);
        SAXParser parser = parserFactory.newSAXParser();

        SAXNoteHandler handler = new SAXNoteHandler();
        parser.parse(new InputSource(new StringReader(xmlString)), handler);
        //populate composition panel
        this.compController.populateCompositionPanel(handler.pStack.peek());
        handler.notes.forEach(n -> this.compController.addNoteToComposition(n));
    }

    /**
     * Generates a String of the currently selected notes to
     * adds them to the clipboard
     */
    public void copySelected() {
        String mainString = createXML(this.compController.getSelectedRectangles());
        this.addToClipBoard(mainString);
    }

    /**
     * Creates an xml string of the given Selectable Rectangles
     * @param recs
     * @return
     */
    public static String createXML(Collection<SelectableRectangle> recs) {
        String mainString = "";

        for (SelectableRectangle sr : recs) {
            if (!sr.xProperty().isBound()) {
                mainString += sr.toString(1);
            }
        }
        return "<Composition>\n" + mainString + "</Composition>\n";
    }

    /**
     * adds new strings of data to the clip board
     *
     * @param cutCopyData string data to be added to the clipboard
     */
    public void addToClipBoard(String cutCopyData) {
        Transferable transferable = new StringSelection(cutCopyData);
        board.setContents(transferable, null);
    }

    /**
     * The SAX event handler which handles parsing through
     * our saved note strings
     */
    private class SAXNoteHandler extends DefaultHandler {
        /** Stack to hold pointers to rectangles to assist in creation of groups*/

        private Stack<Collection<SelectableRectangle>> pStack;
        private Collection<Note> notes;

        public SAXNoteHandler(){
            this.pStack = new Stack<>();
            this.notes = new ArrayList<>();
            //populate the stack with an initial list
            this.pStack.push(new ArrayList<>());
        }

        @Override
        //This is triggered when the start of a tag is found
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            switch (qName) {
                case "NoteRectangle":
                    NoteRectangle rec = compController.getClickInPanelHandler()
                                              .addNoteRectangle(
                            Double.parseDouble(attributes.getValue("xpos")),
                            Double.parseDouble(attributes.getValue("ypos")),
                            Double.parseDouble(attributes.getValue("width")),
                            instController.getInstrument(Integer.parseInt(
                                    attributes.getValue("instValue"))
                            )
                    );
                    this.notes.add(compController.getClickInPanelHandler().addBoundNote(rec,
                            rec.getInstrument()));
                    pStack.peek().add(rec);
                    break;
                case "GroupRectangle":
                    pStack.push(new ArrayList<>());
            }
        }

        @Override
        /** Triggered when an end tag is found */
        public void endElement(String uri, String localName,
                               String qName)
                throws SAXException {
            switch(qName) {
                case "NoteRectangle":
                    break;
                case "GroupRectangle":
                    GroupRectangle temp = compController.createGroupRectangle(this.pStack.pop());
                    this.pStack.peek().add(temp);
            }
        }
    }
}

