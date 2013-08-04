package topicpane.graph;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class DopplerEffectGraph extends Pane {

	private final NumberAxis xAxis = new NumberAxis();
	private final NumberAxis yAxis = new NumberAxis();
	private final LineChart<Number, Number> chart = new LineChart<Number, Number>(
			xAxis, yAxis);
	private XYChart.Series series = new XYChart.Series();
	private XYChart.Data data = new XYChart.Data<>();

	public DopplerEffectGraph() {
		this.getChildren().add(setGraph());
	}

	public LineChart setGraph() {
		chart.setLegendVisible(false);
		xAxis.setLabel("Time(s)");
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(2);
		yAxis.setLabel("Frequency (Hz)");
		// yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(5);
		// chart
		chart.setTitle("Doppler Effect Frequency");
		chart.setCreateSymbols(false);
		// chart.getData().setCreateSymbols(false);

		series.setName("DE Data");
		chart.getData().add(series);
		return chart;
	}

	public void addSerieData(Number x, Number y) {
		series.getData().add(data = new XYChart.Data<>(x, y));
	}

	public void removeAllData() {
		series.getData().clear();
	}

}
