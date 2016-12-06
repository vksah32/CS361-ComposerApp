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

package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import proj8ZhouRinkerSahChistolini.Models.Playable;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * Aids in creation and linking to right click menus on non control nodes
 */
public class ContextMenuFactory {

    private Node sourceRectangle;
    private CompositionPanelController compController;
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
        MenuItem setInstrument = new MenuItem("Set Instrument...");
        MenuItem setVolume = new MenuItem("Set Volume...");

        delete.setOnAction(handler::delete);
        playSelected.setOnAction(handler::playSelected);
        setInstrument.setOnAction(handler::setInstrument);

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

        private void delete(ActionEvent e) {
            compController.deleteSelectedNotes();
        }

        private void playSelected(ActionEvent e) {
            compController.playSection(compController.getSelectedNotes());
        }
        private void setInstrument(ActionEvent e){
            
            compController.getSelectedRectangles().forEach(n->
                    ((SelectableRectangle) n).setInstrument(6)
            );
        }

    }
}
