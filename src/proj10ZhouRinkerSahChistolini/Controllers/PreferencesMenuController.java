package proj10ZhouRinkerSahChistolini.Controllers;
import proj10ZhouRinkerSahChistolini.Models.Instrument;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by Alex on 12/11/16.
 */
public class PreferencesMenuController {
    /**
     * reference to the CompositionPanelController
     */
    private CompositionPanelController compositionPanelController;

    /** reference to the instrumentPanelController */
    private InstrumentPanelController instrumentPanelController;

    /** a list of color options for the various instruments */
    private Set<String> colorOptions = new HashSet<String>();
    private List<Instrument> instrumentOptions;

    /**
     * Sets up the references to the necessary controllers this minion needs
     * to talk to
     */
    public void init(CompositionPanelController compController,
                     InstrumentPanelController instController) {
        this.compositionPanelController = compController;
        this.instrumentPanelController = instController;
        initializeColorOptions();
        initializeInstrumentOptions(this.instrumentPanelController.getInstruments());
    }

    @FXML
    /**
     * updates the composition preferences based on the user's interaction with the
     * dialog menu
     */
    public void updateCompPreferences() {
        this.compositionPanelController.stopComposition();
        String tempo = String.valueOf(
                this.compositionPanelController.getCompositionTempo()
        );
        String width = getNoteFromWidth(compositionPanelController.getNoteWidth(), 100);
        String volume = String.valueOf(this.compositionPanelController.getNoteVolume());
        Dialog<HashMap<String,String>> dialog = generateCompositionDialog(width,
                                                                          volume,
                                                                          tempo);
        Optional<HashMap<String, String>> result = dialog.showAndWait();

        if(result.isPresent()) { //If the result isn't present, then cancel was pressed
            HashMap<String,String> results = result.get();
            try {
                this.compositionPanelController.updatePreferences(
                        getWidthFromNote(results.get("duration"), 100),
                        Integer.parseInt(results.get("volume")),
                        Integer.parseInt(results.get("tempo"))
                );
            } catch (NumberFormatException e) {
                this.compositionPanelController.updatePreferences(
                        getWidthFromNote(results.get("duration"), 100),
                        this.compositionPanelController.getNoteVolume(),
                        this.compositionPanelController.getCompositionTempo()
                );
            }
        }
    }

    @FXML
    /**
     * updates the instrument panel preferences based on the user's interaction
     * with the dialog menu
     */
    public void updateInstPreferences() {
        this.compositionPanelController.stopComposition();
        Dialog<InstrumentDataHolder> dialog = generateInstrumentDialog(
                this.instrumentPanelController.getInstruments()
        );
        Optional<InstrumentDataHolder> result = dialog.showAndWait();
        if(result.isPresent()) { //If the result isn't present, then cancel was pressed
            this.instrumentPanelController.updateInstruments(
                    result.get().getInstruments(),
                    result.get().getColors()
            );
        }
    }

