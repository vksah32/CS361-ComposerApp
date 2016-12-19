package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import proj9ZhouRinkerSahChistolini.Models.Instrument;

import java.util.*;

/**
 *  This class holds all the info for instruments
 *  @author Ed Zhou
 *  @author Alex Rinker
 *  @author Victoria Chistolini
 *  @author Vivek Sah
 */
public class InstrumentPanelController {

    /**Pane to hold the radio buttons*/
    @FXML
    private VBox instrumentBox;
    /**List of instruments*/
    private List<Instrument> instruments;
    /** List of style classes associated with the instruments */
    private HashMap<String,String> styleMappings;
    /**Toggle group to handle selection*/
    @FXML
    private ToggleGroup instrumentToggle;

    /**
     * Initiates instruments and radio buttons to the pane
     */
    @FXML
    public void initialize() {
        this.instruments = new ArrayList<Instrument>();
        initializeStyleMappings();
        addInstrument("Harpsichord", 6, 0, this.styleMappings.get("Harpsichord"));
        addInstrument("Marimba", 12, 1, this.styleMappings.get("Marimba"));
        addInstrument("Organ", 19, 2, this.styleMappings.get("Organ"));
        addInstrument("Accordion", 21, 3, this.styleMappings.get("Accordion"));
        addInstrument("Guitar", 28, 4, this.styleMappings.get("Guitar"));
        addInstrument("Violin", 40, 5, this.styleMappings.get("Violin"));
        addInstrument("French Horn", 60, 6, this.styleMappings.get("French Horn"));
        addInstrument("Piano", 0, 7, this.styleMappings.get("Piano"));
        this.instrumentToggle.getToggles().get(0).setSelected(true);
    }

    /**
     * sets up the initial styles
     */
    private void initializeStyleMappings() {
        this.styleMappings = new HashMap<>();
        this.styleMappings.put("Harpsichord","green-inst");
        this.styleMappings.put("Mariba","blue-inst");
        this.styleMappings.put("Organ","gold-inst");
        this.styleMappings.put("Accordion","magenta-inst");
        this.styleMappings.put("Guitar","orange-inst");
        this.styleMappings.put("Violin","black-inst");
        this.styleMappings.put("French Horn","pink-inst");
        this.styleMappings.put("Piano","grey-inst");
    }

    public List<Instrument> getInstruments(){
        return this.instruments;
    }

    /** accessor method for the styleMappings */
    public HashMap<String, String> getStyleMappings() {return this.styleMappings;}

    /**
     * Adds a new instrument to the pane
     * @param name The title of the instrument
     * @param instrument The 0-127 value of the instrument
     * @param channel The channel in which the instrument is played
     */
    public void addInstrument(String name,
                              int instrument,
                              int channel,
                              String styleClass
    ){
        this.instruments.add(new Instrument(name, instrument, channel));

        RadioButton instrButton = new RadioButton(name);
        instrButton.getStyleClass().add("instrument-button");
        instrButton.getStyleClass().add(styleClass);
        instrButton.setToggleGroup(this.instrumentToggle);
        this.instrumentBox.getChildren().add(instrButton);
    }

    /**
     * Returns the currently selected instrument's id
     * @return instrument id
     */
    public int getSelectedInstrument(){
        RadioButton chk = (RadioButton) this.instrumentToggle.getSelectedToggle();
        String name = chk.getText();
        Instrument selected = this.instruments.get(0);
        for (Instrument instrument: this.instruments){
            if (instrument.getName().equals(name)){
                selected = instrument;
                break;
            }
        }
        return selected.getValue();
    }

    /**
     * Returns the instrument with the associated value if
     * it has been initialized
     */
    public Instrument getInstrument(int value) {
        for (Instrument i : this.instruments) {
            if (i.getValue() == value) {
                return i;
            }
        }
        return null;
    }

    /**
     * Returns true if there is an instrument in the panel that has the
     * specified pitch
     * @param pitch to check against the panel instruments
     * @return
     */
    public boolean contains(int pitch){
        for (Instrument instrument : this.instruments) {
            if (instrument.getValue() == pitch) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the InstrumentPanel's instruments to the input
     * values
     * @param instruments an arraylist of instruments
     */
    public void updateInstruments(List<Instrument> instruments,
                                  List<String> styleClasses) {
        ArrayList<Instrument> oldInstruments = new ArrayList(this.instruments);
        HashMap<String, String> oldStyles = new HashMap<>(styleMappings);
        this.instruments.clear();
        this.styleMappings.clear();
        this.instrumentToggle.getToggles().clear();
        this.instrumentBox.getChildren().clear();

        Instrument inst;
        String styleClass;
        for(int i=0; i<instruments.size(); i++) {
            inst = instruments.get(i);
            styleClass = styleClasses.get(i) + "-inst";
            if(!styleClass.equals("-inst")) {
                addInstrument(
                        inst.getName(),
                        inst.getValue(),
                        i,
                        styleClass
                );
                this.styleMappings.put(inst.getName(), styleClass);
            } else {
                addInstrument(
                        inst.getName(),
                        inst.getValue(),
                        i,
                        oldStyles.get(oldInstruments.get(i).getName())
                );
                this.styleMappings.put(
                        inst.getName(),
                        oldStyles.get(oldInstruments.get(i).getName())
                );
            }
        }
        this.instrumentToggle.getToggles().get(0).setSelected(true);
    }
}
