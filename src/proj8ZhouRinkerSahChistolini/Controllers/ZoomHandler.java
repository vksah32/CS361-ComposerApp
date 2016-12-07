/**
 * File: ActionController.java
 * @author Victoria Chistolini
 * @author Edward (osan) Zhou
 * @author Alex Rinker
 * @author Vivek Sah
 * Class: CS361
 * Project: 5
 * Date: Nov 1, 2016
 */

package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * controls zooming in and out of the composition
 */
public class ZoomHandler {

    /** current zoom of the panel*/
    private DoubleProperty currentZoomScale;

    /**
     * creates new zoom handler to control zooming in the panel
     * @param compControl reference to the composition panel controller
     */
    public ZoomHandler(CompositionPanelController compControl){

        //binds to the zoom factor in the composition panel contoller
        this.currentZoomScale = new SimpleDoubleProperty(1);
        compControl.getZoomFactor().bind(this.currentZoomScale);
    }


    /**
     * zoom in by 20%, there is no current cap on zooming in
     */
    public void zoomIn(){

        this.currentZoomScale.set(this.currentZoomScale.getValue()+0.20);
    }

    /**
     * zoom out by 20%, make is 60% the origional size
     */
    public void zoomOut(){
        double newScale = Math.max(0.6,this.currentZoomScale.getValue()-0.20 );
        this.currentZoomScale.set(newScale);
    }
}
