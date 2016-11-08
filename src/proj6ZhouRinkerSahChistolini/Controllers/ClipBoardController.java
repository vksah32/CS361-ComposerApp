package proj6ZhouRinkerSahChistolini.Controllers;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;

/**
 *
 * Manages the cut or copied data to be added
 * to or from cleared the clip board.
 *
 */
public class ClipBoardController {

    private Clipboard board;


    /**
     *
     */
    public ClipBoardController(){

        this.board =  Toolkit.getDefaultToolkit().getSystemClipboard();

    }




    public void clear(){


    }

    /**
     * adds new strings of data to the clip board
     *
     * @param cutCopyData string data to be added to the clipboard
     *
     */
    public void addStringContent(String cutCopyData){
        Transferable transferable = new StringSelection(cutCopyData);
        board.setContents(transferable, null);

    }
}
