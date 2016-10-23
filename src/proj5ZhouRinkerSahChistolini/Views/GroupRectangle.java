package proj5ZhouRinkerSahChistolini.Views;

import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Created by Remis on 10/17/2016.
 */
public class GroupRectangle extends SelectableRectangle{

    /** The direct children under the group*/
    private HashSet<SelectableRectangle> children;

    private double initialWidth;

    /**
     * The constructor of the NoteRectangle
     * @param selection Hashset of selected rectangles to group
     */

    public GroupRectangle(HashSet<SelectableRectangle> selection) {
        Rectangle left = selection.stream().min(Comparator.comparing(Rectangle::getX)).get();
        Rectangle right = selection.stream().max(Comparator.comparing(
                                                    rec -> rec.getX()+rec.getWidth())).get();
        Rectangle top = selection.stream().min(Comparator.comparing(Rectangle::getY)).get();
        Rectangle bot = selection.stream().max(Comparator.comparing(Rectangle::getY)).get();
        this.initialWidth = right.getX() + right.getWidth() - left.getX();
        this.setX(left.getX());
        this.setY(top.getY());
        this.setWidth(this.initialWidth);
        this.setHeight(bot.getY() + bot.getHeight() - top.getY());

        this.getDirectChildren(selection);
        this.bindSelection();
        this.getStyleClass().add("group-note");
    }


    public void getDirectChildren(Collection<SelectableRectangle> children){
        this.children = new HashSet<>();
        for (SelectableRectangle rect : children) {
            if(!rect.isBounded()) {
                this.children.add(rect);
            }
         }
    }

    /**
     * Bind selected direct children to this rectangle
     */
    private void bindSelection() {
        for (SelectableRectangle rect : this.children) {
            rect.xProperty().bind(
                    this.xProperty().add(
                            ((this.widthProperty().divide(this.initialWidth))).multiply(
                                    rect.getX() - this.getX())
                    )
            );
            rect.yProperty().bind(this.yProperty().add(rect.getY() - this.getY()));

            rect.widthProperty().bind(
                    (this.widthProperty().divide(this.initialWidth).multiply(rect.getWidth()))
            );
        }

    }



    public void unbindChildren() {
        for (SelectableRectangle rect : this.getChildren()) {
            rect.xProperty().unbind();
            rect.yProperty().unbind();
            rect.widthProperty().unbind();
            rect.setBounded(false);
        }
    }

    public HashSet<SelectableRectangle> getChildren(){return this.children;}

    /**
     * sets the selection of the rectangle
     * @param selected
     */
    public void setSelected(boolean selected) {
        if(selected){
            this.getStyleClass().removeAll("group-note");
            this.getStyleClass().add("selected-group-note");
            for (SelectableRectangle rec: this.getChildren()){
                rec.setSelected(true);
            }
        }
        else{
            this.getStyleClass().removeAll("selected-group-note");
            this.getStyleClass().add("group-note");
            for (SelectableRectangle rec: this.getChildren()){
                rec.setSelected(false);
            }
        }
        this.selected = selected;
    }
}

