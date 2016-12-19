/**
 * File: EditMenuController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Dec 13, 2016
 */
package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeInstrumentAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeVolumeAction;
import proj9ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj9ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import proj9ZhouRinkerSahChistolini.Models.Note;


/**
 * contorls the property panel
 */
public class PropertyPanelController {

    /* reference to the CompositionPanelController */
    private CompositionPanelController compositionPanelController;

    /* reference to the instrument panel controller */
    private InstrumentPanelController instController;

    @FXML
    /* main Pannel for the property bar */
    private AnchorPane pane;

    @FXML
    /* controls instrument selection box */
    private ComboBox instrumentSelect;

    @FXML
    /* controls pitch selection input */
    private TextField pitchBox;

    @FXML
    /* controls duration selection input */
    private TextField durationBox;

    @FXML
    /* contorls volume input */
    private Slider volumeBar;


    /**
     * Sets up graphic display of the property panel
     */
    public void init(CompositionPanelController compController,
                     InstrumentPanelController instController) {
        this.compositionPanelController = compController;
        this.instController = instController;

        this.setPropertyBarVisibility();

        //set up combo box selections
        Collection<String> instrumentOptions = this.instController.getInstrumentNames();
        for (String s : instrumentOptions) {
            instrumentSelect.getItems().add(s);
        }

    }

    @FXML
    /**
     * called when the apply button is pushed to set instrument properties
     */
    public void handleApply() {

        //set pitch
        if(!this.pitchBox.isDisabled()){
            this.setPitchVal();
        }

        // set duration
        if(!this.durationBox.isDisabled()){
            this.setDuration();
        }

        //set volume
        if (!this.volumeBar.isDisabled()){
            this.setVolume();
        }

        //set instrument
        if (!this.instrumentSelect.isDisabled())
            this.setInstrument();
    }


    /**
     * sets selected notes to desired pitch value
     */
    private void setPitchVal(){
        try {


            int newPitch = Integer.parseInt(this.pitchBox.getCharacters().toString());

            //make sure pitch is in valid range
            if (newPitch>=0 && newPitch<=127) {


                this.compositionPanelController.addAction(new ChangeVolumeAction(this.compositionPanelController.getSelectedNotes(), newPitch));
                this.compositionPanelController.getSelectedRectangles().forEach(n -> {
                    if (n instanceof NoteRectangle) {
                        n.yProperty().setValue(1270 - newPitch * 10);
                    }
                });
            }
            else{
                this.warning("Invalid Number", "Please select an integer between 0-127");
            }

        } catch (NumberFormatException e) {

            this.warning("Invalid Pitch Input", "Please select a valid integer input between 0-127");

        }

    }

    /**
     * sets the selected notes to the new duration
     */
    private void setDuration(){

        try {

            int newDuration = Integer.parseInt(this.durationBox.getCharacters().toString());
            if (newDuration >0) {
                this.compositionPanelController.addAction(new ChangeVolumeAction(this.compositionPanelController.getSelectedNotes(), newDuration));
                this.compositionPanelController.getSelectedRectangles().forEach(n ->{
                    if(n instanceof NoteRectangle && !n.widthProperty().isBound()) {
                        ((NoteRectangle) n).widthProperty().set(newDuration);
                    } else if (n instanceof GroupRectangle && !n.widthProperty().isBound()){
                        ((GroupRectangle)n).setAllSameWidth(newDuration);

                    }
                });
            }
            else {
                this.warning("Invalid Duration Value", "Please select a positive integer for duration");

            }
        } catch (NumberFormatException e) {
            this.warning("Invalid Duration Input", "Please select a valid integer");
        }

    }

    /**
     * sets selected rectangles to a specified volume
     */
    private void setVolume(){

        double volume = (this.volumeBar.getValue() / 100) * 127;
        this.compositionPanelController.addAction(new ChangeVolumeAction(this.compositionPanelController.getSelectedNotes(), (int) volume));
        this.compositionPanelController.getSelectedNotes().forEach(n -> {
            n.setVolume((int) volume);

        });

    }


