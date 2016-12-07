/**
 * File: EditMenuController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 8
 * Date: Nov 14, 2016
 */
package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import proj8ZhouRinkerSahChistolini.Controllers.Actions.DeleteNoteAction;
import proj8ZhouRinkerSahChistolini.Controllers.Actions.PasteAction;
import proj8ZhouRinkerSahChistolini.Controllers.Actions.SelectAction;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * This class handles all of the MenuItems associated
 * with the Edit Menu. It acts as an extension/minion to the
 * main controller
 */
public class EditMenuController {
    /** a controller to assist in bindings between the menus and controllers*/
    private BindingController bindingController;

    /**a controller to assist with clipboard actions ***/
    private ClipBoardController clipboardController;

    /** CompositionPanelController reference */
    private CompositionPanelController compositionPanelController;

    private ZoomHandler zoomHandler;

    /**
     * All of our MenuItem are put into fields
     * Each of the following FXML elements are MenuItems defined in
     * our fxml. They are injected here into our controller so that
     * we can bind their disable properties
     */
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem groupButton;
    @FXML
    private MenuItem ungroupButton;
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem redoButton;
    @FXML
    private MenuItem cutButton;
    @FXML
    private MenuItem copyButton;
    @FXML
    private MenuItem pasteButton;
    @FXML
    public MenuItem selectAllButton;

    /** Initializes the controllers so they can communicate properly */
    @FXML
    public void init(CompositionPanelController compController,
                     BindingController bindingController,
                     ClipBoardController clipboardController) {
        this.compositionPanelController = compController;
        this.bindingController = bindingController;
        this.clipboardController = clipboardController;
        this.zoomHandler = new ZoomHandler(this.compositionPanelController);
        this.bindMenuItems();

    }

    @FXML
    /**
     * deletes the selected notes
     */
    public void deleteSelectedNotes() {
        this.compositionPanelController.stopComposition();
        DeleteNoteAction deletedNotes = new DeleteNoteAction(
                this.compositionPanelController.getSelectedRectangles(),
                this.compositionPanelController.getSelectedNotes(),
                this.compositionPanelController
        );
        this.compositionPanelController.deleteSelectedNotes();
        this.compositionPanelController.addAction(deletedNotes);
    }

    @FXML
    /**
     * selects all the notes
     */
    public void selectAllNotes() {
        this.compositionPanelController.stopComposition();
        //add
        Collection<SelectableRectangle> before = (
                this.compositionPanelController.getSelectedRectangles()
        );
        this.compositionPanelController.selectAllNotes();
        this.compositionPanelController.addAction(
                new SelectAction(
                        before,
                        this.compositionPanelController.getRectangles(),
                        this.compositionPanelController
                )
        );
    }

    @FXML
    /**
     * group the selected notes
     */
    public void groupSelected() {
        this.compositionPanelController.stopComposition();
        this.compositionPanelController.groupSelected(
                this.compositionPanelController.getSelectedRectangles()
        );
    }

    @FXML
    /**
     * copies the selected rectangles
     */
    public void copySelected() {
        this.compositionPanelController.stopComposition();
        this.clipboardController.copySelected();
    }

    @FXML
    /**
     * cut the selected rectangles
     */
    public void cutSelected() {
        this.compositionPanelController.stopComposition();
        this.clipboardController.copySelected();
        this.deleteSelectedNotes();
    }

    @FXML
    /**
     * paste the copied rectangles
     */
    public void pasteSelected() {
        this.compositionPanelController.stopComposition();
        this.clipboardController.pasteSelected();
        PasteAction pastedNotes = new PasteAction(
                this.compositionPanelController.getSelectedRectangles(),
                this.compositionPanelController.getSelectedNotes(),
                this.compositionPanelController);
        this.compositionPanelController.addAction(pastedNotes);
    }

    @FXML
    /**
     * ungroup the selected notes
     */
    public void ungroupSelected() {
        this.compositionPanelController.stopComposition();
        this.compositionPanelController.ungroupSelected(
                this.compositionPanelController.getSelectedRectangles()
        );
    }

    @FXML
    /**
     * zoom in 20%
     */
    public void zoomIn() {
        this.zoomHandler.zoomIn();
    }

    @FXML
    /**
     * zoom out 20%
     */
    public void zoomOut(){
        this.zoomHandler.zoomOut();
    }

    @FXML
    /**
     * undoes the latest action
     */
    public void undo() {
        this.compositionPanelController.stopComposition();
        this.compositionPanelController.getActionController().undo();
    }

    @FXML
    /**
     * redoes the latest undo action
     */
    public void redo() {
        this.compositionPanelController.stopComposition();
        this.compositionPanelController.getActionController().redo();
    }

    /**
     * Sets up the bindings for the menuButtons in order to disable them
     */
    public void bindMenuItems() {
        //deleteButton
        this.deleteButton.disableProperty().bind(
                this.bindingController.getAreNotesSelectedBinding()
        );
        //groupButton
        this.groupButton.disableProperty().bind(
                this.bindingController.getMultipleSelectedBinding()
        );
        //UngroupButton
        this.ungroupButton.disableProperty().bind(
                this.bindingController.getGroupSelectedBinding()
        );
        //redoButton
        this.redoButton.disableProperty().bind(
                this.bindingController.getRedoEmptyBinding()
        );
        //undoButton
        this.undoButton.disableProperty().bind(
                this.bindingController.getUndoEmptyBinding()
        );
        //cutButton
        this.cutButton.disableProperty().bind(
                this.bindingController.getAreNotesSelectedBinding()
        );
        //copyButton
        this.copyButton.disableProperty().bind(
                this.bindingController.getAreNotesSelectedBinding()
        );
        //selectAllButton
        this.selectAllButton.disableProperty().bind(
                this.bindingController.getChildrenProperty().sizeProperty().isEqualTo(0)
        );
    }
}
