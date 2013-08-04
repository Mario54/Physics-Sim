package topicpane.graph;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
public class SHMGraph extends Pane {
	private final NumberAxis xAxis = new NumberAxis();
	private final NumberAxis yAxis = new NumberAxis();
	private final LineChart<Number, Number> chart = new LineChart<Number, Number>(
			xAxis, yAxis);
	private XYChart.Series series = new XYChart.Series();
	private int serieIndex = 0;
	public SHMGraph() {
		this.getChildren().add(setGraph());
		chart.setCreateSymbols(false);
	}
	public LineChart setGraph() {
		xAxis.setLabel("Period (s)");
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(3000);
		xAxis.setTickUnit(500);
		yAxis.setLabel("Amplitude (m)");
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(-200);
		yAxis.setUpperBound(5);
		chart.setTitle("Simple Harmonic Motion");
		chart.setLegendVisible(false);
		series.setName("SHM data");
		chart.getData().add(series);
		return chart;
	}
	public void clearData() {
		series.getData().clear();
	}
	public void addSerieData(Number x, Number y) {
		if (x.doubleValue() > xAxis.getUpperBound()) {
			xAxis.setUpperBound(x.doubleValue());
			xAxis.setLowerBound(xAxis.getUpperBound() - 3000);
		}
		series.getData().add(new XYChart.Data<>(x, y));
	}
}