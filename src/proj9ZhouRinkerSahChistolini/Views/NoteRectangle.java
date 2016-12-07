/**
 * File: NoteRectangle.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj9ZhouRinkerSahChistolini.Views;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import proj9ZhouRinkerSahChistolini.Controllers.InstrumentPanelController;

/**
 * Represents a musical note in the gui interface
 */
public class NoteRectangle extends SelectableRectangle {

    /**int value for the instrument**/
    private IntegerProperty instrument;
    private String instrString;
    /** Modifier to color opacity */
    private IntegerProperty volume;
    private Rectangle transparency;
    /**
     * The constructor of the NoteRectangle
     * @param x
     * @param y
     * @param width
     * @param height
     * @param instr
     * @param styleName
     */
    public NoteRectangle(double x, double y,
                         double width, double height,
                         int instr, String styleName,
                         InstrumentPanelController instrController) {
        super(x, y, width, height);
        this.getStyleClass().add("note");
        this.instrString = styleName.toLowerCase().replace(" ", "-");
        this.getStyleClass().add(this.instrString);
        this.instrument = new SimpleIntegerProperty();
        this.instrument.addListener(e -> {
            getStyleClass().removeAll(instrString);
            String newInstrument = instrController.getInstrument(
                    getInstrument()).getName().toLowerCase().replace(" ", "-");
            getStyleClass().add(newInstrument);
            instrString = newInstrument;
        });
        this.instrument.setValue(instr);
        this.volume = new SimpleIntegerProperty();
        this.transparency = new Rectangle();
        this.transparency.xProperty().bind(this.xProperty());
        this.transparency.yProperty().bind(this.yProperty());
        this.transparency.heightProperty().bind(this.heightProperty());
        this.transparency.widthProperty().bind(this.widthProperty());
        this.transparency.setMouseTransparent(true);
        this.volume.addListener(e -> {
            this.transparency.setFill(Color.rgb(255,255,255,1.0 -  ((double) volumeProperty().intValue())/127.0));
            });
    }

    /**
     * returns the instrument value of this rectangle
     */
    public int getInstrument() {return this.instrument.intValue();}

    /**
     * returns the instrument property
     */
    public IntegerProperty instrumentProperty(){return this.instrument;}

    /**
     * Set the instrument property
     */
    public void setInstrument(int val){
        this.instrument.set(val);
    }

    /** Return the rectangle responsibile for tranparency*/
    public Rectangle getTransparency(){
        return this.transparency;
    }

    /**
     * returns the volume property
     */
    public IntegerProperty volumeProperty(){ return this.volume; }

    /**
     * sets the selection of the rectangle
     * @param selected
     */
    public void setSelected(boolean selected){
        if(selected){
            this.getStyleClass().removeAll("note");
            this.getStyleClass().add("selected-note");
        }
        else{
            this.getStyleClass().removeAll("selected-note");
            this.getStyleClass().add("note");
        }
        this.selected.set(selected);
    }

    public void populate(Pane pane, Transform transform){
        this.setSelected(true);
        this.getTransforms().add(transform);
        this.transparency.getTransforms().add(transform);
        pane.getChildren().add(this);
        pane.getChildren().add(this.transparency);
    }
    @Override
    public void toFront(){
        super.toFront();
        this.transparency.toFront();
    }
    @Override
    /**
     * x,y,width, name, channel, integer representing MIDI instrucment
     * @return
     */
    public String toString() {return toXML(0);}

    /**
     * Returns XML formatted string of NoteRectangle object
     * @param numTabs an int representing the indentation level
     *                to make the string more readable
     * @return String representation of the NoteRectangle
     */
    public String toXML(int numTabs) {
        String tabbing = (numTabs > 0) ? String.format("%" + numTabs*4 + "s", " ") : "";
        return tabbing +
                "<NoteRectangle " +
                "xpos=\"" + this.xProperty().intValue()    +"\" "+
                "ypos=\"" + this.yProperty().intValue() +"\" "+
                "width=\"" + this.widthProperty().getValue()     + "\" " +
                "instValue=\"" + this.instrument.intValue()  +"\" " +
                "/>\n";
    }
}
