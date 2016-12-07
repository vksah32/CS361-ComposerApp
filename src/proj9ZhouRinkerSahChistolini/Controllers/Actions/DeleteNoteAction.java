package proj9ZhouRinkerSahChistolini.Controllers.Actions;

import javafx.scene.Node;
import proj9ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj9ZhouRinkerSahChistolini.Models.Playable;
import proj9ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles when notes are deleted for undoing and redoing
 */
public class DeleteNoteAction implements Actionable{

    /** deleted notes from Composition Panel */
    private Collection<SelectableRectangle> graphicalNote;
    /** deleted model notes from Compostion */
    private Collection<Playable> modelNotes;
    /** reference to all notes in Composition */
    private Collection<Playable> allNotesOnComposition;
    /** reference to all rectangel on Composition Panel */
    private Collection<Node> recs;
    private CompositionPanelController compController;
    /**
     * Initilize DeleteNoteAction when notes are deleted from panel
     *
     * @param graphicalNotes deleted note from panel
     * @param modelNotes deleted notes from composition panel
     * @param comp reference to the composition panel controller
     *
     */
    public DeleteNoteAction(Collection<SelectableRectangle> graphicalNotes,
                            Collection<Playable> modelNotes,
                            CompositionPanelController comp ){

        this.graphicalNote = graphicalNotes;
        this.modelNotes = modelNotes;
        this.allNotesOnComposition = comp.getNotesfromComposition();
        this.recs = comp.getCompositionPane().getChildren();
        this.compController = comp;

    }

    /**
     * re-deleted notes from the compsition and composition panel
     */
    @Override
    public void reDoIt() {
        compController.deleteSelected(graphicalNote);

        for(Playable note : modelNotes){
            allNotesOnComposition.remove(note);
        }
    }

    /**
     * re-add notes to panel and composition
     */
    @Override
    public void unDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            compController.addRectangle(rec, true);
            if (rec instanceof NoteRectangle){
                recs.add(((NoteRectangle) rec).getTransparency());
            }
        }

        for(Playable note : modelNotes){
            allNotesOnComposition.add(note);
        }
    }
}
