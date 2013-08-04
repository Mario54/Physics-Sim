package topicpane.graph;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
public class GeneralGraph extends Pane {
	private final NumberAxis xAxis = new NumberAxis();
	private final NumberAxis yAxis = new NumberAxis();
	private final LineChart<Number, Number> chart = new LineChart<Number, Number>(
			xAxis, yAxis);
	private XYChart.Series series = new XYChart.Series();
	private XYChart.Data data = new XYChart.Data<>();
	public GeneralGraph(String xLbl, String yLbl, String title, boolean symbols) {
		this.getChildren().add(setGraph(xLbl, yLbl, title, symbols, false));
	}
	public GeneralGraph(String xLbl, String yLbl, String title, boolean symbols,
			boolean bound) {
		this.getChildren().add(setGraph(xLbl, yLbl, title, symbols, bound));
	}
	public LineChart setGraph(String xLbl, String yLbl, String title,
			boolean symbols, boolean bound) {
		xAxis.setLabel(xLbl);
		xAxis.setAutoRanging(true);
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(5);
		yAxis.setLabel(yLbl);
		yAxis.setAutoRanging(true);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(100);
		chart.setTitle(title);
		chart.setCreateSymbols(false);
		chart.setCreateSymbols(symbols);
		chart.setLegendVisible(false);
		chart.getData().add(series);
		return chart;
	}
	public void addSerieData(Number x, Number y) {
		series.getData().add(data = new XYChart.Data<>(x, y));
	}
}