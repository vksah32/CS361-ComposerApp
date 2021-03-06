package proj10ZhouRinkerSahChistolini.Controllers.Actions;

import proj10ZhouRinkerSahChistolini.Models.Gesture;
import proj10ZhouRinkerSahChistolini.Models.Note;
import proj10ZhouRinkerSahChistolini.Models.Playable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Action to keep track of changing volumes in notes
 */
public class ChangeVolumeAction implements Actionable{

    /** Notes altered */
    private List<Note> notes;
    /** Volume states */
    private int[] beforeVolume;
    private int afterVolume;
    /**
     * @param notes to be changes
     * @param after volume int value to be changed towards
     */
    public ChangeVolumeAction(  Collection<Playable> notes,
                                int after){

        this.notes = new ArrayList();
        this.addOnlyNotes(notes);
        this.afterVolume = after;
        this.beforeVolume = new int[this.notes.size()];
        for (int i=0; i<this.notes.size();i++){
            this.beforeVolume[i] = this.notes.get(i).getVolume();
        }
    }

    /**
     * reapply volume changes
     */
    @Override
    public void reDoIt() {
        this.notes.forEach(n-> n.setVolume(afterVolume));
    }

    /**
     * unapply volume changes
     */
    @Override
    public void unDoIt() {
        for (int i=0; i<this.notes.size(); i++){
            this.notes.get(i).setVolume(beforeVolume[i]);
        }
    }

    private void addOnlyNotes(Collection<Playable> sounds){
        sounds.forEach(n-> {
            if (n instanceof Gesture){
                addOnlyNotes(((Gesture) n).getChildren());
            }
            else if (n instanceof Note){
                this.notes.add((Note)n);
            }
        });
    }
}
