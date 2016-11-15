/**
 * File: Controller.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Nov 14, 2016
 */
package proj7ZhouRinkerSahChistolini.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

/**
 * This class handles all of the MenuItems associated
 * with the File Menu
 */
public class FileMenuController {

    /**
     * Ensures that all processes are killed on the
     * destruction of the window.
     */
    @FXML
    public void cleanUpOnExit() {
        Platform.exit();
        System.exit(0);
    }

}
