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
package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class handles all of the MenuItems associated
 * with the File Menu
 */
public class FileMenuController {

    /** The Currently opened file */
    private File currentOpenFile;
    /** Chooser to pick the files */
    private FileChooser chooser;
    /** The application's compositionController */
    private CompositionPanelController compositionPanelController;
    /** The application's XMLHandler */
    private XMLHandler XMLHandler;

    public void initialize(){
        this.currentOpenFile =null;
        chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "xml files(*.xml)", "*.xml"
        );
        chooser.getExtensionFilters().add(extFilter);
    }

    public void init(CompositionPanelController compositionPanelController,
                     XMLHandler xmlHandler){
        this.compositionPanelController = compositionPanelController;
        this.XMLHandler = xmlHandler;
    }

    /**
     * Ensures that all processes are killed on the
     * destruction of the window.
     */
    @FXML
    public void cleanUpOnExit() {
        this.compositionPanelController.stopComposition();
        checkUnsavedChanges();
        Platform.exit();
        System.exit(0);
    }


    /** Dialog window that gives information about the application */
    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("This is a musical composition development " +
                "software \n Authors: \n Vivek Sah, " +
                "Victoria Chistolini, Alex Rinker and Ed Zhou");
        alert.show();
    }

    /**
     * Create new Composition based on whether or not the composition has changed
     */
    @FXML
    public void createNewDocument() {
        this.compositionPanelController.stopComposition();
        // check if modified
        checkUnsavedChanges();
        //clear the document
        this.compositionPanelController.reset();
        this.currentOpenFile = null;
    }

    /** Open a new composition file */
    @FXML
    public void open() {
        this.compositionPanelController.stopComposition();
        checkUnsavedChanges();
        this.currentOpenFile = this.chooser.showOpenDialog(new Stage());
        if(this.currentOpenFile == null) {//If the user cancels
            return;
        }
        try {
            String lines = readFile(this.currentOpenFile);
            this.compositionPanelController.reset();
            try {
                this.XMLHandler.loadNotesFromXML(lines);
            } catch (SAXException e) {
                this.errorAlert("Error Parsing File", "Malformed XML File");
            } catch (ParserConfigurationException e) {
                return;
            }
        }
        catch(IOException x){
            errorAlert("File Could Not Be Read", x.getMessage());
            return;
        }

    }

    /**
     * Save current composition as a new file
     */
    @FXML
    public void saveAs() {
        this.compositionPanelController.stopComposition();
        //Setup a temporary variable to protect an accidental overwrite
        File temp = this.chooser.showSaveDialog(new Stage());
        if(temp == null) {//If the user cancels
            return;
        } else {
            this.currentOpenFile = temp;
        }
        this.writeFile(this.XMLHandler.createXML(
                this.compositionPanelController.getRectangles()),
                this.currentOpenFile
        );
    }

    /**
     * Write content to a file
     * @param contentToWrite
     * @param file
     */
    @FXML
    private void writeFile(String contentToWrite, File file){
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(contentToWrite);
            fileWriter.close();
        }
        catch (IOException ex) {
            errorAlert("File Could not be Written", ex.getMessage());
        }
    }

    /**
     * Save current state of composition to a file
     */
    @FXML
    public void save() {
        this.compositionPanelController.stopComposition();
        if (this.currentOpenFile == null) {
            this.saveAs();
        }
        else {
            this.writeFile(this.XMLHandler.createXML(
                    this.compositionPanelController.getRectangles()),
                    this.currentOpenFile
            );
        }
    }

    /**
     * creates and displays a dialog warning box which allows the user to
     * verify one of the options shown. Returns a value to identify the
     * user's choice
     * @return result an int corresponding to the desired outcome
     */
    public int generateConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning: You Have Unsaved Changes");
        alert.setHeaderText("You currently have unsaved changes in your composition.\n" +
                "Would you like to save those changes before closing?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){
            return 1;
        } else if (result.get() == noButton) {
            return 0;
        } else {
            return 2;
        }
    }


    /**
     * if there are unsaved changes, ask user if they want to save
     */
    public void checkUnsavedChanges(){
        if(hasUnsavedChanges()) {
            int result = generateConfirmationDialog();
            switch(result) {
                //user selected save changes
                case 1:
                    save();
                    //user did not select a button
                case 0:
                    break;
                //user selected not to save changes
                case 2:
                    return;
            }
        }
    }
    /**
     * returns a String representing the characters
     * read from the input File
     * @param file the input File to be read
     * @returns lines a String representation of the file
     */
    public String readFile(File file) throws IOException{
        String lines = "";

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line =  br.readLine()) != null){
                lines += line + "\n";
            }
            br.close();


        return lines;
    }

    /**
     * Pop up an error box
     * @param type the type of error that occurred
     * @param e the message to be displayed in the box
     */
    public void errorAlert(String type, String e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(type);
        alert.setContentText(e);
        alert.show();
    }
    /**
     * compares the current composition to the saved file (if available)
     * and returns true if they are different and false if there are no differences
     * @returns result boolean which represent whether or not there are unsaved changes
     */
    public boolean hasUnsavedChanges() {
        boolean result = false;
        List<String> saved = null;

        //if there are rectangles in the composition, prompt the warning
        if (this.currentOpenFile == null &&
                this.compositionPanelController.getRectangles().size() > 0) {
            result = true;
        }
        //if the current save file differs from the composition, prompt the warning
        else if (this.currentOpenFile != null){
        try {
            saved = Arrays.asList(
                    readFile(this.currentOpenFile).split("\n")
            );
        }
        catch (IOException x){
            //passivly tell user there are unsaved changes
            return true;


        }
            List<String> current = Arrays.asList(this.XMLHandler.createXML(
                    this.compositionPanelController.getRectangles()
            ).split("\n"));
            for (String s : saved) {
                if (!current.contains(s)) {result = true;}
            }
            for (String s : current) {
                if (!saved.contains(s)) {result = true;}
            }
        }
        return result;

    }
}
