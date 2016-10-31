package proj5ZhouRinkerSahChistolini.Controllers;

import proj5ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj5ZhouRinkerSahChistolini.Models.Note;

import java.util.Collection;
import java.util.HashSet;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

/**
 * holds information necessary to redo and undo an addNoteEvent
 */
public class AddNoteAction implements Actionable {

    private NoteRectangle graphicalNote;

    private Note modelNote;

    private Collection<SelectableRectangle> selected;

    public AddNoteAction(NoteRectangle graphicalNote, Note modelNote, Collection<SelectableRectangle> selected ){

        this.selected = selected;

        this.graphicalNote = graphicalNote;
        this.modelNote = modelNote;

    }

    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(false);
        }
        this.graphicalNote.setSelected(true);
        recs.add(this.graphicalNote);

        notes.add(this.modelNote);


    }

    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {
        recs.remove(this.graphicalNote);
        notes.remove(this.modelNote);
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(true);
        }
    }
}

