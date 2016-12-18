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
import javafx.scene.layout.AnchorPane;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;
import java.util.function.BinaryOperator;

/**
 * contorls the property panel
 */
public class PropertyPanelController {

    /**
     * reference to the CompositionPanelController
     */
    private CompositionPanelController compositionPanelController;

    private InstrumentPanelController instController;

    @FXML
    private AnchorPane pane;

    @FXML
    private ComboBox instrumentSelect;
    

    /**
     * Sets up the references to the necessary controllers this minion needs
     * to talk to
     */
    public void init(CompositionPanelController compController,
                     InstrumentPanelController instController)
    {
        this.compositionPanelController = compController;
        this.instController = instController;

        Collection<String> instrumentOptions = this.instController.getInstrumentNames();
        for (String s : instrumentOptions){
            instrumentSelect.getItems().add(s);

        }

        BooleanBinding selectedNotesBinding = Bindings.createBooleanBinding(() ->
                        this.compositionPanelController.getSelectedRectangles().size() > 0,
                this.compositionPanelController.getActionController().getUndoActionsProperty()
        );

        this.pane.visibleProperty().bind(selectedNotesBinding);

    }

    @FXML
    public void handleApply(){
        Collection<SelectableRectangle> selectedList = this.compositionPanelController.getSelectedRectangles();


        System.out.println("ere");
    }




}
