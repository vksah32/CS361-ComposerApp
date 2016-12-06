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

package proj8ZhouRinkerSahChistolini.Views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import proj8ZhouRinkerSahChistolini.Controllers.InstrumentPanelController;

/**
 * Represents a musical note in the gui interface
 */
public class NoteRectangle extends SelectableRectangle {

    /**int value for the instrument**/
    private IntegerProperty instrument;
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
        this.getStyleClass().add(styleName.toLowerCase().replace(" ", "-"));
        this.instrument = new SimpleIntegerProperty();
        this.instrument.addListener(e -> {
            this.getStyleClass().remove(styleName.toLowerCase().replace(" ", "-"));
            this.getStyleClass().add("note");
            this.getStyleClass().add(instrController.getInstrument(
                    this.getInstrument()).getName().toLowerCase().replace(" ", "-"));
        });
        this.instrument.setValue(instr);
    }

    /**
     * returns the instrument value of this rectangle
     */
    public int getInstrument() {return this.instrument.intValue();}

    /**
     * returns the instrument property
     */
    public IntegerProperty instrumentProperty(){return this.instrument;}

    public void setInstrument(int val){
        this.instrument.set(val);

    }
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

    @Override
    /**
     * x,y,width, name, channel, integer representing MIDI instrucment
     * @return
     */
    public String toString() {return toXML(0);}

    /**
     * x,y,width, name, channel, integer representing MIDI instrucment
     * @return
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
