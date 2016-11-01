package proj5ZhouRinkerSahChistolini.Controllers;

/**
 * Preforms an action on the views and models
 * to redo and undo thier states
 */
public interface Actionable {

    /**
     * Preforms the original action
     */
    public void reDoIt();


    /**
     * Undoes the origional action
     */
    public void unDoIt();

}

