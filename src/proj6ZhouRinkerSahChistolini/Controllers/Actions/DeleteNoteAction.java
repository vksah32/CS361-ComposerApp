package proj6ZhouRinkerSahChistolini.Controllers.Actions;

import javafx.scene.Node;
import proj6ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj6ZhouRinkerSahChistolini.Models.Note;
import proj6ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles when notes are deleted for undoing and redoing
 */
public class DeleteNoteAction implements Actionable{

    /** deleted notes from Composition Panel */
    private Collection<SelectableRectangle> graphicalNote;
    /** deleted model notes from Compostion */
    private Collection<Note> modelNotes;
    /** reference to all notes in Composition */
    private Collection<Note> allNotesOnComposition;
    /** reference to all rectangel on Composition Panel */
    private Collection<Node> recs;

    /**
     * Initilize DeleteNoteAction when notes are deleted from panel
     *
     * @param graphicalNotes deleted note from panel
     * @param modelNotes deleted notes from composition panel
     * @param comp reference to the composition panel controller
     *
     */
    public DeleteNoteAction(Collection<SelectableRectangle> graphicalNotes,
                            Collection<Note> modelNotes,
                            CompositionPanelController comp ){

        this.graphicalNote = graphicalNotes;
        this.modelNotes = modelNotes;
        this.allNotesOnComposition = comp.getNotesfromComposition();
        this.recs = comp.getCompositionPane().getChildren();

    }

    /**
     * re-deleted notes from the compsition and composition panel
     */
    @Override
    public void reDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            recs.remove(rec);
        }

        for(Note note : modelNotes){
            allNotesOnComposition.remove(note);
        }

    }

    /**
     * re-add notes to panel and composition
     */
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
