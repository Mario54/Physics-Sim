package topicpane;
import java.io.*;
import java.util.Scanner;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import reader.ReadText;
import topicpane.animation.RCCircuitAnimation;
public class RCCircuit extends Pane {
	private HBox inputBox = new HBox();
	private BorderPane mainLayout = new BorderPane();
	private HBox chartBox = new HBox();
	private VBox helpBox = new VBox();
	private StackPane animationBox = new StackPane();
	private Label voltageLbl = new Label("Voltage of the battery: ");
	private TextField voltage = new TextField("50");
	private Label resistanceLbl = new Label("Resistance: ");
	private TextField resistance = new TextField("25");
	private Label capacitanceLbl = new Label("Capacitance: ");
	private TextField capacitance = new TextField("1");
	private Button start = new Button("Charge the capacitor");
	private Button pause = new Button(" Pause ");
	private Button reset = new Button("Reset");
	private Button help = new Button("Help");
	private RCCircuitAnimation rcCircuit;
	private final NumberAxis xAxisCurrent = new NumberAxis();
	private final NumberAxis yAxisCurrent = new NumberAxis();
	private final NumberAxis xAxisCharge = new NumberAxis();
	private final NumberAxis yAxisCharge = new NumberAxis();
	private final NumberAxis xAxisVoltage = new NumberAxis();
	private final NumberAxis yAxisVoltage = new NumberAxis();
	private LineChart<Number, Number> currentChart = new LineChart<Number, Number>(
			xAxisCurrent, yAxisCurrent);
	private LineChart<Number, Number> chargeChart = new LineChart<Number, Number>(
			xAxisCharge, yAxisCharge);
	private LineChart<Number, Number> voltageChart = new LineChart<Number, Number>(
			xAxisVoltage, yAxisVoltage);
	private XYChart.Series<Number, Number> currentSeries;
	private XYChart.Series<Number, Number> chargeSeries;
	private XYChart.Series<Number, Number> voltageSeries;
	private Label instructionComp = new Label();
	public RCCircuit() {
		ReadText readText = new ReadText();
		instructionComp.setText(readText.getInstruction("src/topicpane/rcCircuitHelp.txt"));
		
		this.getChildren().add(mainLayout);
		mainLayout.setTop(new Text("RC Circuit"));
		currentChart.setAnimated(false);
		yAxisCurrent.setLabel("Current through the resistor");
		xAxisCurrent.setLabel("Time (s)");
		chargeChart.setAnimated(false);
		yAxisCharge.setLabel("Charge of the capacitor");
		xAxisCharge.setLabel("Time (s)");
		voltageChart.setAnimated(false);
		yAxisVoltage.setLabel("Voltage of the capacitor");
		xAxisVoltage.setLabel("Time (s)");
		currentSeries = new XYChart.Series<Number, Number>();
		currentSeries.setName("Current through the resistor");
		chargeSeries = new XYChart.Series<Number, Number>();
		chargeSeries.setName("Charge of the capacitor");
		voltageSeries = new XYChart.Series<Number, Number>();
		voltageSeries.setName("Voltage of the capacitor");
		currentChart.setCreateSymbols(false);
		currentChart.getData().add(currentSeries);
		currentChart.setPrefHeight(300);
		currentChart.setPrefWidth(400);
		currentChart.setLegendVisible(false);
		chargeChart.setCreateSymbols(false);
		chargeChart.getData().add(chargeSeries);
		chargeChart.setPrefHeight(300);
		chargeChart.setPrefWidth(400);
		chargeChart.setLegendVisible(false);
		voltageChart.setCreateSymbols(false);
		voltageChart.getData().add(voltageSeries);
		voltageChart.setPrefHeight(300);
		voltageChart.setPrefWidth(400);
		voltageChart.setLegendVisible(false);
		chartBox.getChildren().addAll(currentChart, chargeChart, voltageChart);
		rcCircuit = new RCCircuitAnimation(this);
		inputBox.getChildren().addAll(voltageLbl, voltage, resistanceLbl,
				resistance, capacitanceLbl, capacitance, start, pause, reset, help/*
																																					 * ,
																																					 * stop
																																					 */);
		inputBox.setSpacing(20);
		inputBox.setPadding(new Insets(20, 20, 20, 20));
		inputBox.setAlignment(Pos.BASELINE_LEFT);
		helpBox.setPadding(new Insets(20, 20, 20, 20));
		mainLayout.setBottom(inputBox);
		animationBox.getChildren().add(rcCircuit);
		mainLayout.setCenter(animationBox);
		mainLayout.setRight(helpBox);
		mainLayout.setTop(chartBox);
		chartBox.setAlignment(Pos.CENTER);
		inputBox.setAlignment(Pos.CENTER);
		chartBox.setTranslateX(100);
		inputBox.setTranslateX(100);
		pause.setDisable(true);
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				currentSeries.getData().clear();
				chargeSeries.getData().clear();
				voltageSeries.getData().clear();
				if (start.getText().equals("Charge the capacitor")) {
					System.out.println("Charging");
					rcCircuit.startCharging(Double.valueOf(voltage.getText()),
							Double.valueOf(resistance.getText()),
							Double.valueOf(capacitance.getText()));
					start.setText("Discharge the capacitor");
				} else {
					System.out.println("Discharging");
					rcCircuit.startDischarging(Double.valueOf(voltage.getText()),
							Double.valueOf(resistance.getText()),
							Double.valueOf(capacitance.getText()));
					start.setText("Charge the capacitor");
				}
				voltage.setDisable(true);
				resistance.setDisable(true);
				capacitance.setDisable(true);
				pause.setText("Pause");
				pause.setDisable(false);
				start.setDisable(true);
			}
		});
		pause.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!rcCircuit.isPaused()) {
					rcCircuit.pause(true);
					pause.setText("Play");
				} else {
					rcCircuit.pause(false);
					pause.setText(" Pause ");
				}
			}
		});
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				currentSeries.getData().clear();
				chargeSeries.getData().clear();
				voltageSeries.getData().clear();
				rcCircuit.reset();
				pause.setDisable(true);
				start.setDisable(false);
				voltage.setDisable(false);
				resistance.setDisable(false);
				capacitance.setDisable(false);
			}
		});
		help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (helpBox.getChildren().isEmpty()) {
					helpBox.getChildren().add(instructionComp);
					help.setText("Close help");
					pause.setDisable(true);
					start.setDisable(true);
					reset.setDisable(true);
				} else {
					helpBox.getChildren().clear();
					help.setText("Help");
					pause.setDisable(false);
					start.setDisable(false);
					reset.setDisable(false);
				}
			}
		});
	}
	public void plotData(double time, double current, double charge,
			double voltage) {
		currentSeries.getData().add(
				new XYChart.Data<Number, Number>(time / 1000, current));
		chargeSeries.getData().add(
				new XYChart.Data<Number, Number>(time / 1000, charge));
		voltageSeries.getData().add(
				new XYChart.Data<Number, Number>(time / 1000, voltage));
	}
	public void finished() {
		rcCircuit.reset();
		start.setDisable(false);
		pause.setDisable(true);
		voltage.setDisable(false);
		resistance.setDisable(false);
		capacitance.setDisable(false);
	}
}
