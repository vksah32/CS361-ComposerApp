/**
 * File: DragInPanelHandler.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class DragInPanelHandler {

    /** The panel that this handler actively edits and listens to */
    private Pane panelToEdit;
    /** The controller of the composition panel */
    private CompositionPanelController compController;
    /** The rectangle which appears when you select a group of notes*/
    private Rectangle selectionRectangle;
    /** The x coordinate at which the drag event originated*/
    private double startX;
    /** The y coordinate at which the drag event originated*/
    private double startY;
    /** Whether or not the control key is held down*/
    private boolean metaDown;

    /** Creates a new DragInPaneHandler
     *
     * @param panelToEdit The panel which this handler edits
     * @param compController The main composition controller
     */
    public DragInPanelHandler(Pane panelToEdit, CompositionPanelController compController){
        this.panelToEdit = panelToEdit;
        this.compController = compController;
        this.selectionRectangle = new Rectangle();
        this.selectionRectangle.setVisible(false);
        this.selectionRectangle.setId("selectionRectangle");
        this.panelToEdit.getChildren().add(this.selectionRectangle);
    }

    /**
     * Handles when the mouse is pushed down
     * @param event the event associated with the mouse push down
     */
    public void handleMousePressed(MouseEvent event){
        this.startX = event.getX();
        this.startY = event.getY();
        this.selectionRectangle.setX(this.startX);
        this.selectionRectangle.setY(this.startY);
        this.metaDown = event.isShortcutDown();
    }

    /**
     * Handles when the mosue is dragged
     * @param event The even associated with this mouse drag
     */
    public void handleDragged(MouseEvent event) {
        this.selectionRectangle.setVisible(true);
        if(!this.metaDown){
            this.compController.clearSelected();
        }
        double leftX = Math.min(event.getX(),this.startX);
        double width = Math.abs(event.getX()-this.startX);
        double lowestY = Math.min(event.getY(),this.startY);
        double height = Math.abs(event.getY()-this.startY);
        this.selectionRectangle.setWidth(width);
        this.selectionRectangle.setHeight(height);
        this.selectionRectangle.setX(leftX);
        this.selectionRectangle.setY(lowestY);
        ArrayList<NoteRectangle> rectangles = compController.getRectangles();
        for(NoteRectangle rectangle: rectangles){
            if(rectangle.intersects(leftX,lowestY,width,height)){
                rectangle.setSelected(true);
            }
        }
    }

    /** handles when the mouse is released
     *
     * @param event The mouse event associated with this mouse release
     */
    public void handleDragReleased(MouseEvent event) {
        this.selectionRectangle.setVisible(false);
    }
}
