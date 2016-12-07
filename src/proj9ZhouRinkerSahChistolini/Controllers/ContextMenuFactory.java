/**
 * File: ContextMenuFactory.java
 *
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 9
 * Date: 12/6/2016, 2016
 */

package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeInstrumentAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ChangeVolumeAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.DeleteNoteAction;
import proj9ZhouRinkerSahChistolini.Models.Playable;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aids in creation and linking to right click menus on non control nodes
 */
public class ContextMenuFactory {

    /** Source Node to be given the menu*/
    private Node sourceRectangle;
    /** Composition Controller */
    private CompositionPanelController compController;
    
    /** Initiate Factory*/
    public ContextMenuFactory(CompositionPanelController compController, Node rec) {
        this.compController = compController;
        this.sourceRectangle = rec;
    }

    /**
     * Create a context menu for a specified rectangle
     * @return
     */
    public ContextMenu createPlayableRightClickMenu() {
        PlayableContextMenuHandler handler = new PlayableContextMenuHandler();
        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem playSelected = new MenuItem("Play selected");
        Menu setInstrument = new Menu("Set Instrument...");
        MenuItem setVolume = new MenuItem("Set Volume...");

        delete.setOnAction(handler::delete);
        playSelected.setOnAction(handler::playSelected);
        this.compController.getInstrumentPanelController().getInstruments().forEach(i -> {
            MenuItem instrument = new MenuItem(i.getName());
            setInstrument.getItems().add(instrument);
            instrument.setOnAction(a -> handler.setInstrument(instrument.getText()));
        });
        setVolume.setOnAction(handler::setVolume);

        menu.getItems().addAll(delete, playSelected, setInstrument, setVolume);


        return menu;
    }

    /**
     * Create listeners to right click menu requests to the none control source node
     * @param contextMenu
     */
    public void setUpListeners(ContextMenu contextMenu) {
        sourceRectangle.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            contextMenu.show(sourceRectangle, event.getScreenX(), event.getScreenY());
            event.consume();
        });
        sourceRectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            contextMenu.hide();
        });
    }

    /**
     * Handles Playable Context menu
     */
    private class PlayableContextMenuHandler {

        /**
         * Delete selected notes
         */
        private void delete(ActionEvent e) {
            compController.stopComposition();
            DeleteNoteAction deletedNotes = new DeleteNoteAction(
                    compController.getSelectedRectangles(),
                    compController.getSelectedNotes(),
                    compController
            );
            compController.deleteSelectedNotes();
            compController.addAction(deletedNotes);
        }

        /** Play selected notes */
        private void playSelected(ActionEvent e) {
            compController.playSection(compController.getSelectedNotes());
        }

        /** Set selected Rectangles to a specified instrument*/
        private void setInstrument(String text){
            List<SelectableRectangle> before = new ArrayList<>();
            compController.getSelectedRectangles().forEach(n->before.add(n));
            compController.addAction(new ChangeInstrumentAction(compController,before,text));
            before.forEach(n-> {
                n.setInstrument(
                        compController.getInstrumentPanelController().getInstruments()
                                .stream()
                                .filter(
                                        i -> i.getName().equals(text)
                                ).collect(Collectors.toList()).get(0).getValue()
                );
            });
        }

        /** Set selected Notes to a specified volume through dialog box */
        private void setVolume(ActionEvent e){
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Set Volume");
            TextField text = new TextField(Integer.toString(average(compController.getSelectedNotes())));
            GridPane grid = new GridPane();
            grid.add(new Label("Integer 0-127: "), 1, 1);
            grid.add(text, 2, 1);
            dialog.getDialogPane().setContent(grid);
            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
            dialog.setResultConverter((b) -> {
                    if (b == buttonTypeOk && isValidNumeric(text.getText())) {
                        return Integer.parseInt(text.getText());
                    }
                    return null;
            });
            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()){
                compController.addAction(new ChangeVolumeAction(compController.getSelectedNotes(),result.get()));
                compController.getSelectedNotes().forEach( n -> {
                    n.setVolume(result.get());
                });
            }
        }

        /**
         * Get the average volume of a collection of notes
         * */
        private int average(Collection<Playable> notes){
            int total = 0;
            for (Playable child : notes){
                total += child.getVolume();
            }
            return total/notes.size();
        }

        /** Check if value is correct format*/
        private boolean isValidNumeric(String str) {
            try {
                double d = Integer.parseInt(str);
                if(!(d  <= 127.0 && d >= 0.0)){
                    throw new NumberFormatException();
                }
            }
            catch(NumberFormatException nfe) {
                return false;
            }
            return true;
        }

    }
}
