package topicpane.animation;
import java.util.ArrayList;
import topicpane.RCCircuit;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
public class RCCircuitAnimation extends Pane implements RCCircuitInterface {
	private Group graphics = new Group();
	private Group balls = new Group();
	private Rectangle capacitorL = new Rectangle(widthCB, heightCB);
	private Rectangle capacitorR = new Rectangle(widthCB, heightCB);
	private Rectangle resistor1 = new Rectangle(widthR, heightR);
	private Rectangle resistor2 = new Rectangle(widthR, heightR);
	private Rectangle batteryPos = new Rectangle(widthCB, heightCB);
	private Rectangle batteryNeg = new Rectangle(widthCB, heightCB / 2);
	private Label currentResistor = new Label();
	private Label chargeCapacitor = new Label();
	private Label voltageCapacitor = new Label();
	private Path chargingCircuit = new Path();
	private Path dischCircuit = new Path();
	private Line chargingLine;
	private Line dischargingLine;
	private Timeline timeline;
	private RCCircuit circuitPane;
	private long initialTime;
	private boolean paused = false;
	private double timePaused;
	private ArrayList<PathTransition> pathTransitions = new ArrayList<PathTransition>();
	@SuppressWarnings("unchecked")
	public RCCircuitAnimation(RCCircuit circuit) {
		circuitPane = circuit;
		this.getChildren().add(graphics);
		this.getChildren().add(balls);
		objectsX.put("c", (double) capacitorX);
		objectsX.put("b", (double) batteryX);
		objectsX.put("r1", (double) resistor1X);
		addComponents();
	}
	public void addComponents() {
		resistor1.setFill(Color.WHITE);
		resistor1.setStroke(Color.BLACK);
		resistor1.setStrokeWidth(5);
		resistor2.setFill(Color.WHITE);
		resistor2.setStroke(Color.BLACK);
		resistor2.setStrokeWidth(5);
		capacitorL.setX((Double) objectsX.get("c") - distCB / 2 - widthCB);
		capacitorL.setY(topR.getY() - heightCB / 2);
		capacitorR.setX((Double) objectsX.get("c") + distCB / 2);
		capacitorR.setY(topR.getY() - heightCB / 2);
		resistor1.setX((Double) objectsX.get("r1") - widthR / 2);
		resistor1.setY(topR.getY() - heightR / 2);
		batteryPos.setX((Double) objectsX.get("b") - distCB / 2 - widthCB);
		batteryPos.setY(botL.getY() - heightCB / 2);
		batteryNeg.setX((Double) objectsX.get("b") + distCB / 2);
		batteryNeg.setY(botL.getY() - heightCB / 4);
		currentResistor.setTranslateX(resistor1X - 45);
		currentResistor.setTranslateY(topR.getY() - 80);
		chargeCapacitor.setTranslateX(capacitorX - distCB - 23);
		chargeCapacitor.setTranslateY(topR.getY() + heightCB / 2 + 20);
		voltageCapacitor.setTranslateX(capacitorX - distCB - 23);
		voltageCapacitor.setTranslateY(topR.getY() - 100);
		graphics.getChildren().addAll(capacitorL, capacitorR, resistor1,
				batteryPos, batteryNeg, currentResistor, chargeCapacitor,
				voltageCapacitor);
		buildLines();
	}
	public void buildLines() {
		Line line = new Line();
		line.setStrokeWidth(5);
		ArrayList<Line> lines = new ArrayList<Line>();
		Point2D midPointR = new Point2D(topR.getX(), botL.getY() / 2 + topR.getY()
				/ 2);
		lines
				.add(new Line(botL.getX(), botL.getY(), batteryPos.getX(), botL.getY()));
		lines.add(new Line(batteryNeg.getX() + strokeWidth, botL.getY(), topR
				.getX(), botL.getY()));
		lines.add(new Line(topR.getX(), botL.getY(), topR.getX(), topR.getY()));
		lines.add(new Line(topR.getX(), midPointR.getY(), botL.getX() + 100,
				midPointR.getY()));
		lines.add(new Line(topR.getX(), topR.getY(), resistor1.getX() + widthR
				+ strokeWidth / 2, topR.getY()));
		lines.add(new Line(resistor1.getX() - strokeWidth / 2, topR.getY(),
				capacitorR.getX() + strokeWidth, topR.getY()));
		lines
				.add(new Line(capacitorL.getX(), topR.getY(), botL.getX(), topR.getY()));
		lines.add(new Line(botL.getX(), botL.getY(), botL.getX(),
				midPointR.getY() + 100));
		lines
				.add(new Line(botL.getX(), topR.getY(), botL.getX(), midPointR.getY()));
		for (Line lineE : lines) {
			lineE.setStrokeWidth(strokeWidth);
		}
		chargingLine = new Line(botL.getX(), botL.getY(), botL.getX(), topR.getY());
		chargingLine.setStrokeWidth(strokeWidth);
		chargingLine.setVisible(false);
		
		dischargingLine = new Line(topR.getX(), midPointR.getY(), botL.getX(),
				midPointR.getY());
		dischargingLine.setStrokeWidth(strokeWidth);
		dischargingLine.setVisible(false);
		graphics.getChildren().addAll(lines);
		graphics.getChildren().addAll(chargingLine, dischargingLine);
		chargingCircuit.getElements().add(
				new MoveTo(capacitorL.getX() + widthCB, topR.getY()));
		chargingCircuit.getElements().add(new LineTo(botL.getX(), topR.getY()));
		chargingCircuit.getElements().add(new LineTo(botL.getX(), botL.getY()));
		chargingCircuit.getElements().add(new LineTo(topR.getX(), botL.getY()));
		chargingCircuit.getElements().add(new LineTo(topR.getX(), topR.getY()));
		chargingCircuit.getElements().add(
				new LineTo(capacitorR.getX() + widthCB / 2, topR.getY()));
		dischCircuit.getElements().add(
				new MoveTo(capacitorR.getX() + widthCB / 2, topR.getY()));
		dischCircuit.getElements().add(new LineTo(topR.getX(), topR.getY()));
		dischCircuit.getElements().add(new LineTo(topR.getX(), midPointR.getY()));
		dischCircuit.getElements().add(new LineTo(botL.getX(), midPointR.getY()));
		dischCircuit.getElements().add(new LineTo(botL.getX(), topR.getY()));
		dischCircuit.getElements().add(
				new LineTo(capacitorL.getX() + widthCB / 2, topR.getY()));
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void startCharging(final Double voltage, final Double resistance,
			final Double capacitance) {
		dischargingLine.setVisible(false);
		chargingLine.setVisible(true);
		initialTime = System.currentTimeMillis();
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new EventHandler() {
					@Override
					public void handle(Event event) {
						double currentTime = ((double) System.currentTimeMillis() - initialTime) / 1000.0;
						double current = voltage / resistance
								* Math.exp(-1 * currentTime / (resistance * capacitance));
						double charge = capacitance * voltage
								* (1 - Math.exp(-currentTime / (resistance * capacitance)));
						chargeCapacitor.setText("Charge = " + df.format(charge));
						voltageCapacitor.setText("Voltage = "
								+ df.format(charge / capacitance));
						currentResistor.setText("Current = " + df.format(current));
						circuitPane.plotData(currentTime, current, charge, charge
								/ capacitance);
						if (charge / capacitance < 0.95 * voltage) {
							prepareBall(chargingCircuit, current);
						} else {
							timeline.stop();
							circuitPane.finished();
						}
					}
				}), new KeyFrame(Duration.millis(100)));
		timeline.play();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startDischarging(final Double voltage, final Double resistance,
			final Double capacitance) {
		final long initialTime = System.currentTimeMillis();
		dischargingLine.setVisible(true);
		chargingLine.setVisible(false);
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new EventHandler() {
					@Override
					public void handle(Event event) {
						double currentTime = ((double) System.currentTimeMillis() - initialTime) / 1000.0;
						double current = voltage / resistance
								* Math.exp(-currentTime / (resistance * capacitance));
						double charge = capacitance * voltage
								* Math.exp(-currentTime / (resistance * capacitance));
						chargeCapacitor.setText("Charge = " + df.format(charge));
						voltageCapacitor.setText("Voltage = "
								+ df.format(charge / capacitance));
						currentResistor.setText("Current = " + df.format(current));
						circuitPane.plotData(currentTime, current, charge, charge
								/ capacitance);
						if (charge / capacitance > 0.05 * voltage) {
							prepareBall(dischCircuit, current);
						} else {
							timeline.stop();
							circuitPane.finished();
						}
					}
				}), new KeyFrame(Duration.millis(100)));
		timeline.play();
	}
	public Circle prepareBall(Path path, double current) {
		final Circle circle;
				
			if (path == dischCircuit) {
				 circle = new Circle(capacitorR.getX() + widthCB, topR.getY(),10);
			}
		
			else {
				 circle = new Circle(capacitorL.getX() + widthCB, topR.getY(),10);
			}
				
		circle.setFill(Color.RED);
		balls.getChildren().add(circle);
		PathTransition pt = new PathTransition();
		pt.setDuration(Duration.millis(10000 / current));
		pt.setPath(path);
		pt.setNode(circle);
		pt.setCycleCount(1);
		pt.play();
		pt.setInterpolator(Interpolator.DISCRETE);
		pt.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				circle.setVisible(false);
			}
		});
		pathTransitions.add(pt);
		return circle;
	}
	public void stop() {
		balls.getChildren().clear();
		timeline.stop();
	}
	public void pause(boolean pause) {
		paused = pause;
		if (paused) {
			timePaused = System.currentTimeMillis();
			timeline.pause();
			for (PathTransition pt : pathTransitions) {
				if (pt.getNode().isVisible()) {
					pt.pause();
				}
			}
		} else {
			initialTime += System.currentTimeMillis() - timePaused;
			timeline.play();
			for (PathTransition pt : pathTransitions) {
				if (pt.getNode().isVisible()) {
					pt.play();
				}
			}
		}
	}
	public boolean isPaused() {
		return paused;
	}
	public void reset() {
		for (PathTransition pt : pathTransitions) {
			if (pt.getNode().isVisible()) {
				pt.getNode().setVisible(false);
				pt = null;
			}
		}
		timeline.stop();
		initialTime = 0;
	}
}
