package topicpane.animation;
import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
public class DopplerEffectAnimation extends StackPane {
	public final double radius = 25;
	public double xTranslationLis = 0;
	public double xTranslationEm = 350 - 2 * radius;
	public double xTranslationFinalLis = 0;
	public double xTranslationFinalEm = 0;
	public final double yTranslation = 10;
	private Rectangle listener = new Rectangle(0, 0, 50, 50);
	private Circle emitter = new Circle(radius);
	private TranslateTransition animationListener = new TranslateTransition(
			Duration.millis(1000), listener);;
	private TranslateTransition animationEmitter = new TranslateTransition(
			Duration.millis(1000), emitter);
	private double currentStopTime = 0;
	private StackPane root = new StackPane();
	public DopplerEffectAnimation() {
		root.getChildren().addAll(listener, emitter);
		this.getChildren().addAll(root);
	}
	public void setIniLis(double val) {
		xTranslationLis = val;
	}
	public void setIniEm(double val) {
		xTranslationEm = val;
	}
	public void setFinLis(double val) {
		xTranslationFinalLis = val;
	}
	public void setFinEm(double val) {
		xTranslationFinalEm = val;
	}
	public void setShapes(Color colListener, Color colEmitter) {
		listener.setFill(colListener);
		listener.setTranslateX(xTranslationLis);
		listener.setTranslateY(yTranslation);
		emitter.setFill(colEmitter);
		emitter.setTranslateX(xTranslationEm);
		emitter.setTranslateY(yTranslation);
	}
	public void setCompAnim() {
		animationListener.setCycleCount(1);
		animationListener.setDuration(Duration.millis(1000));
		animationListener.setAutoReverse(false);
		animationListener.setFromX(xTranslationLis);
		animationListener.setToX(xTranslationFinalLis);
		animationEmitter.setCycleCount(1);
		animationEmitter.setDuration(Duration.millis(1000));
		animationEmitter.setAutoReverse(false);
		animationEmitter.setFromX(xTranslationEm);
		animationEmitter.setToX(xTranslationFinalEm);
	}
	public void play() {
		if (currentStopTime > 0) {
			animationListener.playFrom(Duration.millis(currentStopTime));
			animationEmitter.playFrom(Duration.millis(currentStopTime));
		} else {
			animationListener.play();
			animationEmitter.play();
		}
	}
	public void stop() {
		animationListener.stop();
		animationEmitter.stop();
	}
	public void pause() {
		animationListener.pause();
		currentStopTime = animationListener.getCurrentTime().toMillis();
		animationEmitter.pause();
	}
	public double getListenerSpeed() {
		return animationListener.getCurrentRate();
	}
	public double getEmitterSpeed() {
		return animationEmitter.getCurrentRate();
	}
	public TranslateTransition getEmitter() {
		return animationEmitter;
	}
	public TranslateTransition getListener() {
		return animationListener;
	}
}
