package proj9ZhouRinkerSahChistolini.Controllers;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.Integer.max;

/**
 * Allows easy importing and exporting to different file formats of the composition
 */
public class FileConverter {
    public static final int NOTE_ON = ShortMessage.NOTE_ON;
    public static final int NOTE_OFF = ShortMessage.NOTE_OFF;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private Sequence sequence;

    private CompositionPanelController compositionPanelController;
    private XMLHandler xmlHandler;
    public FileConverter(CompositionPanelController compositionPanelController,
                         XMLHandler xmlHandler){
        this.compositionPanelController = compositionPanelController;
        this.xmlHandler = xmlHandler;
    }

    public void importMidi(File file) throws IOException, InvalidMidiDataException,
            ParserConfigurationException, org.xml.sax.SAXException {
        this.sequence = MidiSystem.getSequence(file);
        String xml = "<Composition>\n" + this.midiToString() + "</Composition>\n";
        this.xmlHandler.loadNotesFromXML(xml);
    }

    /**
     * Create note string based on an on and off midievent
     * @param on
     * @param off
     * @return
     */
    private String createNoteString(MidiEvent on, MidiEvent off){
        int bpmMod = sequence.getResolution()/(30);
        if (true){
            return "    <Note " +
                    "xpos=\"" + (int)on.getTick()/bpmMod  +"\" "+
                    "ypos=\"" + ((127-(((ShortMessage) on.getMessage()).getData1()))*10) +"\" "+
                    "width=\"" + ((off.getTick() - (on.getTick()))/bpmMod) + "\" " +
                    "instValue=\"" + ((ShortMessage) on.getMessage()).getData1()  +"\" " +
                    "volume=\"" + ((ShortMessage) on.getMessage()).getData2()  +"\" " +
                    "/>\n";
    }
    else{return "";}
    }

    /**
     * find an associated NOTE_ON message
     * @param onList
     * @param off
     * @return
     */
    private MidiEvent getAssociatedOnMessage(ArrayList<MidiEvent> onList, ShortMessage off){
        int index = 0;
        for(int i = 0; i< onList.size(); i++){
            if(((ShortMessage)onList.get(i).getMessage()).getData1() == off.getData1() &&
                    ((ShortMessage)onList.get(i).getMessage()).getChannel() == off.getChannel()){
                index = i;
                break;
            }
        }
        if (onList.size() ==0)
            return null;
        return onList.remove(index);
    }

    /**
     * Build a composition compatible xml string from midi
     * @return builtXML
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public String midiToString() {
        String builtXML = "";
        int maxTick = 0;
        ArrayList<ArrayList<MidiEvent>> onEvents = new ArrayList();

        for (Track track :  sequence.getTracks()) {
            ArrayList<MidiEvent> on = new ArrayList();
            onEvents.add(on); //add new track to the list

            for (int i=0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    maxTick = max(((int)event.getTick()), maxTick);
                    // Add a new instrument to the panel
                    if (!compositionPanelController
                            .getInstrumentPanelController().contains(sm.getData1())){
                        compositionPanelController.getInstrumentPanelController().addInstrument(
                                Integer.toString(sm.getData1()),
                                sm.getData1(),
                                sm.getChannel()
                        );
                    }

                    //parse on and off events
                    if (sm.getCommand() == NOTE_ON) {
                        if (sm.getData2() != 0){
                            on.add(event);
                        }
                        else { //some midifiles just set the note_on with volume 0 as off
                            try{builtXML += createNoteString(getAssociatedOnMessage(on,sm), event);}
                            catch (Exception e){continue;}
                        }
                    } else if (sm.getCommand() == NOTE_OFF) {
                        try{builtXML += createNoteString(getAssociatedOnMessage(on,sm), event);}
                        catch (Exception e){continue;}

                    }
                }
            }
        }
        compositionPanelController.getCompositionPane().setMaxWidth(maxTick);
        return builtXML;

    }
}


