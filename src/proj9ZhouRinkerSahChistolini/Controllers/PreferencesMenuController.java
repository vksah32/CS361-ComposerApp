package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Alex on 12/11/16.
 */
public class PreferencesMenuController {
    /**
     * reference to the CompositionPanelController
     */
    private CompositionPanelController compositionPanelController;

    /**
     * Sets up the references to the necessary controllers this minion needs
     * to talk to
     */
    public void init(CompositionPanelController compController) {
        this.compositionPanelController = compController;
    }

    @FXML
    /**
     *
     */
    public void displayCompPreferences() {
        String tempo = String.valueOf(
                this.compositionPanelController.getCompositionTempo()
        );
        String width = getNoteFromWidth(compositionPanelController.getNoteWidth(), 100);
        String volume = String.valueOf(this.compositionPanelController.getNoteVolume());
        Dialog<HashMap<String,String>> dialog = generateCompositionDialog(width,
                                                                          volume,
                                                                          tempo);
        Optional<HashMap<String, String>> result = dialog.showAndWait();

        HashMap<String,String> results = result.get();

        if(results != null) {
            this.compositionPanelController.updatePreferences(
                    getWidthFromNote(results.get("duration"), 100),
                    Integer.parseInt(results.get("volume")),
                    Integer.parseInt(results.get("tempo"))
            );
        }
    }

    @FXML
    /**
     *
     */
    public void displayInstPreferences() {
        return;
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
        grid.setPadding(new Insets(20, 150, 10, 10));

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
        grid.add(new Label("Composition Tempo:"), 0, 0);
        grid.add(tempoSelect, 1, 0);
        grid.add(new Label("Default Note Volume:"), 0, 1);
        grid.add(noteVolume, 1, 1);
        grid.add(new Label("Default Note Length:"), 0, 2);
        grid.add(noteLength, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
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
}