    private Dialog<HashMap<String, String>> generateCompositionDialog(String currentNoteLength,
                                                      String currentNoteVolume,
                                                      String currentTempo) {
        // Create the custom dialog.
        Dialog<HashMap<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Composition Preferences");

        // Set the button types and add them to the pane
        ButtonType applyButton = new ButtonType("Apply");
        dialog.getDialogPane().getButtonTypes().addAll(applyButton, ButtonType.CANCEL);

        // Create the grid to place the questions on
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        //Set up the preferences questions
        TextField tempoSelect = new TextField();
        tempoSelect.setText(currentTempo);
        tempoSelect.setPromptText("Composition Tempo");
        TextField noteVolume = new TextField();
        noteVolume.setText(currentNoteVolume);
        noteVolume.setPromptText("Default Note Volume");
        ChoiceBox noteLength = new ChoiceBox<Integer>();
        noteLength.getItems().addAll("Whole", "Half", "Quarter", "Eighth");
        noteLength.setValue(currentNoteLength);

        //Add items to the grid
        grid.add(new Label("Composition Tempo (bpm):"), 0, 0);
        grid.add(tempoSelect, 1, 0);
        grid.add(new Label("Default Note Volume (0-127):"), 0, 1);
        grid.add(noteVolume, 1, 1);
        grid.add(new Label("Default Note Length:"), 0, 2);
        grid.add(noteLength, 1, 2);
        grid.add(new Text("Note: \n" +
                          "Illegal input will be set to the nearest legal value"), 0, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a hashmap of results
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButton) {
                HashMap<String, String> preferences = new HashMap<>();
                preferences.put("tempo", tempoSelect.getText());
                preferences.put("volume", noteVolume.getText());
                preferences.put("duration", (String) noteLength.getValue());
                return preferences;
            }
            return null;
        });

        return dialog;
    }


    private Dialog<InstrumentDataHolder> generateInstrumentDialog(
            List<Instrument> instruments
    ) {
        // Create the custom dialog.
        Dialog<InstrumentDataHolder> dialog = new Dialog<>();
        dialog.setTitle("Instrument Preferences");

        // Set the button types and add them to the pane
        ButtonType applyButton = new ButtonType("Apply");
        dialog.getDialogPane().getButtonTypes().addAll(applyButton, ButtonType.CANCEL);

        // Create the grid to place the questions on
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        ChoiceBox instBox;
        ChoiceBox instColorBox;
        ArrayList<ChoiceBox> instBoxes = new ArrayList<>();
        ArrayList<ChoiceBox> colorBoxes = new ArrayList<>();

        //Set up the various choices
        for(int i=0; i<instruments.size(); i++) {
            //Instrument Choices
            instBox = new ChoiceBox();
            instBox.getItems().addAll(this.instrumentOptions);
            instBox.setValue(instruments.get(i));
            instBoxes.add(instBox);

            //Color choices
            instColorBox = new ChoiceBox();
            instColorBox.getItems().addAll(this.colorOptions);
            instColorBox.getItems().add("Default Color");
            instColorBox.setValue("Default Color");
            colorBoxes.add(instColorBox);

            //Add items to the grid
            grid.add(instBox, 0, i);
            grid.add(instColorBox, 1, i);
        }
        dialog.getDialogPane().setContent(grid);

        // Convert the result to an array of Instruments
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButton) {
                ArrayList<Instrument> instPreferences = new ArrayList<>();
                ArrayList<String> colorPreferences = new ArrayList<>();

                for(ChoiceBox c : instBoxes) {
                    instPreferences.add((Instrument) c.getValue());
                }

                for(ChoiceBox c : colorBoxes) {
                    String color = (String) c.getValue();
                    if(color.equals("Default Color")) { colorPreferences.add("");
                    } else {
                        colorPreferences.add((String) c.getValue());
                    }
                }

                return new InstrumentDataHolder(instPreferences, colorPreferences);
            }
            return null;
        });

        return dialog;
    }

    // The following methods are dialog-specific conversions used in the various
    // Dialogs created above.

    /**
     * returns a string representation of the type of note based on
     * the width to ticksPerBeat ratio
     * @param width
     * @param ticksPerBeat
     * @return String representation of the note (quarter, half, etc)
     */
    public String getNoteFromWidth(int width, int ticksPerBeat) {
        switch(ticksPerBeat/width) {
            case 1:
                return "Whole";
            case 2:
                return "Half";
            case 4:
                return "Quarter";
            case 8:
                return "Eighth";
        }
        return null;
    }

    /**
     * returns the integer width of the note based on the
     * String name and the ticks per beat
     * @param width
     * @param ticksPerBeat
     * @return
     */
    public int getWidthFromNote(String width, int ticksPerBeat) {
        switch(width) {
            case "Whole":
                return ticksPerBeat;
            case "Half":
                return ticksPerBeat/2;
            case "Quarter":
                return ticksPerBeat/4;
            case "Eighth":
                return ticksPerBeat/8;
        }
        return 0;
    }

    /**
     * seeds the instrument color options field
     */
    private void initializeColorOptions() {
        Collections.addAll(this.colorOptions,
                "green", "blue", "gold", "magenta",
                "orange", "black", "pink", "grey",
                "cyan", "red", "lime", "fuchsia",
                "khaki", "lightblue", "plum",
                "crimson"
        );
    }

    /**
     * seeds the Instrument class options field
     */
    private void initializeInstrumentOptions(List<Instrument> instruments) {
        this.instrumentOptions = new ArrayList<>(instruments);
        this.instrumentOptions.add(new Instrument("Xylophone", 13, 8, 8));
        this.instrumentOptions.add(new Instrument("Harmonica", 22, 9, 9));
        this.instrumentOptions.add(new Instrument("Recorder", 74, 10, 10));
        this.instrumentOptions.add(new Instrument("Alto Sax", 65, 11, 11));
        this.instrumentOptions.add(new Instrument("Synth Bass", 38, 12, 12));
        this.instrumentOptions.add(new Instrument("Flute", 73, 13, 13));
        this.instrumentOptions.add(new Instrument("Electric Piano", 4, 14, 14));
        this.instrumentOptions.add(new Instrument("Pad 1", 88, 14, 14));
        this.instrumentOptions.add(new Instrument("Pad 4", 91, 14, 14));
    }

    /**
     * a class which holds data associated with instrument panel preferences
     */
    private class InstrumentDataHolder {
        private List<Instrument> instruments;
        private List<String> colorChoices;

        public InstrumentDataHolder(List<Instrument> instruments,
                                    List<String> colors
        ) {
            this.instruments = instruments;
            this.colorChoices = colors;
        }

        /**
         * returns the list of instruments
         * @return arraylist of instruments
         */
        public List<Instrument> getInstruments() {
            return this.instruments;
        }

        /**
         * returns the colors
         * @return the list of colors
         */
        public List<String> getColors() {
            return this.colorChoices;
        }
    }
}
