/**
 * File: Composition.java
 * @author Victoria Chistolini
 * @author Tiffany Lam
 * @author Joseph Malionek
 * @author Vivek Sah
 * Class: CS361
 * Project: 4
 * Date: October 11, 2016
 */

package proj8ZhouRinkerSahChistolini.Models;

import java.util.HashSet;
import java.util.Collection;

/**
 * The central logic for creating and playing back a composition.
 */
public class Composition {
    /** The volume at which all the notes will be played */
    public static final int VOLUME = 127;
    /** The track on which this composition will be played */
    public static final int TRACK_INDEX = 0;

    /** The midiplayer on which this composition will be played */
    private MidiPlayer player;

    /** The notes to build the composition */
    private HashSet<Playable> notes;

    /** Creates a new Composition object with a midiplayer which has
     * 60 beats per minute and 100 ticks per beat*/
    public Composition(){
        player = new MidiPlayer(100,60);
        notes = new HashSet<>();
    }
    /**
     * This will add a note to this composition
     * given a set of note parameters into a midi event
     * @param startTick the tick at which this note will be played
     * @param duration how long this note will be played
     * @param pitch the pitch of the note
     * @param instrument the instrument with which this note will be played
     *
     */
    public void addNote(int startTick, int duration, int pitch, Instrument instrument) {
        this.player.addNote(pitch, this.VOLUME, startTick,
                duration, instrument.getChannel(), this.TRACK_INDEX,
                instrument.getValue());
    }

    /**
     * Adds a note to this composition
     * @param note the note which will be added to the composition
     */
    public void addNote(Playable note){
        if (note instanceof Note) {
            Note temp = (Note) note;
            this.addNote(temp.getStartTick(), temp.getDuration(),
                    temp.getPitch(), temp.getInstrument());
        }
        else if(note instanceof Gesture){
            ((Gesture) note).getChildren().forEach(n -> this.addNote(n));
        }
    }

    /**
     * Appends a playable to the note field
     * removes children of gestures from top of collection
     * @param note the note to be added to the field
     */
    public void appendPlayable(Playable note){
        if (note instanceof Gesture){
            this.notes.removeAll(((Gesture) note).getChildren());
        }
        this.notes.add(note);


    }

    /**
     * Removes a note from the notes field
     * @param note the note to be removed
     */
    public void removeNote(Note note){
        this.notes.remove(note);
    }

    /**
     * Constructs a composition from the notes collection
     */
    public void buildSong(){
        for(Playable note: notes){
            this.addNote(note);
        }
    }

    /**
     * plays the current composition
     */
    public void play() {
        this.player.play();
    }

    /**
     * stops the current composition and clears the midi player
     */
    public void stop() {
        this.player.clear();
        this.player.stop();
    }

    /**
     * getter for notes
     * @return
     */
    public HashSet<Playable> getNotes() {
        return notes;
    }



    public Collection<Playable> getSelectedCompositionNotes() {
        HashSet<Playable> selectedNotes = new HashSet<>();

        for (Playable note : notes) {
            if (note.selectedProperty().getValue()) {
                selectedNotes.add(note);
            }
        }

        return selectedNotes;
    }
}
