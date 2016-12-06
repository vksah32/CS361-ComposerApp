package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * Controls and directs methods to modify
 */
public class ContextMenuHandler {
    private SelectableRectangle sourceRectangle;
    private CompositionPanelController compController;

    public ContextMenuHandler(CompositionPanelController compController, SelectableRectangle rec){
        this.compController = compController;
        this.sourceRectangle = rec;
    }

    public void delete(ActionEvent e){
        this.compController.deleteSelectedNotes();
    }

    public void playSelected(ActionEvent e){
        this.compController.playSection(this.compController.getSelectedNotes());
    }

    /**
     * Create a context menu for a specified rectangle
     * @param rec
     * @return
     */
    public static ContextMenu createRightClickMenu(CompositionPanelController controller,
                                                   SelectableRectangle rec){
        ContextMenuHandler handler = new ContextMenuHandler(controller, rec);
        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(handler::delete);
        MenuItem playSelected = new MenuItem("Play selected");
        playSelected.setOnAction(handler::playSelected);
        MenuItem setInstrument = new MenuItem("Set Instrument...");
        MenuItem setVolume = new MenuItem("Set Volume...");
        menu.getItems().addAll(delete, playSelected, setInstrument, setVolume);
        return menu;
    }

    public static void setUpListeners(SelectableRectangle rectangle, ContextMenu contextMenu){
        rectangle.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            contextMenu.show(rectangle, event.getScreenX(), event.getScreenY());
            event.consume();
        });
        rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            contextMenu.hide();
        });
    }

}
