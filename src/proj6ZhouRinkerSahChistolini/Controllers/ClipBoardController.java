package proj6ZhouRinkerSahChistolini.Controllers;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;

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

    public String getClipboardContent() throws IOException, UnsupportedFlavorException  {

            String result = (String) this.board.getData(DataFlavor.stringFlavor);
            return result;
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
