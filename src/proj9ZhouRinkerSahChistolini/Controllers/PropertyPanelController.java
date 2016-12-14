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

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.util.Collection;

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
    private AnchorPane anchorPane;

    @FXML
    private ComboBox instrumentSelect;



    /**
     * Sets up the references to the necessary controllers this minion needs
     * to talk to
     */
    public void init(CompositionPanelController compController, InstrumentPanelController instController) {
        this.compositionPanelController = compController;
        this.instController = instController;

        //Collection<String> instrumentOptions = this.instController.getInstrumentNames();
        //for (String s : instrumentOptions){
          //  box.getItems().add(s);

//        }
    }



}
