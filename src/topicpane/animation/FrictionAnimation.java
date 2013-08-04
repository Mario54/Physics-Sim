package topicpane.animation;
import java.util.Date;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import topicpane.Friction;
public class FrictionAnimation extends Pane implements FrictionInterface {
	private Friction friction;
	private Group graphics = new Group();
	private int frame = 60;
	private double initialTime;
	private double initialVelocity;
	private CarBean carBean = new CarBean();
	private final TimeCounter counter = new TimeCounter();
	private double x = initialX;
	private double y = initialY;
	private Rectangle object = new Rectangle(50, 50);
	private double timePaused = 0;
	private double time;
	private boolean paused;
	public FrictionAnimation(Friction frictionClass) {
		friction = frictionClass;
		this.getChildren().add(graphics);
		object.setX(x);
		object.setY(y);
		object.setFill(Color.BLUE);
		addComponents();
	}
	public void addComponents() {
		graphics.getChildren().addAll(object);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void startAnimation(double velocityX, double friction) {
		carBean.setX(x);
		carBean.setSpeedX(velocityX);
		carBean.setFriction(friction);
		carBean.setAccelerationX(carBean.getFriction() * 9.8);
		initialVelocity = carBean.getSpeedX();
		initialTime = (int) System.currentTimeMillis();
		counter.reset();
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new EventHandler() {
					@Override
					public void handle(Event event) {
						if (!paused)
							refreshScene();
					}
				}), new KeyFrame(Duration.millis(3)));
		timeline.play();
	}
	private void refreshScene() {
		time = (int) System.currentTimeMillis() - initialTime;
		double currentSpeed = (initialVelocity - ((double) carBean
				.getAccelerationX() * (time / ANIMATION_SPEED)));
		if (currentSpeed > 0) {
			frame++;
			carBean.setSpeedX(currentSpeed);
			carBean
					.setX((double) (carBean.getSpeedX() * (time / ANIMATION_SPEED) + 0.5
							* carBean.getAccelerationX()
							* Math.pow((time / ANIMATION_SPEED), 2)));
			x = carBean.getX();
			gotoxy(x, y);
			if (frame >= 30) {
				frame = 0;
				friction.addData(time / ANIMATION_SPEED, x, carBean.getSpeedX());
			}
		} else {
			friction.animationEnded();
		}
	}
	private void gotoxy(double x, double y) {
		object.setX(x);
		object.setY(y);
	}
	public void pause(boolean pause) {
		paused = pause;
		System.out.println(paused);
		if (!paused) {
			initialTime += System.currentTimeMillis() - timePaused;
		} else {
			timePaused = System.currentTimeMillis();
		}
	}
	public boolean isPaused() {
		return paused;
	}
	public void reset() {
		initialTime = 0;
		paused = false;
		gotoxy(initialX, initialY);
	}
	class TimeCounter {
		private long start = new Date().getTime();
		void reset() {
			start = new Date().getTime();
		}
		long elapsed() {
			return new Date().getTime() - start;
		}
	}
}
