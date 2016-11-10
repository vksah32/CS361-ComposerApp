package proj7ZhouRinkerSahChistolini.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import proj7ZhouRinkerSahChistolini.Views.GroupRectangle;
import proj7ZhouRinkerSahChistolini.Views.SelectableRectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller to assist in creating custom bindings
 * between controllers and view/model
 */
public class BindingController {
    /** The application's main controller*/
    private Controller mainController;

    /** The application's composition panel controller*/
    private CompositionPanelController compositionController;

    public BindingController(
        Controller mainController,
        CompositionPanelController compositionPanelController
    ){
        this.mainController = mainController;
        this.compositionController = compositionPanelController;
    }

    /**
     * returns the children property of the composition panel
     * @return ListProperty childrenProperty
     */
    public ListProperty<Node> getChildrenProperty() {
        //Bind obp to children and list to obp
        ObjectProperty<ObservableList<Node>> obp = new SimpleObjectProperty();
        obp.setValue(this.compositionController.getCompositionPane().getChildren());
        ListProperty<Node> childrenProperty = new SimpleListProperty();
        childrenProperty.bind(obp);
        return childrenProperty;
    }

    /**
     * returns the areNotesSelected property which is a custom binding
     * representing whether or not any note is selected
     * @return this.areNotesSelected
     */
    public BooleanBinding getAreNotesSelectedBinding() {
        BooleanBinding selectedNotesBinding = Bindings.createBooleanBinding(() ->
            this.compositionController.getSelectedRectangles().size() == 0,
            this.compositionController.getActionController().getUndoActionsProperty()
        );
        return selectedNotesBinding;
    }

    /**
     * returns the multipleSelected BooleanBinding which represents
     * whether or not there are multiple elements selected
     * @return multipleSelectedBinding the BooleanBinding
     */
    public BooleanBinding getMultipleSelectedBinding() {
        BooleanBinding multipleSelectedBinding = Bindings.createBooleanBinding(() ->
            this.getUnboundSelected().size() < 2,
            this.compositionController.getActionController().getUndoActionsProperty()
        );
        return multipleSelectedBinding; }

    /**
     * returns the groupsSelected Binding customly created
     * for determining whether or not a group is selected
     * @return groupsSelectedBinding
     */
    public BooleanBinding getGroupSelectedBinding() {
        BooleanBinding groupSelectedBinding = Bindings.createBooleanBinding(() ->
            this.getSelectedGroupRectangles().size() < 1,
            this.compositionController.getActionController().getUndoActionsProperty()
        );
        return groupSelectedBinding;
    }

    /**
     * Binding if the redo observable list is empty
     * @returns the redoEmptySize Binding
     */
    public BooleanBinding getRedoEmptyBinding() {
        return this.compositionController.getActionController().isRedoEmpty();
    }
    /**
     * Binding if the undo observable list is empty
     * @returns the undoEmptySize Binding
     */
    public BooleanBinding getUndoEmptyBinding() {
        return this.compositionController.getActionController().isUndoEmpty();
    }

    //helper methods
    /**
     * Returns a collection of selected group rectangles
     * @return a collection
     */
    public Collection<GroupRectangle> getSelectedGroupRectangles(){
        List<GroupRectangle> selectedList = new ArrayList<>();
        for(SelectableRectangle rectangle : this.compositionController.getRectangles()){
            if(rectangle instanceof GroupRectangle){
                selectedList.add((GroupRectangle) rectangle);
            }
        }
        return selectedList;
    }

    /**
     * returns a Collection of the selected rectangles that have
     * not been bound
     * @return unboundList a Collection of SelectableRectangles
     */
    public Collection<SelectableRectangle> getUnboundSelected(){
        ArrayList<SelectableRectangle> unboundList = new ArrayList<>();
        for (SelectableRectangle rec :this.compositionController.getSelectedRectangles()){
            if (!rec.xProperty().isBound()){
                unboundList.add(rec);
            }
        }
        return unboundList;
    }
}
