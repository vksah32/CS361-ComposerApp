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
    private boolean isMetaDown;
    private Collection<Node> recs;
    private Collection<Note> notes;

    private Collection<SelectableRectangle> selected;

    public AddNoteAction(NoteRectangle graphicalNote, Note modelNote, Collection<SelectableRectangle> selected, boolean metaDown, CompositionPanelController comp ){
        this.isMetaDown = metaDown;
        this.selected = selected;

        this.graphicalNote = graphicalNote;
        this.modelNote = modelNote;
        this.recs = comp.getCompositionPane().getChildren();
        this.notes = comp.getNotesfromComposition();

    }

    @Override
    public void reDoIt() {
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(false);
        }
        this.graphicalNote.setSelected(true);
        recs.add(this.graphicalNote);
        notes.add(this.modelNote);
    }

    @Override
    public void unDoIt() {
        recs.remove(this.graphicalNote);
        notes.remove(this.modelNote);
        for (SelectableRectangle  rec : this.selected) {
            rec.setSelected(true);
        }
    }
}

