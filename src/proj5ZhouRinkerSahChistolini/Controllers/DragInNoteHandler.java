/**
 * File: DragInNoteHandler.java
 *
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.input.MouseEvent;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**Handles when the user drags in a note rectangle*/
public class DragInNoteHandler {
    /** The mouse x coordinate from the previous mouse event*/
    private double previousX;
    /** The mouse y coordinate from the previous mouse event*/
    private double previousY;
    /** Keeps track of whether the current drag action is extending rectangles or dragging rectangles */
    private boolean extendEventHappening;
    /** The rectangle that this handler is assigned to */
    private SelectableRectangle sourceRectangle;
    /** The main CompositionController */
    private CompositionPanelController compController;

    /**Creates a new DragInNoteHandler
     *
     * @param sourceRectangle The rectangle that this listens to
     * @param compController The main compositionController
     */
    public DragInNoteHandler(SelectableRectangle sourceRectangle,
                             CompositionPanelController compController) {
        this.sourceRectangle = sourceRectangle;
        this.compController = compController;
    }

    /**
     * Handles when the mouse presses on this handlers rectangle
     * @param event the MouseEvent associated with the mouse press
     */
    public void handleMousePressed(MouseEvent event) {
        this.compController.stopComposition();
        if (event.getX() >= this.sourceRectangle.getX() + this.sourceRectangle.getWidth() - 5) {
            this.extendEventHappening = true;
        } else {
            this.extendEventHappening = false;
        }
        this.previousX = event.getX();
        this.previousY = event.getY();
        event.consume();
    }


    /**
     * Handles when the mouse is dragging
     * @param event the MouseEvent associated with this mouse drag
     */
    public void handleDragged(MouseEvent event) {
        this.compController.stopComposition();
        if (!this.sourceRectangle.isSelected()) {
            this.compController.clearSelected();
            this.sourceRectangle.setSelected(true);
        } else {
            if (this.extendEventHappening) {
                this.handleNoteExtend(event);
            } else {
                this.handleNoteTranslate(event);
            }
        }

        event.consume();
    }

    /**
     * Handles dragging notes across the pane
     * @param event the MouseEvent associated with the mouse drag
     */
    private void handleNoteTranslate(MouseEvent event) {
        double deltaX = event.getX() - this.previousX;
        double deltaY = event.getY() - this.previousY;
        for (SelectableRectangle rectangle : this.compController.getSelectedRectangles()) {
            if (!rectangle.isBounded()){
                rectangle.setX(rectangle.getX() + deltaX);
                rectangle.setY(rectangle.getY() + deltaY);
            }
        }
        this.previousX = event.getX();
        this.previousY = event.getY();
    }

    /**
     * handles extending the lengths of the selected notes
     * @param event the MouseEvent associated with the mouse drag
     */
    private void handleNoteExtend(MouseEvent event) {
        double deltaX = event.getX() - this.sourceRectangle.getWidth() - this.sourceRectangle.getX();
        for (SelectableRectangle rectangle : this.compController.getSelectedRectangles()) {
            if (!rectangle.isBounded()) {
                double width = rectangle.getWidth() + deltaX;
                // makes sure the width is at least 5
                width = Math.max(5, width);
                // makes sure the note does not extend past the end of the player
                // width = Math.min(width, this.panelToEdit.getWidth()-rectangle.getX());
                rectangle.setWidth(width);
            }
        }

    }

    /**
     * Takes care of what happens when the mouse is released after a drag
     * @param event the MouseEvent fired when the mouse was released
     */
    public void handleMouseReleased(MouseEvent event) {
        for (SelectableRectangle rectangle : this.compController.getSelectedRectangles()) {
            if (!rectangle.isBounded()) {
                double newPitch = Math.floor((rectangle.getY() - 1) / 10) * 10 + 1;
                rectangle.setY(newPitch);
            }
        }
    }
}



