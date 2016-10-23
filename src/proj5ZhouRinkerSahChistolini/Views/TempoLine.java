package proj5ZhouRinkerSahChistolini.Views;

import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;


public class TempoLine extends Line {
    private TranslateTransition tempoAnimation = new TranslateTransition();

    /**
     * Initializes the tempoAnimation object with the default
     * values it needs
     * Provides the animation with the tempoLine which it moves
     * makes sure the animation is linear
     * and sets our onFinished event
     */
    public TempoLine() {
        this.tempoAnimation.setNode(this);
        this.tempoAnimation.setInterpolator(Interpolator.LINEAR); // Don't ease
        this.tempoAnimation.setOnFinished(event -> this.setVisible(false));
    }

    /**
     * Updates the tempoLine's stop location based on the input double
     *
     * @param stopTime this is the stop location (e.g time) which is the
     * location of the right edge of the final note to be played
     */
    public void updateTempoLine(double stopTime) {
        this.tempoAnimation.stop();
        this.setTranslateX(0);
        this.tempoAnimation.setDuration(new Duration(stopTime*10));
        this.tempoAnimation.setToX(stopTime);
        this.setVisible(true);
    }

    /**
     * Starts the animation of the tempoLine
     */
    public void playAnimation() {
        this.tempoAnimation.play();
    }

    /**
     * Stops the visual animation of the tempoLine
     */
    public void stopAnimation() {
        this.tempoAnimation.stop();
        this.setVisible(false);
    }
}
