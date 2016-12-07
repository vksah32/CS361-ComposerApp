package proj9ZhouRinkerSahChistolini.Controllers.Actions;

import proj9ZhouRinkerSahChistolini.Controllers.CompositionPanelController;
import proj9ZhouRinkerSahChistolini.Views.NoteRectangle;
import proj9ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action to keep track of changing instruments in selectable rectangles
 */
public class ChangeInstrumentAction implements Actionable{

    /** Notes altered */
    private List<NoteRectangle> graphicalNotes;
    /** Composition Controller */
    private CompositionPanelController compController;
    /** Instrument states */
    private int[] beforeInstrument;
    private String afterInstrument;

    /**
     * @param controller the Composition Controller
     * @param notes to be changed
     * @param after string of instrument to be changed too
     */
    public ChangeInstrumentAction(  CompositionPanelController controller,
                                    List<SelectableRectangle> notes,
                                    String after){

        this.compController = controller;
        this.graphicalNotes = new ArrayList();
        for (SelectableRectangle note : notes){
            if (note instanceof NoteRectangle){
                graphicalNotes.add((NoteRectangle) note);
            }
        }
        this.afterInstrument = after;
        this.beforeInstrument = new int[graphicalNotes.size()];
        for (int i=0; i<graphicalNotes.size();i++){
            this.beforeInstrument[i] = this.graphicalNotes.get(i).getInstrument();
        }
    }

    /**
     * re-deleted notes from the compsition and composition panel
     */
    @Override
    public void reDoIt() {
        graphicalNotes.forEach(n-> {
            n.setInstrument(
                    compController.getInstrumentPanelController().getInstruments()
                            .stream()
                            .filter(
                                    i -> i.getName().equals(afterInstrument)
                            ).collect(Collectors.toList()).get(0).getValue()
            );
        });
    }

    /**
     * re-add notes to panel and composition
     */
    @Override
    public void unDoIt() {
        for (int i=0; i<graphicalNotes.size(); i++){
            graphicalNotes.get(i).setInstrument(beforeInstrument[i]);
        }
    }
}
