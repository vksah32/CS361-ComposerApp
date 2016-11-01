package proj5ZhouRinkerSahChistolini.Controllers;

import proj5ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj5ZhouRinkerSahChistolini.Models.Note;

import java.util.Collection;
import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * holds information necessary to redo and undo adding a note
 */
public class AddNoteAction implements Actionable {

    private NoteRectangle graphicalNote;

    /** added note's model version */
    private Note modelNote;
    /** rectangles on the composition pane */
    private Collection<Node> recs;
    /** notes on the composition */
    private Collection<Note> notes;
    /** rectangles that are currently selected */
    private Collection<SelectableRectangle> selected;


    /**
     * Initilize new AddNoteActionEvent each time a new note is added
     *
     * @param graphicalNote the NoteRectanlge added to the composition panel
     * @param modelNote the musical note added to the composition in the model
     * @param selected the currently selected rectangles
     * @param comp the composition panel controller
     *
     */
    public AddNoteAction(NoteRectangle graphicalNote, Note modelNote, Collection<SelectableRectangle> selected, CompositionPanelController comp ){

        this.selected = selected;
        this.graphicalNote = graphicalNote;
        this.modelNote = modelNote;
        this.recs = comp.getCompositionPane().getChildren();
        this.notes = comp.getNotesfromComposition();

    }

    /**
     * redoes the action of adding a new note to the composition
     */
    @Override
    public void reDoIt() {
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(false);
        }
        this.graphicalNote.setSelected(true);
        recs.add(this.graphicalNote);
        notes.add(this.modelNote);
    }


    /**
     * undoes the action of adding a new note to the composition
     */
    @Override
    public void unDoIt() {
        recs.remove(this.graphicalNote);
        notes.remove(this.modelNote);
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(true);
        }
    }
}

