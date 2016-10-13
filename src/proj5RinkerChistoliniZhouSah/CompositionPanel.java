/**
 * File: CompositionPanel.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj4MalionekLamChistoliniSah;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import java.util.ArrayList;

/**
 * The pane in which all of the notes are stored and displayed.
 */
public class CompositionPanel extends Pane {

    /** an ArrayList of NoteRectangles */
    private ArrayList<NoteRectangle> rectangles;

    /**
     * Constructs the Panel and draws the appropriate lines.
     */
    public CompositionPanel()
    {
        this.drawLines();
        rectangles = new ArrayList<>();
    }

    /**
     * adds the NoteRectangles
     * @param rectangle
     * @param selected
     */
    public void addRectangle(NoteRectangle rectangle, boolean selected){
        this.getChildren().add(rectangle);
        rectangles.add(rectangle);
        if(selected){
            rectangle.setSelected(true);
        }
    }

    /**
     * Draws 127 lines with the specified spacing and colors.
     */
    private void drawLines()
    {
        for(int i = 1; i < 128; i++)
        {
            Line line = new Line(0, i*10+1, 2000,i*10+1);
            line.setId("lines");
            this.getChildren().add(line);
        }
    }

    /**
     * gets the rectangles
     * @return an ArrayList of the rectangles
     */
    public ArrayList<NoteRectangle> getRectangles(){
        return this.rectangles;
    }

    /**
     * gets the selectedRectangles
     * @return an ArrayList of the selected notes
     */
    public ArrayList<NoteRectangle> getSelectedRectangles(){
        ArrayList<NoteRectangle> selectedList = new ArrayList<>();
        for(NoteRectangle rectangle:this.rectangles){
            if(rectangle.isSelected()){
                selectedList.add(rectangle);
            }
        }
        return selectedList;
    }

    /**
     * clears the selection of the rectangles
     */
    public void clearSelected(){
        for(NoteRectangle rectangle:this.rectangles){
            if(rectangle.isSelected()){
                rectangle.setSelected(false);
            }
        }
    }

    /**
     * removes the selected rectangles
     */
    public void deleteSelectedNotes(){

        ArrayList<NoteRectangle> rectangles = this.getSelectedRectangles();
        //first remove from the panel
        for (NoteRectangle r: rectangles){
            this.getChildren().remove(r);
        }
        //then remove from ArrayList of rectangles
        this.rectangles.removeAll(rectangles);
    }

    /**
     * selects all the notes in the composition
     */
    public void selectAllNotes(){
        for (NoteRectangle rectangle: this.rectangles){
            rectangle.setSelected(true);
        }
    }
}
