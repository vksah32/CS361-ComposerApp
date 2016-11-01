package proj5ZhouRinkerSahChistolini.Controllers;

import java.util.Collection;
import proj5ZhouRinkerSahChistolini.Models.Note;
import javafx.scene.Node;

/**
 * Preforms an action on the views and models
 * to redo and undo their states
 */
public interface Actionable {


    /**
     * Preforms the original action
     */
    public void reDoIt();


    /**
     * Undoes the original action
     */
    public void unDoIt();
    
    
    
    
}

