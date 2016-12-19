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

package proj9ZhouRinkerSahChistolini.Controllers;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.Actionable;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.ExtendNoteAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.SelectAction;
import proj9ZhouRinkerSahChistolini.Controllers.Actions.TranslateNoteAction;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.ArrayList;
import java.util.Collection;

/**Handles when the user drags in a note rectangle*/
public class DragInNoteHandler {
    /** The mouse x coordinate from the previous mouse event*/
    private double previousX;
    /** The mouse y coordinate from the previous mouse event*/
    private double previousY;
    /** Keeps track of whether the current drag action is extending
     *  rectangles or dragging rectangles
     */
    private boolean extendEventHappening;
    /** The rectangle that this handler is assigned to */
    private SelectableRectangle sourceRectangle;
    /** The main CompositionController */
    private CompositionPanelController compController;
    /**  if an extension occured */
    private Boolean didExtend;
    /** the final deltaX to move */
    private double totalDeltaX;
    /** the final deltaY to move */
    private double totalDeltaY;
    /** rectangles after current state action */
    private Collection<SelectableRectangle> beforeState;
    /** rectangles after current state action */
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
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }

        this.beforeState = this.compController.getSelectedRectangles();
        if (event.getX() >= this.sourceRectangle.getX() +
                            this.sourceRectangle.getWidth() - 5
        ) {
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
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        double deltaX = event.getX() - this.previousX;
        double deltaY = event.getY() - this.previousY;
        for (
            SelectableRectangle rectangle : this.compController.getSelectedRectangles()
        ) {
            rectangle.setUnboundPosition(rectangle.getX() + deltaX,
                                         rectangle.getY() + deltaY);
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
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        double deltaX = event.getX() -
                        this.sourceRectangle.getWidth() -
                        this.sourceRectangle.getX();
        for (SelectableRectangle rectangle : this.compController.getSelectedRectangles()){
            double width = rectangle.getWidth() + deltaX;
            // makes sure the width is at least 5
            width = Math.max(5, width);
            // makes sure the note does not extend past the end of the player
            rectangle.setUnboundWidth(width);
        }
        //Stop keeping track if dragged left of note
        if (event.getX() > this.sourceRectangle.getX()+5) {
            this.totalDeltaX += deltaX;
        }

    }

    /**
     * Takes care of what happens when the mouse is released after a drag
     * @param event the MouseEvent fired when the mouse was released
     */
    public void handleMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            return;
        }
        Collection<SelectableRectangle> changedRectangles = new ArrayList<>();
        double adjustY = 0;
        for (SelectableRectangle rectangle :
                this.compController.getSelectedRectangles()){
            double newPitch = Math.floor((rectangle.getY() - 1) / 10) * 10 + 1;
            //change adjust based on parent rectangle if grouping
            if(!rectangle.xProperty().isBound())
                adjustY = newPitch - rectangle.getY();
            rectangle.setUnboundY(newPitch);
            changedRectangles.add(rectangle);
        }
        this.totalDeltaY += adjustY;

        if (!event.isStillSincePress()) {
            this.afterState = this.compController.getSelectedRectangles();
            if (didExtend) {
                if (!(this.compController
                          .getActionController()
                          .peekUndo()
                          instanceof SelectAction)) {
                    this.compController.addAction(new SelectAction(this.beforeState,
                                                                   this.afterState,
                                                                   this.compController)
                    );
                }
                Actionable extendedAction = new ExtendNoteAction(changedRectangles,
                                                                 totalDeltaX
                );
                this.compController.addAction(extendedAction);
            } else {
                //If an prior unselected note is dragged, add selectionAction as well
                if(!this.beforeState.equals(this.afterState)){
                    this.compController.addAction(
                            new SelectAction(this.beforeState,
                                             this.afterState,
                                             this.compController)
                    );
                }
                Actionable translatedAction = new TranslateNoteAction(
                        changedRectangles, totalDeltaX, totalDeltaY, this.compController
                );
                this.compController.addAction(translatedAction);
            }

            //reset control fields
            this.totalDeltaX = 0;
            this.totalDeltaY = 0;
            this.didExtend = false;
        }
        this.compController.getPropPanelController().populatePropertyPanel();
        event.consume();
    }
}



