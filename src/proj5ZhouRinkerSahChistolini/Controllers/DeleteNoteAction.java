package proj5ZhouRinkerSahChistolini.Controllers;

import javafx.scene.Node;
import proj5ZhouRinkerSahChistolini.Models.Note;
import proj5ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj5ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles when notes are deleted for undoing and redoing
 */
public class DeleteNoteAction implements Actionable{


    private Collection<SelectableRectangle> graphicalNote;

    private Collection<Note> modelNotes;

    public DeleteNoteAction(Collection<SelectableRectangle> graphicalNotes, Collection<Note> modelNotes ){

        this.graphicalNote = graphicalNotes;
        this.modelNotes = modelNotes;

    }

    @Override
    public void reDoIt(Collection<Node> recs, Collection<Note> notes) {
        for(SelectableRectangle rec : graphicalNote){
            recs.remove(rec);
        }

        for(Note note : modelNotes){
            notes.remove(note);
        }

    }

    @Override
    public void unDoIt(Collection<Node> recs, Collection<Note> notes) {
        for(SelectableRectangle rec : graphicalNote){
            recs.add(rec);
        }

        for(Note note : modelNotes){
            notes.add(note);
        }
    }
}
