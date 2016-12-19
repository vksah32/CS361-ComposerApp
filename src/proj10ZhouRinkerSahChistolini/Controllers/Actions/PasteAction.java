package proj10ZhouRinkerSahChistolini.Controllers.Actions;

import javafx.scene.Node;
import proj10ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj10ZhouRinkerSahChistolini.Models.Playable;
import proj10ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj10ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.Collection;

/**
 * handles when notes are deleted for undoing and redoing
 */
public class PasteAction implements Actionable{

    /** deleted notes from Composition Panel */
    private Collection<SelectableRectangle> graphicalNote;
    /** deleted model notes from Compostion */
    private Collection<Playable> modelNotes;
    /** reference to all notes in Composition */
    private Collection<Playable> allNotesOnComposition;
    /** reference to all rectangel on Composition Panel */
    private Collection<Node> recs;

    /**
     * Initilize DeleteNoteAction when notes are deleted from panel
     *
     * @param graphicalNotes rectangle notes added to panel
     * @param modelNotes model notes from composition panel
     * @param comp reference to the composition panel controller
     *
     */
    public PasteAction(Collection<SelectableRectangle> graphicalNotes, Collection<Playable> modelNotes, CompositionPanelController comp ){

        this.graphicalNote = graphicalNotes;
        this.modelNotes = modelNotes;
        this.allNotesOnComposition = comp.getNotesfromComposition();
        this.recs = comp.getCompositionPane().getChildren();

    }

    /**
     * re-deleted notes from the compsition and composition panel
     */
    @Override
    public void unDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            recs.remove(rec);
            if (rec instanceof NoteRectangle)
            recs.remove(((NoteRectangle) rec).getTransparency());
        }

        for(Playable note : modelNotes){
            allNotesOnComposition.remove(note);
        }

    }

    /**
     * re-add notes to panel and composition
     */
    @Override
    public void reDoIt() {
        for(SelectableRectangle rec : graphicalNote){
            recs.add(rec);
        }

        for(Playable note : modelNotes){
            allNotesOnComposition.add(note);
        }
    }
}