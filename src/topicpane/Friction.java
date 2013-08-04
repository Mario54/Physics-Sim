package topicpane;
import java.io.*;
import java.util.Scanner;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import reader.ReadText;
import topicpane.animation.FrictionAnimation;
public class Friction extends Pane {
	private HBox inputBox = new HBox();
	private BorderPane mainLayout = new BorderPane();
	private HBox chartBox = new HBox();
	private VBox helpBox = new VBox();
	private StackPane animationBox = new StackPane();
	private Label frictionLbl = new Label("Friction coefficient: ");
	private TextField friction = new TextField("5");
	private Label speedLbl = new Label("Initial Speed:");
	private TextField initialSpeed = new TextField("250");
	private Button start = new Button("Start Animation");
	private Button pause = new Button("Pause");
	private Button reset = new Button("Reset");
	private Button help = new Button("Help");
	private Label instructionComp = new Label();
	private final NumberAxis xAxisVelocity = new NumberAxis();
	private final NumberAxis yAxisVelocity = new NumberAxis();
	private final NumberAxis xAxisPos = new NumberAxis();
	private final NumberAxis yAxisPos = new NumberAxis();
	private LineChart<Number, Number> velocityChart = new LineChart<Number, Number>(
			xAxisVelocity, yAxisVelocity);
	private LineChart<Number, Number> posChart = new LineChart<Number, Number>(
			xAxisPos, yAxisPos);
	private XYChart.Series<Number, Number> velocitySeries;
	private XYChart.Series<Number, Number> posSeries;
	private FrictionAnimation frictionAnimation;
	public Friction() {		
		ReadText readText = new ReadText();
		instructionComp.setText(readText.getInstruction("src/topicpane/frictionHelp.txt"));
		
		pause.setDisable(true);
		reset.setDisable(true);
		frictionAnimation = new FrictionAnimation(this);
		posChart.setAnimated(false);
		velocityChart.setAnimated(false);
		this.getChildren().add(mainLayout);
		inputBox.getChildren().addAll(frictionLbl, friction, speedLbl,
				initialSpeed, start, pause, reset, help);
		inputBox.setSpacing(18);
		inputBox.setPadding(new Insets(10, 10, 10, 10));
		inputBox.setAlignment(Pos.BASELINE_LEFT);
		mainLayout.setRight(helpBox);
		animationBox.getChildren().add(frictionAnimation);
		animationBox.setPadding(new Insets(50, 50, 50, 50));
		mainLayout.setBottom(inputBox);
		chartBox.getChildren().addAll(velocityChart, posChart);
		mainLayout.setCenter(animationBox);
		mainLayout.setTop(chartBox);
		chartBox.setPadding(new Insets(10, 10, 10, 10));
		velocitySeries = new XYChart.Series<Number, Number>();
		velocitySeries.setName("Velocity");
		posSeries = new XYChart.Series<Number, Number>();
		posSeries.setName("Position");
		velocityChart.setTitle("Velocity of the object");
		velocityChart.setCreateSymbols(false);
		velocityChart.getData().add(velocitySeries);
		velocityChart.setLegendVisible(false);
		yAxisVelocity.setLabel("Velocity");
		xAxisVelocity.setLabel("Time (s)");
		posChart.setTitle("Position of the object");
		posChart.setCreateSymbols(false);
		posChart.getData().add(posSeries);
		posChart.setLegendVisible(false);
		yAxisPos.setLabel("Position");
		xAxisPos.setLabel("Time (s)");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				reset.setDisable(false);
				help.setDisable(true);
				start.setDisable(true);
				pause.setDisable(false);
				velocitySeries.getData().clear();
				posSeries.getData().clear();
				frictionAnimation.startAnimation(
						Double.valueOf(initialSpeed.getText()),
						Double.valueOf(friction.getText()));
			}
		});
		pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (frictionAnimation.isPaused()) {
					frictionAnimation.pause(false);
					pause.setText("Pause");
					help.setDisable(true);
				} else {
					frictionAnimation.pause(true);
					pause.setText("Play");
					help.setDisable(false);
				}
			}
		});
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				frictionAnimation.reset();
				start.setDisable(false);
				help.setDisable(false);
				friction.setDisable(false);
				initialSpeed.setDisable(false);
			}
		});
		help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (helpBox.getChildren().isEmpty()) {
					helpBox.getChildren().add(instructionComp);
					help.setText("Close Help");
					disableMainComponents(true);
				} else {
					helpBox.getChildren().clear();
					help.setText("Help");
					disableMainComponents(false);
				}
			}
		});
	}
	public void addData(double time, double pos, double velocity) {
		velocitySeries.getData().add(
				new XYChart.Data<Number, Number>(time / 1000, velocity));
		posSeries.getData().add(new XYChart.Data<Number, Number>(time / 1000, pos));
	}
	public void animationEnded() {
		pause.setDisable(true);
		help.setDisable(false);
	}

	public void disableMainComponents(boolean disable) {
		if (frictionAnimation.isPaused()) {
			pause.setDisable(disable);
			reset.setDisable(disable);
			friction.setDisable(true);
			initialSpeed.setDisable(true);
		} else {
			start.setDisable(disable);
			reset.setDisable(disable);
			friction.setDisable(false);
			initialSpeed.setDisable(false);
		}
	}
}
