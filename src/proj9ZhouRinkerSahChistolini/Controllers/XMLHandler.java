package proj9ZhouRinkerSahChistolini.Controllers;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import proj9ZhouRinkerSahChistolini.Models.Gesture;
import proj9ZhouRinkerSahChistolini.Models.Note;
import proj9ZhouRinkerSahChistolini.Models.Playable;
import proj9ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj9ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * Created by Alex on 11/17/16.
 */
public class XMLHandler {

    /** a reference to the composition controller*/
    private CompositionPanelController compController;

    /**
     * Constructor method for this class
     * @param compController a reference to the composition panel controller
     */
    public XMLHandler(CompositionPanelController compController) {
        this.compController = compController;
    }

    /**
     * returns a list representing the composition of the input string
     * @returns composition a list representation of the composition string
     */
    public void loadNotesFromXML(String xmlString)
            throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(true);
        SAXParser parser = parserFactory.newSAXParser();

        SAXNoteHandler handler = new SAXNoteHandler();
        parser.parse(new InputSource(new StringReader(xmlString)), handler);
        addToComposition(handler);
    }

    /**
     * adds notes to the composition based on the input
     * Handler's data
     */
    private void addToComposition(SAXNoteHandler handler) {
        //populate composition panel
        if (compController.getWidth() < handler.maxWidth){
            compController.setWidth(handler.maxWidth);
            compController.drawLines();
        }
        this.compController.populateCompositionPanel(handler.pStack.peek());
        handler.notesStack.peek().forEach(n -> this.compController.addNoteToComposition(n));
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
     * The SAX event handler which handles parsing through
     * our saved note strings
     */
    private class SAXNoteHandler extends DefaultHandler {
        /** Stack to hold pointers to rectangles to assist in creation of groups*/

        private Stack<Collection<SelectableRectangle>> pStack;
        private Stack<Collection<Playable>> notesStack;
        /** Maximum size of the composition*/
        private int maxWidth;

        /**
         * initializer for SAXNoteHandler
         */
        public SAXNoteHandler(){
            this.pStack = new Stack<>();
            this.notesStack = new Stack<>();
            //populate the stack with an initial list
            this.pStack.push(new ArrayList<>());
            this.notesStack.push(new ArrayList<>());
            this.maxWidth = 0;
        }

        @Override
        //This is triggered when the start of a tag is found
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            switch (qName) {
                case "Note":
                    int width = (int)Double.parseDouble(attributes.getValue("width"));
                    NoteRectangle rec = compController.getClickInPanelHandler()
                            .addNoteRectangle(
                                    Double.parseDouble(attributes.getValue("xpos")),
                                    Double.parseDouble(attributes.getValue("ypos")),
                                    Integer.parseInt(attributes.getValue("instValue")),
                                    width
                            );
                    Note note = compController.getClickInPanelHandler().createBoundNote(
                            rec, rec.getInstrument(), max(0, min(127,Integer.parseInt(attributes.getValue("volume")))));
                    this.notesStack.peek().add(note);

                    this.pStack.peek().add(rec);
                    this.maxWidth = max(this.maxWidth,(int) note.getRightX());
                    break;
                case "Gesture":
                    pStack.push(new ArrayList<>());
                    notesStack.push(new ArrayList<>());
            }
        }

        @Override
        /** Triggered when an end tag is found */
        public void endElement(String uri, String localName,
                               String qName)
                throws SAXException {
            switch(qName) {
                case "Note":
                    break;
                case "Gesture":
                    if(!this.pStack.peek().isEmpty()) {
                        GroupRectangle temp = compController.createGroupRectangle(this.pStack.pop());
                        Gesture modelTemp = compController.createGesture(temp, notesStack.pop());
                        this.notesStack.peek().add(modelTemp);
                        this.pStack.peek().add(temp);
                    }
            }
        }
    }
}
