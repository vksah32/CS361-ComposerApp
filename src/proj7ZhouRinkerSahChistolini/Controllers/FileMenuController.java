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
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    /** The application's clipboardController */
    private ClipBoardController clipboardController;

    public void initialize(){
        this.currentOpenFile =null;
        chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("fxml files(*.fxml)", "*.fxml");
        chooser.getExtensionFilters().add(extFilter);
    }

    public void init(CompositionPanelController compositionPanelController,
                     ClipBoardController clipboardController){
        this.compositionPanelController = compositionPanelController;
        this.clipboardController = clipboardController;
    }

    /**
     * Ensures that all processes are killed on the
     * destruction of the window.
     */
    @FXML
    public void cleanUpOnExit() {
        Platform.exit();
        System.exit(0);
    }

    /** Dialog window that gives information about the application */
    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("This is a musical composition development software \n Authors: \n Vivek Sah, " +
                "Victoria Chistolini, Alex Rinker and Ed Zhou");
        alert.show();
    }

    /** Create new Composition */
    @FXML
    public void createNewDocument() throws IOException {
// TODO: 11/16/2016 check if there is current changes to prompt save 
        // check if modified
        File temp = File.createTempFile("temp-file-name", ".tmp");
        String current = ClipBoardController.createXML(this.compositionPanelController.getRectangles());
        this.writeFile(current,temp);

        this.compositionPanelController.reset();
        this.currentOpenFile = null;
    }

    /** Open a new composition file */
    @FXML
    public void open() throws IOException {
        this.currentOpenFile = this.chooser.showOpenDialog(new Stage());
        String lines = "";
        BufferedReader br = new BufferedReader(new FileReader(this.currentOpenFile));
        String line;
        while ((line =  br.readLine()) != null){
            lines += line;
        }
        br.close();
        this.compositionPanelController.reset();
        this.clipboardController.stringToComposition(lines);
    }

    /**
     * Save current composition as a new file
     */
    @FXML
    public void saveAs() {
        this.currentOpenFile = this.chooser.showSaveDialog(new Stage());
        this.writeFile(ClipBoardController.createXML(this.compositionPanelController.getRectangles()),
                                                     this.currentOpenFile);
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
            System.out.println("not file");
        }
    }

    /**
     * Save current state of composition to a file
     */
    @FXML
    public void save() {
        if (this.currentOpenFile == null) {
            this.saveAs();
        }
        else {
            this.writeFile(ClipBoardController.createXML(this.compositionPanelController.getRectangles()),
                                                         this.currentOpenFile);
        }
    }

}