    /**
     * Set selected Rectangles to a specified instrument
     */
    private void setInstrument() {
        String text = (String) this.instrumentSelect.getValue();

         List<SelectableRectangle> before = new ArrayList<>();
         this.compositionPanelController.getSelectedRectangles().

        forEach(n->before.add(n));
            this.compositionPanelController.addAction(new

        ChangeInstrumentAction(this.compositionPanelController, before, text));
            before.forEach(n->
            {
                 n.setInstrument(
                    this.compositionPanelController.getInstrumentPanelController().getInstruments()
                        .stream()
                        .filter(
                                i -> i.getName().equals(text)
                        ).collect(Collectors.toList()).get(0).getValue()
        );
    });
}

    /**
     * Sets the property bar to visible if there are selected notes
     */
    private void setPropertyBarVisibility(){

        BooleanBinding selectedNotesBinding = Bindings.createBooleanBinding(() ->
                        this.compositionPanelController.getSelectedRectangles().size() > 0,
                this.compositionPanelController.getActionController().getUndoActionsProperty()
        );

        this.pane.visibleProperty().bind(selectedNotesBinding);

    }


    /**
     * update property panel with current selected notes
     */

    public void populatePropertyPanel(){

        List<Note> noteSet = this.compositionPanelController.getComposition().getSelectedNotes();

        this.populatePitch(noteSet);
        this.populateDuration(noteSet);
        this.populateInstrument(noteSet);
        this.populateVolume(noteSet);


    }


    /**
     * Determine if all selected notes have same duration.
     * If not the duration input field is disabled
     * If so the duration input field is set to the current value
     *
     * @param list currently selected notes
     */
    private void populateDuration(List<Note> list){
        if(list.isEmpty()){
            return;
        }
        double commonDuration = list.get(0).getWidth();
        if (this.compositionPanelController.getComposition().getSelectedNotes().stream().allMatch(n ->
                n.getWidth() == commonDuration
        )) {
            this.durationBox.setDisable(false);
            this.durationBox.setText(Integer.toString((int) commonDuration));
        } else {

            this.durationBox.setDisable(true);
            this.durationBox.setText("--");
        }

    }


    /**
     * Determine if all selected notes have same instrument.
     * If not the instrument input field is disabled
     * If so the instrument input field is set to the current value
     *
     * @param noteSet currently selected notes
     */
    private void populateInstrument(List<Note> noteSet) {
        if(noteSet.isEmpty()){
            return;
        }

        String stringInst = noteSet.get(0).getInstruemntName();
        int val = noteSet.get(0).getInstrumentValue();


        if (this.compositionPanelController.getComposition().getSelectedNotes().stream().allMatch(n ->
                n.getInstrumentValue() == val
        )) {
            this.instrumentSelect.setDisable(false);
            this.instrumentSelect.setValue(stringInst);

        } else {

            this.instrumentSelect.setDisable(true);
            this.instrumentSelect.setValue("--");
        }
    }



    /**
     * Determine if all selected notes have same pitch.
     * If not the pitch input field is disabled
     * If so the pitch input field is set to the current value
     *
     * @param list currently selected notes
     */
    private void populatePitch(List<Note> list) {
        if(list.isEmpty()){
            return;
        }

        double commonPitch = list.get(0).getPitch();

        if (this.compositionPanelController.getComposition().getSelectedNotes().stream().allMatch(n ->
                n.getPitch() == commonPitch
        )) {
            this.pitchBox.setDisable(false);
            this.pitchBox.setText(Integer.toString((int) commonPitch));

        } else {

            this.pitchBox.setDisable(true);
            this.pitchBox.setText("--");
        }

    }


    /**
     * Determine if all selected notes have same volume.
     * If not the volume input field is disabled
     * If so the volume input field is set to the current value
     *
     * @param list currently selected notes
     */
    private void populateVolume(List<Note> list){
        if(list.isEmpty()){
            return;
        }

        int commonVolume = list.get(0).getVolume();
        double volumePercent = (commonVolume/127.0)*100.0;

        if (this.compositionPanelController.getComposition().getSelectedNotes().stream().allMatch(n ->
                n.getVolume() == commonVolume
        )) {
            this.volumeBar.setDisable(false);
            this.volumeBar.setValue(volumePercent);

        } else {

            this.volumeBar.setDisable(true);
            this.volumeBar.setValue(0.0);
        }

    }


    /**
     * Pop up an error box
     * @param type the type of error that occurred
     * @param e the message to be displayed in the box
     */
    public void warning(String type, String e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(type);
        alert.setContentText(e);
        alert.show();
    }

}
