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

package proj8ZhouRinkerSahChistolini.Controllers;
import proj8ZhouRinkerSahChistolini.Models.Playable;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
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
    private XMLHandler XMLHandler;

    /**
     * Generates a new ClipBoardController initializing the references
     * needed to perform clipboard actions
     *
     * @param compController the associated CompositionPanelController
     * @param xmlHandler the associated xmlHandler
     */
    public ClipBoardController(CompositionPanelController compController,
                               XMLHandler xmlHandler) {
        this.compController = compController;
        this.XMLHandler = xmlHandler;
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
        } catch (UnsupportedFlavorException | IOException e) {
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
            this.XMLHandler.loadNotesFromXML(stringNotes);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            return;
        }
        for (SelectableRectangle rec : this.compController.getRectangles()) {
            if (!temp.contains(rec)) {
                rec.setSelected(true);
            }
        }
    }

    /**
     * Generates a String of the currently selected notes to
     * adds them to the clipboard
     */
    public void copySelected() {
        String mainString = createXML(this.compController.getSelectedNotes());
        this.addToClipBoard(mainString);
    }

    /**
     * Creates an xml string of the given Selectable Rectangles
     * @param recs
     * @return
     */
    public static String createXML(Collection<Playable> recs) {
        String mainString = "";

        for (Playable sr : recs) {
            mainString += sr.toXML(1);

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
}

