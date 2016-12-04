package proj8ZhouRinkerSahChistolini.Controllers;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import proj8ZhouRinkerSahChistolini.Models.Gesture;
import proj8ZhouRinkerSahChistolini.Models.Playable;
import proj8ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj8ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Stack;

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
     * returns whether or not the two input strings will build the
     * same composition
     * @param comp1 the XML String representation of composition 1
     * @param comp2 the XML String representation of composition 2
     * @return boolean representing the equality of the parameters
     */
    public boolean areEqualCompositions(String comp1, String comp2) {
        List<String> gestures1 = Arrays.asList(comp1.split("<Gesture>|</Gesture>"));
        List<String> gestures2 = Arrays.asList(comp2.split("<Gesture>|</Gesture>"));

        //If the compositions don't have the same number of gestures, they are different
        if(gestures1.size() != gestures2.size()) { return false; }

        //Loop through each gesture, and compare the notes in them
        for(int i=0; i<gestures1.size(); i++) {
            //Determine if the gesture is empty
            if(gestures1.get(i).isEmpty() && gestures2.get(i).isEmpty()) {
                continue;
            } else if (gestures1.get(i).isEmpty() || gestures2.get(i).isEmpty()) {
                return false;
            }

            //If neither gesture is empty, determine if they have the same notes
            if (!areEqualGestures(gestures1.get(i), gestures2.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns whether or not the two input strings will build the
     * same gesture
     * @param gesture1 the XML String representation of gesture 1
     * @param gesture2 the XML String representation of gesture 2
     * @return boolean representing the equality of the parameters
     */
    public boolean areEqualGestures(String gesture1, String gesture2) {
        List<String> gest1 = Arrays.asList(gesture1.split("\n"));
        List<String> gest2 = Arrays.asList(gesture2.split("\n"));
        gest1.forEach(g -> g.trim());
        gest2.forEach(g -> g.trim());

        for(String note : gest1) {
            if(!gest2.contains(note)) { return false; }
        }
        for(String note : gest2) {
            if(!gest1.contains(note)) { return false; }
        }
        return true;
    }

    /**
     * adds notes to the composition based on the input
     * Handler's data
     */
    private void addToComposition(SAXNoteHandler handler) {
        //populate composition panel
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

        public SAXNoteHandler(){
            this.pStack = new Stack<>();
            this.notesStack = new Stack<>();
            //populate the stack with an initial list
            this.pStack.push(new ArrayList<>());
            this.notesStack.push(new ArrayList<>());
        }

        @Override
        //This is triggered when the start of a tag is found
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            switch (qName) {
                case "Note":
                    NoteRectangle rec = compController.getClickInPanelHandler()
                            .addNoteRectangle(
                                    Double.parseDouble(attributes.getValue("xpos")),
                                    Double.parseDouble(attributes.getValue("ypos")),
                                    Integer.parseInt(attributes.getValue("instValue"))
                            );
                    this.notesStack.peek().add(compController.getClickInPanelHandler().addBoundNote(
                            rec, rec.getInstrument())
                    );
                    this.pStack.peek().add(rec);
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
                    GroupRectangle temp = compController.createGroupRectangle(this.pStack.pop());
                    Gesture modelTemp = compController.createGesture(temp, notesStack.pop());
                    this.notesStack.peek().add(modelTemp);
                    this.pStack.peek().add(temp);
            }
        }
    }
}
