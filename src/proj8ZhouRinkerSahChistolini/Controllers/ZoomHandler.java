package proj8ZhouRinkerSahChistolini.Controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by jhkamherst on 12/4/16.
 */
public class ZoomHandler {
    private CompositionPanelController compControl;
    private DoubleProperty currentZoomScale;

    public ZoomHandler(CompositionPanelController compControl){
        this.compControl=compControl;
        this.currentZoomScale = new SimpleDoubleProperty(1);
        compControl.getZoomFactor().bind(this.currentZoomScale);
    }



    /**
     * zoom in by 25%
     */
    public void zoomIn(){

        this.currentZoomScale.set(this.currentZoomScale.getValue()+0.20);
    }

    /**
     * zoom out by 25%
     */
    public void zoomOut(){
        double newScale = Math.max(0.75,this.currentZoomScale.getValue()-0.20 );
        this.currentZoomScale.set(newScale);
    }
}
