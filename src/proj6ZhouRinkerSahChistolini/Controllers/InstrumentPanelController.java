package proj6ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
import proj6ZhouRinkerSahChistolini.Models.Instrument;

import java.util.ArrayList;

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
    private ArrayList<Instrument> instruments;
    /**Toggle group to handle selection*/
    @FXML
    private ToggleGroup instrumentToggle;

    /**
     * Initiates instruments and radio buttons to the pane
     */
    @FXML
    public void initialize() {
        this.instruments = new ArrayList<Instrument>();
        addInstrument("Harpsichord", 6, 0);
        addInstrument("Marimba", 12, 1);
        addInstrument("Organ", 19, 2);
        addInstrument("Accordion", 21, 3);
        addInstrument("Guitar", 28, 4);
        addInstrument("Violin", 40, 5);
        addInstrument("French Horn", 60, 6);
        addInstrument("Piano", 0, 7);
        this.instrumentToggle.getToggles().get(0).setSelected(true);
    }

    /**
     * Adds a new instrument to the pane
     * @param name The title of the instrument
     * @param instrument The 0-127 value of the instrument
     * @param channel The channel in which the instrument is played
     */
    public void addInstrument(String name, int instrument, int channel){
        this.instruments.add(new Instrument(name, instrument, channel));

        RadioButton instrButton = new RadioButton(name);
        instrButton.getStyleClass().add("instrument-button");
        instrButton.getStyleClass().add(name.toLowerCase().replace(" ", "-"));
        instrButton.setToggleGroup(this.instrumentToggle);
        this.instrumentBox.getChildren().add(instrButton);
    }

    /**
     * Returns the currently selected instrument
     * @return instrument object
     */
    public Instrument getSelectedInstrument(){
        RadioButton chk = (RadioButton) this.instrumentToggle.getSelectedToggle();
        String name = chk.getText();
        Instrument selected = this.instruments.get(0);
        for (Instrument instrument: this.instruments){
            if (instrument.getName().equals(name)){
                selected = instrument;
                break;
            }
        }
        return selected;
    }
}
