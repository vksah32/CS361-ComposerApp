package proj10ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import proj10ZhouRinkerSahChistolini.Models.Instrument;

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
    private List<String> styleList;
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
        addInstrument("Harpsichord", 6, 0, this.styleList.get(0), 0);
        addInstrument("Marimba", 12, 1, this.styleList.get(1), 1);
        addInstrument("Organ", 19, 2, this.styleList.get(2), 2);
        addInstrument("Accordion", 21, 3, this.styleList.get(3), 3);
        addInstrument("Guitar", 28, 4, this.styleList.get(4), 4);
        addInstrument("Violin", 40, 5, this.styleList.get(5), 5);
        addInstrument("French Horn", 60, 6, this.styleList.get(6), 6);
        addInstrument("Piano", 0, 7, this.styleList.get(7), 7);
        this.instrumentToggle.getToggles().get(0).setSelected(true);
    }

    /**
     * sets up the initial styles
     */
    private void initializeStyleMappings() {
        this.styleList = new ArrayList<>();
        this.styleList.add("green-inst");
        this.styleList.add("blue-inst");
        this.styleList.add("gold-inst");
        this.styleList.add("magenta-inst");
        this.styleList.add("orange-inst");
        this.styleList.add("black-inst");
        this.styleList.add("pink-inst");
        this.styleList.add("grey-inst");
    }

    public List<Instrument> getInstruments(){
        return this.instruments;
    }

    /** accessor method for the styleMappings */
    public List<String> getStyleMappings() {return this.styleList;}

    /**
     * Adds a new instrument to the pane
     * @param name The title of the instrument
     * @param instrument The 0-127 value of the instrument
     * @param channel The channel in which the instrument is played
     */
    public void addInstrument(String name,
                              int instrument,
                              int channel,
                              String styleClass,
                              int id
    ){
        this.instruments.add(new Instrument(name, instrument, channel, id));
        RadioButton instrButton = new RadioButton(name);
        instrButton.getStyleClass().add("instrument-button");
        instrButton.getStyleClass().add(styleClass);
        instrButton.setToggleGroup(this.instrumentToggle);
        this.instrumentBox.getChildren().add(instrButton);
    }

    public void addInstrument(Instrument inst, String styleClass) {
        this.instruments.add(inst);
        RadioButton instrButton = new RadioButton(inst.getName());
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
     * gets names of current instruments
     *
     * @return instrument list
     */
    public Collection<String> getInstrumentNames() {

        HashSet<String> nameList = new HashSet<>();
        for (Instrument inst : this.instruments) {
            nameList.add(inst.getName());
        }
        return nameList;
    }

    /**
     * gets the intsrument integer value for a fiven instrument
     * @param instrumentName string name of instrument
     * @return int value of instrument, default instruent is piano if none found
     */
    public int getInstrumentValues(String instrumentName){
        for (Instrument i : this.instruments){
            if( i.getName().equals(instrumentName)){
                return i.getValue();
            }
        }
        return 0;
    }

    /**
     * Sets the InstrumentPanel's instruments to the input
     * values
     * @param instruments an arraylist of instruments
     */
    public void updateInstruments(List<Instrument> instruments,
                                  List<String> styleClasses) {
        ArrayList<String> oldStyles = new ArrayList<>(styleList);
        this.instruments.clear();
        this.styleList.clear();
        this.instrumentToggle.getToggles().clear();
        this.instrumentBox.getChildren().clear();

        Instrument inst;
        String styleClass;
        for(int i=0; i<instruments.size(); i++) {
            inst = instruments.get(i);
            styleClass = styleClasses.get(i) + "-inst";
            if(!styleClass.equals("-inst")) {
                addInstrument(inst, styleClass);
                this.styleList.add(styleClass);
            } else {
                addInstrument(inst, oldStyles.get(i));
                this.styleList.add(oldStyles.get(i));
            }
            inst.setId(i);
        }
        this.instrumentToggle.getToggles().get(0).setSelected(true);
    }
}
