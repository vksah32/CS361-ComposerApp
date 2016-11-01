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
    private Collection<Note> allNotesOnComposition;
    private Collection<Node> recs;


    public DeleteNoteAction(Collection<SelectableRectangle> graphicalNotes, Collection<Note> modelNotes, CompositionPanelController comp ){

        this.graphicalNote = graphicalNotes;
        this.modelNotes = modelNotes;
        this.allNotesOnComposition = comp.getNotesfromComposition();
        this.recs = comp.getCompositionPane().getChildren();

    }

    @Override
    public void reDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            recs.remove(rec);
        }

        for(Note note : modelNotes){
            allNotesOnComposition.remove(note);
        }

    }

    @Override
    public void unDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            recs.add(rec);
        }

        for(Note note : modelNotes){
            allNotesOnComposition.add(note);
        }
    }
}
