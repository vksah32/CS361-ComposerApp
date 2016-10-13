/**
 * File: RectangleToNoteConverter.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj4MalionekLamChistoliniSah;

import java.util.ArrayList;

/**
 * Converts a NoteRectangle to a Note
 */
public class RectangleToNoteConverter {

    private CompositionPanel panel;
    private Composition composition;

    public RectangleToNoteConverter(CompositionPanel panel, Composition composition){
        this.composition = composition;
        this.panel = panel;
    }


    public void loadComposition(){
        ArrayList<NoteRectangle> rectangles = this.panel.getRectangles();
        ArrayList<Note> convertedNotes=RectangleToNoteConverter.convertRectangles(rectangles);
        this.composition.addNotes(convertedNotes);
    }


    /**
     * converts a NoteRectangle to a Note
     * @param rectangle
     * @return a note
     */
    public static Note convertRectangle(NoteRectangle rectangle){
        int duration = (int)rectangle.getWidth();
        int startTick = (int)rectangle.getX();
        int pitch = (127-(int)rectangle.getY()/10);
        Note note = new Note(startTick,duration,pitch);
        note.setInstrument(rectangle.getInstrument());
        return note;
    }

    /**
     * converts multiple NoteRectangles to Notes
     * @param rectangles
     * @return an ArrayList of Notes
     */
    public static ArrayList<Note> convertRectangles(ArrayList<NoteRectangle> rectangles){
        ArrayList<Note> notes = new ArrayList<>();
        for(NoteRectangle rectangle: rectangles){
            notes.add(RectangleToNoteConverter.convertRectangle(rectangle));
        }
        return notes;
    }
}
