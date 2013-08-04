package topicpane.animation;
import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
public class SHMAnimation extends Pane {
	final private Rectangle weight = new Rectangle(0, 0, 50, 50);
	final Timeline timeline = new Timeline();
	public SHMAnimation() {
		weight.setFill(Color.CRIMSON);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);
		this.getChildren().add(weight);
	}
	public void setInterpolator(double range, Duration duration) {
		if (!(timeline.getKeyFrames().isEmpty()))
			timeline.getKeyFrames().clear();
		timeline.getKeyFrames().add(
				new KeyFrame(duration, new KeyValue(weight.yProperty(), range,
						new CustomInterpolator())));
	}
	public void clearTimeline() {
		if (!(timeline.getKeyFrames().isEmpty())) {
			timeline.getKeyFrames().clear();
		}
	}
	public Timeline getTimeline() {
		return timeline;
	}
	public Rectangle getRectangle() {
		return weight;
	}
	public void startTimeline() {
		timeline.play();
	}
	public void stopTimeline() {
		timeline.pause();
	}
	public void pause() {
		timeline.pause();
	}
	public double getMaxTime() {
		return timeline.getCycleDuration().toMillis();
	}
	boolean down = true, up = false;
	class CustomInterpolator extends Interpolator {
		protected double curve(double t) {
			if (t == 1) {
				down = false;
				up = true;
			} else if (t == 0) {
				down = true;
				up = false;
			}
			if (down)
				t = -t;
			return (-Math.cos(t * Math.PI) + 1) / 2;
		}
	}
}