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

import java.util.ArrayList;
import java.util.Collection;

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

    private Boolean didExtend;
    private double totalDeltaX;
    private double totalDeltaY;
    private Collection<SelectableRectangle> beforeState;
    private Collection<SelectableRectangle> afterState;

    /**Creates a new DragInNoteHandler
     *
     * @param sourceRectangle The rectangle that this listens to
     * @param compController The main compositionController
     */
    public DragInNoteHandler(SelectableRectangle sourceRectangle,
                             CompositionPanelController compController) {
        this.sourceRectangle = sourceRectangle;
        this.compController = compController;
        this.didExtend = false;
        this.totalDeltaX = 0;
        this.totalDeltaY = 0;
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
        this.beforeState = this.compController.getSelectedRectangles();
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
                this.didExtend = true;
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
            rectangle.setUnboundPosition(rectangle.getX() + deltaX, rectangle.getY() + deltaY);
        }
        this.totalDeltaX += deltaX;
        this.totalDeltaY += deltaY;
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
            double width = rectangle.getWidth() + deltaX;
            // makes sure the width is at least 5
            width = Math.max(5, width);
            // makes sure the note does not extend past the end of the player
            // width = Math.min(width, this.panelToEdit.getWidth()-rectangle.getX());
            rectangle.setUnboundWidth(width);
        }
        this.totalDeltaX += deltaX;

    }

    /**
     * Takes care of what happens when the mouse is released after a drag
     * @param event the MouseEvent fired when the mouse was released
     */
    public void handleMouseReleased(MouseEvent event) {
        Collection<SelectableRectangle> changedRectangles = new ArrayList<>();
        for (SelectableRectangle rectangle : this.compController.getSelectedRectangles()) {
            double newPitch = Math.floor((rectangle.getY() - 1) / 10) * 10 + 1;
            rectangle.setUnboundY(newPitch);
            changedRectangles.add(rectangle);

        }
        if (!event.isStillSincePress()) {
            this.afterState = this.compController.getSelectedRectangles();
            if (didExtend) {
                this.compController.addAction(new SelectAction(this.beforeState, this.afterState));
                Actionable extendedAction = new ExtendNoteAction(changedRectangles, totalDeltaX);
                this.compController.addAction(extendedAction);
            } else {
                this.compController.addAction(new SelectAction(this.beforeState, this.afterState));
                Actionable translatedAction = new TranslateNoteAction(changedRectangles, totalDeltaX, totalDeltaY);
                this.compController.addAction(translatedAction);

            }

            //reset control fields
            this.totalDeltaX = 0;
            this.totalDeltaY = 0;
            this.didExtend = false;
        }
        event.consume();
    }


}



