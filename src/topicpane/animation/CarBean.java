package topicpane.animation;
import javafx.beans.property.*;
public class CarBean {
	private DoubleProperty x = new SimpleDoubleProperty();
	private DoubleProperty y = new SimpleDoubleProperty();
	private DoubleProperty speedX = new SimpleDoubleProperty();
	private DoubleProperty speedY = new SimpleDoubleProperty();
	private DoubleProperty accelerationX = new SimpleDoubleProperty();
	private DoubleProperty accelerationY = new SimpleDoubleProperty();
	private DoubleProperty friction = new SimpleDoubleProperty();
	public final double getX() {
		return x.get();
	}
	public final void setX(double value) {
		x.set(value);
	}
	public final DoubleProperty xProperty() {
		return x;
	}
	public final double getY() {
		return y.get();
	}
	public final void setY(double d) {
		y.set(d);
	}
	public final DoubleProperty yProperty() {
		return y;
	}
	public final double getSpeedX() {
		return speedX.get();
	}
	public final void setSpeedX(double value) {
		speedX.set(value);
	}
	public final DoubleProperty speedXProperty() {
		return speedX;
	}
	public final double getSpeedY() {
		return speedY.get();
	}
	public final void setSpeedY(double value) {
		speedY.set(value);
	}
	public final DoubleProperty speedYProperty() {
		return speedY;
	}
	public final double getAccelerationX() {
		return accelerationX.get();
	}
	public final void setAccelerationX(double value) {
		accelerationX.set(value);
	}
	public final DoubleProperty accelerationXProperty() {
		return accelerationX;
	}
	public final double getAccelerationY() {
		return accelerationY.get();
	}
	public final void setAccelerationY(double value) {
		accelerationY.set(value);
	}
	public final DoubleProperty accelerationYProperty() {
		return accelerationY;
	}
	public final double getFriction() {
		return friction.get();
	}
	public final void setFriction(double value) {
		friction.set(value);
	}
	public final DoubleProperty frictionProperty() {
		return friction;
	}
}
