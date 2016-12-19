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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeInstrumentAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeVolumeAction;
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
     * called when the apply button is pused to set instrument properties
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




    private void setPitchVal(){
        try {
            // set pitch and duration
            int newPitch = Integer.parseInt(this.pitchBox.getCharacters().toString());
            this.compositionPanelController.addAction(new ChangeVolumeAction(this.compositionPanelController.getSelectedNotes(), newPitch));
            this.compositionPanelController.getSelectedRectangles().forEach(n -> {
                if(n instanceof NoteRectangle) {
                    ((NoteRectangle) n).yProperty().setValue(1270-newPitch*10);
                }
            });

        } catch (NumberFormatException e) {

        }

    }

    private void setDuration(){

        try {
            int newDuration = Integer.parseInt(this.durationBox.getCharacters().toString());
            this.compositionPanelController.addAction(new ChangeVolumeAction(this.compositionPanelController.getSelectedNotes(), newDuration));

            System.out.println(this.compositionPanelController.getSelectedRectangles().size());
            this.compositionPanelController.getSelectedRectangles().forEach(n ->{
                if(n instanceof NoteRectangle) {
                    ((NoteRectangle) n).widthProperty().set(newDuration);
                }
            });
        } catch (NumberFormatException e) {
        }

    }

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

    private void setPropertyBarVisibility(){

        BooleanBinding selectedNotesBinding = Bindings.createBooleanBinding(() ->
                        this.compositionPanelController.getSelectedRectangles().size() > 0,
                this.compositionPanelController.getActionController().getUndoActionsProperty()
        );

        this.pane.visibleProperty().bind(selectedNotesBinding);

    }



    public void populatePropertyPanel(){

        List<Note> noteSet = this.compositionPanelController.getComposition().getSelectedNotes();

        this.populatePitch(noteSet);
        this.populateDuration(noteSet);
        this.populateInstrument(noteSet);
        this.populateVolume(noteSet);


    }


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

}
