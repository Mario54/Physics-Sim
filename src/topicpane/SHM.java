package topicpane;
import reader.ReadText;
import topicpane.animation.*;
import topicpane.graph.*;
import javafx.beans.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
public class SHM extends Pane {
	private Slider slFrequency = new Slider();
	private Slider slPeriod = new Slider();
	private Slider slAmplitude = new Slider();
	private Button btnStart = new Button("Start");
	private Button btnPause = new Button("Pause");
	private Button btnStop = new Button("Stop");
	private Button btnReset = new Button("Reset");
	private Button btnHelp = new Button("Help");
	private Label txtHelp = new Label();
	private VBox mainLayout = new VBox();
	private SHMAnimation animation = new SHMAnimation();
	private SHMGraph graph = new SHMGraph();
	private double period = 1;
	private double amplitude = 1;
	public SHM() {
		setHelpText();
		this.getChildren().add(mainLayout);
		mainLayout.getChildren().addAll(getAnimationAndGraph(), getSliders(),
				getButtons(), getTimelineStuff());
	}
	private void setHelpText() {
		ReadText r = new ReadText();
		txtHelp.setText(r.getInstruction("src/topicpane/SHMHelp.txt"));
	}
	public HBox getAnimationAndGraph() {
		HBox animationGraphLayout = new HBox();
		txtHelp.setTranslateY(32);
		txtHelp.setTranslateX(30);
		txtHelp.setVisible(false);
		animationGraphLayout.getChildren().addAll(animation, graph, txtHelp);
		return animationGraphLayout;
	}
	public VBox getSliders() {
		slAmplitude.getStylesheets().add("style.css");
		slFrequency.getStylesheets().add("style.css");
		slPeriod.getStylesheets().add("style.css");
		slAmplitude.setMax(200);
		slAmplitude.setMin(0);
		slFrequency.setMax(1000);
		slFrequency.setMin(0);
		slPeriod.setMax(1000);
		slPeriod.setMin(0);
		slFrequency.setPrefWidth(1000);
		slPeriod.setPrefWidth(1000);
		slAmplitude.setPrefWidth(1000);
		slAmplitude.setShowTickLabels(false);
		slAmplitude.setShowTickMarks(true);
		slAmplitude.setMajorTickUnit(10);
		slAmplitude.setMinorTickCount(5);
		slAmplitude.setBlockIncrement(1);
		slFrequency.setShowTickLabels(false);
		slFrequency.setShowTickMarks(true);
		slFrequency.setMajorTickUnit(100);
		slFrequency.setMinorTickCount(50);
		slFrequency.setBlockIncrement(1);
		slPeriod.setShowTickLabels(false);
		slPeriod.setShowTickMarks(true);
		slPeriod.setMajorTickUnit(100);
		slPeriod.setMinorTickCount(50);
		slPeriod.setBlockIncrement(1);
		slPeriod.setValue(slPeriod.getMax());
		slAmplitude.setValue(100);
		slFrequency.setOnMouseDragged(new DragListener());
		slPeriod.setOnMouseDragged(new DragListener());
		VBox layout = new VBox();
		layout.getChildren().addAll(new Label("Amplitude"), slAmplitude);
		layout.getChildren().addAll(new Label("Frequency:"), slFrequency);
		layout.getChildren().addAll(new Label("Period"), slPeriod);
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.setPadding(new Insets(10, 10, 10, 10));
		return layout;
	}
	public HBox getButtons() {
		HBox layout = new HBox();
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.setSpacing(35);
		layout.setPadding(new Insets(10, 10, 10, 10));
		btnStart.setOnAction(new ButtonListener());
		btnPause.setOnAction(new ButtonListener());
		btnStop.setOnAction(new ButtonListener());
		btnReset.setOnAction(new ButtonListener());
		btnHelp.setOnAction(new ButtonListener());
		layout.getChildren().addAll(btnStart, btnPause, btnStop, btnReset, btnHelp);
		return layout;
	}
	public void setPeriod(double period) {
		if (period == 0)
			this.period = 1;
		else
			this.period = period;
	}
	public double getPeriod() {
		if (period == 0)
			return 1;
		return period;
	}
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	public double getAmplitude() {
		return amplitude;
	}
	public HBox getTimelineStuff() {
		final CustomNumberClass<Integer> multiplier = new CustomNumberClass<Integer>(
				0);
		final CustomNumberClass<Double> currentRate = new CustomNumberClass<Double>(
				0.0);
		final CustomNumberClass<Double> previousRate = new CustomNumberClass<Double>(
				0.0);
		final CustomNumberClass<Boolean> up = new CustomNumberClass<Boolean>(false);
		final CustomNumberClass<Boolean> down = new CustomNumberClass<Boolean>(
				false);
		HBox layout = new HBox();
		animation.getTimeline().currentTimeProperty()
				.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable obs) {
						double time = animation.getTimeline().getCurrentTime().toMillis();
						previousRate.setValue(currentRate.getValue());
						currentRate.setValue(animation.getTimeline().getCurrentRate());
						if (up.getValue()) {
							time = multiplier.getValue()
									* animation.getMaxTime()
									+ ((animation.getMaxTime() - animation.getTimeline()
											.getCurrentTime().toMillis()));
						} else if (down.getValue()) {
							time = (multiplier.getValue() * animation.getMaxTime())
									+ animation.getTimeline().getCurrentTime().toMillis();
						}
						if (previousRate.getValue() > 0 && currentRate.getValue() < 0) {
							up.setValue(true);
							down.setValue(false);
							multiplier.setValue(multiplier.getValue() + 1);
						} else if (previousRate.getValue() < 0
								&& currentRate.getValue() > 0) {
							down.setValue(true);
							up.setValue(false);
							multiplier.setValue(multiplier.getValue() + 1);
						}
						double position = animation.getRectangle().getY() * -1;
						graph.addSerieData(time, position);
					}
				});
		layout.setSpacing(50);
		return layout;
	}
	class DragListener implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {
			Object component = e.getSource();
			if (component == slFrequency) {
				slPeriod.setValue(1000 - e.getX());
			} else if (component == slPeriod) {
				slFrequency.setValue(1000 - e.getX());
			}
		}
	}
	private boolean showHelp = false;
	class ButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Object component = e.getSource();
			if (component == btnStart) {
				setAmplitude(slAmplitude.getValue());
				setPeriod(slPeriod.getValue());
				animation.setInterpolator(amplitude, Duration.millis(period));
				animation.startTimeline();
				btnStop.setDisable(false);
				btnStart.setDisable(true);
			} else if (component == btnStop) {
				btnStop.setDisable(true);
				btnPause.setDisable(true);
				animation.stopTimeline();
			} else if (component == btnReset) {
				animation.stopTimeline();
				menu.Menu.addCenterPane(new SHM());
			} else if (component == btnPause) {
				btnStop.setDisable(true);
				btnPause.setDisable(true);
				animation.pause();
			} else if (component == btnHelp) {
				if (!showHelp) {
					showHelp = true;
					btnHelp.setText("Close Help");
					txtHelp.setVisible(true);
				} else {
					showHelp = false;
					btnHelp.setText("Help");
					txtHelp.setVisible(false);
				}
			}
		}
	}
}