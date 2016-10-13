package proj5ZhouRinkerSahChistolini;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.control.ToggleGroup;
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
    private ToggleGroup toggleGroup;

    /**
     * Initiates instruments and radio buttons to the pane
     * @param instrumentPane Pane to hold the radio buttons
     */
    public void initialize() {
        this.toggleGroup = new ToggleGroup();
        this.instruments = new ArrayList<Instrument>();
        addInstrument("Harpsichord", 6, 0, "green");
        addInstrument("Marimba", 12, 1, "blue");
        addInstrument("Organ", 19, 2, "gold");
        addInstrument("Accordion", 21, 3, "magenta");
        addInstrument("Guitar", 28, 4, "teal");
        addInstrument("Violin", 40, 5, "black");
        addInstrument("French Horn", 60, 6, "brown");
        this.toggleGroup.getToggles().get(0).setSelected(true);
    }

    /**
     * Adds a new instrument to the pane
     * @param name The title of the instrument
     * @param instrument The 0-127 value of the instrument
     * @param channel The channel in which the instrument is played
     * @param color The color associated with the instrument
     */
    public void addInstrument(String name, int instrument, int channel, String color){
        this.instruments.add(new Instrument(name, instrument, channel, color));

        RadioButton instrButton = new RadioButton(name);
        instrButton.getStyleClass().add("instrument");
        instrButton.setStyle(String.format("-fx-text-fill: %s", color));
        instrButton.setToggleGroup(this.toggleGroup);
        this.instrumentBox.getChildren().add(instrButton);
    }

    /**
     * Returns the currently selected instrument
     * @return instrument object
     */
    public Instrument getSelectedInstrument(){
        RadioButton chk = (RadioButton) this.toggleGroup.getSelectedToggle();
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
