package topicpane;
import java.io.*;
import java.util.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import reader.ReadText;
import topicpane.animation.CarBean;
public class ProjectileMotion extends Pane implements ProjectileMotionInterface {
	private HBox inputBox = new HBox();
	private BorderPane mainLayout = new BorderPane();
	private HBox spacer = new HBox();
	private VBox chartBox = new VBox();
	private VBox helpBox = new VBox();
	private Label initialSpeedXLbl = new Label("Initial Speed in X: ");
	private TextField initialSpeedX = new TextField("50");
	private Label initialSpeedYLbl = new Label("Initial Speed in Y:");
	private TextField initialSpeedY = new TextField("50");
	private Button start = new Button("Start Animation");
	private Button reset = new Button("Reset");
	private Button pause = new Button("Pause");
	private Button help = new Button("Help");
	private final NumberAxis xAxisPositionX = new NumberAxis();
	private final NumberAxis yAxisPositionX = new NumberAxis();
	private final NumberAxis xAxisPositionY = new NumberAxis();
	private final NumberAxis yAxisPositionY = new NumberAxis();
	private final NumberAxis xAxisVelocityY = new NumberAxis();
	private final NumberAxis yAxisVelocityY = new NumberAxis();
	private LineChart<Number, Number> positionXChart = new LineChart<Number, Number>(
			xAxisPositionX, yAxisPositionX);
	private LineChart<Number, Number> positionYChart = new LineChart<Number, Number>(
			xAxisPositionY, yAxisPositionY);
	private LineChart<Number, Number> velocityYChart = new LineChart<Number, Number>(
			xAxisVelocityY, yAxisVelocityY);
	private XYChart.Series<Number, Number> positionXSeries;
	private XYChart.Series<Number, Number> positionYSeries;
	private XYChart.Series<Number, Number> velocityYSeries;
	private int centerX = initialX, centerY = initialY;
	private double x, y;
	private final Circle dot = new Circle(centerX, centerY, 7);
	private final TimeCounter counter = new TimeCounter();
	private CarBean carBean = new CarBean();
	private double initialTime;
	private double initialVelocityX;
	private double initialVelocityY;
	private int frame = 55;
	private Label instructionComp = new Label();
	public ProjectileMotion() {
		mainLayout.setTop(new Text("Projectile Motion"));
		this.getChildren().add(mainLayout);
		inputBox.getChildren().addAll(initialSpeedXLbl, initialSpeedX,
				initialSpeedYLbl, initialSpeedY, start, pause, reset, help);
		inputBox.setSpacing(10);
		inputBox.setAlignment(Pos.BASELINE_LEFT);
		dot.setFill(Color.BLUE);
		ReadText readText = new ReadText();
		instructionComp.setText(readText.getInstruction("src/topicpane/projectileMotionHelp.txt"));
		
		helpBox.setAlignment(Pos.BOTTOM_CENTER);
		helpBox.setPadding(new Insets(0, 0, 100, 0));
		chartBox.getChildren().addAll(positionXChart, positionYChart,
				velocityYChart);
		mainLayout.setBottom(inputBox);
		mainLayout.getChildren().add(dot);
		mainLayout.setLeft(chartBox);
		mainLayout.setRight(helpBox);
		spacer.setPadding(new Insets(0, 200, 0, 0));
		mainLayout.setCenter(spacer);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
				new EventHandler() {
					@Override
					public void handle(Event event) {
						refreshScene();
					}
				}), new KeyFrame(Duration.millis(1)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		positionXSeries = new XYChart.Series<Number, Number>();
		positionXSeries.setName("Position in X");
		positionYSeries = new XYChart.Series<Number, Number>();
		positionYSeries.setName("Position in Y");
		velocityYSeries = new XYChart.Series<Number, Number>();
		velocityYSeries.setName("Velocity in Y");
		positionXChart.setLegendVisible(false);
		positionXChart.setCreateSymbols(false);
		positionXChart.getData().add(positionXSeries);
		positionXChart.setPrefHeight(300);
		positionXChart.setTitle("Position in X of the ball");
		positionXChart.setAnimated(false);
		positionYChart.setLegendVisible(false);
		positionYChart.setCreateSymbols(false);
		positionYChart.getData().add(positionYSeries);
		positionYChart.setPrefHeight(300);
		positionYChart.setTitle("Position in Y of the ball");
		positionYChart.setAnimated(false);
		velocityYChart.setLegendVisible(false);
		velocityYChart.setCreateSymbols(false);
		velocityYChart.getData().add(velocityYSeries);
		velocityYChart.setPrefHeight(300);
		velocityYChart.setTitle("Velocity in Y of the ball");
		velocityYChart.setAnimated(false);
		yAxisPositionX.setLabel("Position X");
		xAxisPositionX.setLabel("Time (s)");
		yAxisPositionY.setLabel("Position Y");
		xAxisPositionY.setLabel("Time (s)");
		yAxisVelocityY.setLabel("Velocity Y");
		xAxisVelocityY.setLabel("Time (s)");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				positionXSeries.getData().clear();
				positionYSeries.getData().clear();
				velocityYSeries.getData().clear();
				
				carBean.setX(initialX);
				carBean.setY(initialY);
				x = carBean.getX();
				y = carBean.getY();
				carBean.setSpeedX(Double.valueOf(initialSpeedX.getText()));
				carBean.setSpeedY(Double.valueOf(initialSpeedY.getText()));
				carBean.setAccelerationY(9.81);
				initialVelocityX = carBean.getSpeedX();
				initialVelocityY = carBean.getSpeedY();
				initialTime = (int) System.currentTimeMillis();
				counter.reset();
				timeline.play();
			}
		});
		help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (helpBox.getChildren().isEmpty()) {
					helpBox.getChildren().add(instructionComp);
					help.setText("Close Help");
				} else {
					helpBox.getChildren().clear();
					help.setText("Help");
				}
			}
		});
		pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {}
		});
		reset.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				initialTime = 0;
				gotoxy(initialX, initialY);
			}
		});
	}
	private void refreshScene() {
		frame++;
		int b = 500;
		double time = (int) System.currentTimeMillis() - initialTime;
		double velocityY = (initialVelocityY - ((double) carBean.getAccelerationY() * (time / b)));
		if (y <= initialY) {
			carBean.setSpeedY(velocityY);
			carBean.setX((double) (centerX + (initialVelocityX * (time / b))));
			carBean.setY((double) (centerY - (carBean.getSpeedY() * (time / b) + 0.5
					* carBean.getAccelerationY() * Math.pow(time / b, 2))));
			System.out.println(carBean.getSpeedY());
			x = (carBean.getX());
			y = (carBean.getY());
			gotoxy(x, y);
			if (frame >= 50) {
				frame = 0;
				positionXSeries.getData().add(
						new XYChart.Data<Number, Number>(time / b, (x - centerX)));
				positionYSeries.getData().add(
						new XYChart.Data<Number, Number>(time / b, -(y - centerY)));
				velocityYSeries.getData().add(
						new XYChart.Data<Number, Number>(time / b, carBean.getSpeedY()));
			}
		}
	}
	private void gotoxy(double x, double y) {
		dot.setCenterX(x);
		dot.setCenterY(y);
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
