/**
 * File: DragInPanelHandler.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */

package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import proj8ZhouRinkerSahChistolini.Controllers.Actions.SelectAction;
import proj8ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

public class DragInPanelHandler {

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

    private Collection<SelectableRectangle> before;
    private Collection<SelectableRectangle> after;

    /**
     * Creates a new DragInPaneHandler
     * @param compController The main composition controller
     */
    public DragInPanelHandler(Rectangle selectionRectangle,
                              CompositionPanelController compController){
        this.compController = compController;
        this.selectionRectangle = selectionRectangle;
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
        before = this.compController.getSelectedRectangles();
    }

    /**
     * creates a selection rectangle when the mouse is dragged in panel
     * @param event The even associated with this mouse drag
     */
    public void handleDragged(MouseEvent event) {
        this.selectionRectangle.setVisible(true);
        double scaleFactor = this.compController.getZoomFactor().getValue();
        if(!this.metaDown &&
                (this.selectionRectangle.getWidth() > 5 ||
                        this.selectionRectangle.getHeight() > 5))
        {
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

        double descaledLeftX = Math.min(event.getX()/scaleFactor,
                this.startX/scaleFactor);
        double descaledWidth = Math.abs(event.getX()/scaleFactor-
                this.startX/scaleFactor);
        double descaledLowestY = Math.min(event.getY()/scaleFactor,
                this.startY/scaleFactor);
        double descaledHeight = Math.abs(event.getY()/scaleFactor
                -this.startY/scaleFactor);

        for (SelectableRectangle rectangle : compController.getRectangles()) {
            if (rectangle.intersects(descaledLeftX, descaledLowestY,
                    descaledWidth, descaledHeight)) {
                rectangle.setSelected(true);
            }
        }


    }

    /**
     * handles when the mouse is released after being dragged in panel
     * @param event The mouse event associated with this mouse release
     */
    public void handleDragReleased(MouseEvent event) {
        if (this.selectionRectangle.isVisible()){
            this.after = this.compController.getSelectedRectangles();
            if (!this.before.equals(this.after)) {
                this.compController.addAction(new SelectAction(this.before,
                        this.after,
                        this.compController));
            }
        }
        this.selectionRectangle.setVisible(false);
    }
}