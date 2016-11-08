package proj6ZhouRinkerSahChistolini.Views;

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
     * The constructor of the GroupRectangle
     * Initializes the group based on the input hash set of
     * rectangles.
     *
     * @param selection Hashset of selected rectangles to group
     */
    public GroupRectangle(Collection<SelectableRectangle> selection) {
        Rectangle left = selection.stream()
                                  .min(
                                      Comparator.comparing(Rectangle::getX)
                                  ).get();
        Rectangle right = selection.stream()
                                   .max(
                                       Comparator.comparing(
                                           rec -> rec.getX()+rec.getWidth())
                                   ).get();
        Rectangle top = selection.stream().min(Comparator.comparing(Rectangle::getY)).get();
        Rectangle bot = selection.stream().max(Comparator.comparing(Rectangle::getY)).get();
        this.initialWidth = right.getX() + right.getWidth() - left.getX();
        this.setX(left.getX());
        this.setY(top.getY());
        this.setWidth(this.initialWidth);
        this.setHeight(bot.getY() + bot.getHeight() - top.getY());

        this.setDirectChildren(selection);
        this.bindChildren();
        this.getStyleClass().add("group-note");
    }

    /**
     * Sets the Group's direct children as the input collection
     *
     * @param children a collection of rectangles which are to
     * be the primary members of the group
     */
    public void setDirectChildren(Collection<SelectableRectangle> children){
        this.children = new HashSet<>();
        for (SelectableRectangle rect : children) {
            if(!rect.SelectedProperty().isBound()) {
                this.children.add(rect);
            }
         }
    }

    /**
     * Binds the selected direct children to this rectangle
     */
    public void bindChildren() {
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

    /**
     * Unbinds all of the children within the group.
     */
    public void unbindChildren() {
        for (SelectableRectangle rect : this.children) {
            rect.xProperty().unbind();
            rect.yProperty().unbind();
            rect.widthProperty().unbind();
        }
    }

    /**
     * sets the selection of the rectangle
     * @param selected a boolean representing whether the group is to be
     * selected or deselected
     */
    public void setSelected(boolean selected) {
        if(selected){
            this.getStyleClass().removeAll("group-note");
            this.getStyleClass().add("selected-group-note");
            for (SelectableRectangle rec: this.children){
                rec.setSelected(true);
            }
        }
        else{
            this.getStyleClass().removeAll("selected-group-note");
            this.getStyleClass().add("group-note");
            for (SelectableRectangle rec: this.children){
                rec.setSelected(false);
            }
        }
        this.selected.set(selected);
    }

    /**
     * returns this node's children
     * @returns this.children this class' children field
     */
    public HashSet<SelectableRectangle> getChildren() { return this.children; }
}

