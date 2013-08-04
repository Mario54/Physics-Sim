package topicpane;
import reader.ReadText;
import topicpane.animation.*;
import topicpane.graph.*;
import javafx.beans.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
public class DopplerEffect extends StackPane {
	private final int spacing = 25;
	private final int speed_of_sound = 343;
	private VBox mainLayout = new VBox();
	private TextField txtEmitterSpeed = new TextField("10");
	private TextField txtListenerSpeed = new TextField("10");
	private TextField txtEmitterFrequency = new TextField("25");
	private Button btnStart = new Button("Start");
	private Button btnPause = new Button("Pause");
	private Button btnStop = new Button("Stop");
	private Button btnReset = new Button("Reset");
	private Button btnHelp = new Button("Help");
	private Label txtHelp = new Label();
	String dirLeft = "<-";
	String dirRight = "->";
	private Button btnPositionListener = new Button("Switch Listener Position");
	private Button btnPositionEmitter = new Button("Switch Emitter Position");
	private Button btnDirectionListener = new Button("Current Direction: \""
			+ dirLeft + "\"");
	private Button btnDirectionEmitter = new Button("Current Direction: \""
			+ dirLeft + "\"");
	private double originalFrequency = 1;
	private double finalFrequency = 1;
	private double listenerSpeed = 1;
	private double emitterSpeed = 1;
	final private DopplerEffectAnimation animation = new DopplerEffectAnimation();
	private GeneralGraph graph = new GeneralGraph("Time(s)", "Frequency (Hz)",
			"Doppler Effect Frequency", false);
	public DopplerEffect() {
		this.getChildren().add(mainLayout);
		getAnimationValue();
		mainLayoutSetting();
	}
	public void mainLayoutSetting() {
		mainLayout.setSpacing(25);
		mainLayout.getChildren().addAll(animationAndGraphContainer(),
				componentsContainer(), buttonContainer());
	}
	public double getOriginalFrequency() {
		return originalFrequency;
	}
	public void setFinalFrequency(double finalFreq) {
		finalFrequency = finalFreq;
	}
	public HBox getDirectionButtons() {
		VBox pos = new VBox();
		pos.setSpacing(spacing);
		pos.getChildren().addAll(btnPositionListener, btnPositionEmitter);
		VBox dir = new VBox();
		dir.setSpacing(spacing);
		dir.getChildren().addAll(btnDirectionListener, btnDirectionEmitter);
		HBox layout = new HBox();
		layout.setSpacing(spacing);
		layout.getChildren().addAll(pos, dir);
		return layout;
	}
	public double getFinalFrequency() {
		return finalFrequency;
	}
	public HBox animationAndGraphContainer() {
		VBox container = new VBox();
		HBox helpContainer = new HBox();
		setTxtHelp();
		helpContainer.getChildren().addAll();
		container.getChildren().addAll(animation, graph);
		txtHelp.setVisible(false);
		helpContainer.getChildren().addAll(container, txtHelp);
		txtHelp.setTranslateY(108);
		return helpContainer;
	}
	public void setTxtHelp() {
		ReadText r = new ReadText();
		txtHelp.setText(r.getInstruction("src/topicpane/DopplerEffectHelp.txt"));
	}
	public HBox componentsContainer() {
		HBox componentLayout = new HBox();
		componentLayout.getChildren().addAll(getTexts(), getTextFields(),
				getDirectionButtons());
		return componentLayout;
	}
	public VBox getTexts() {
		VBox textLayout = new VBox();
		textLayout.setSpacing(spacing);
		textLayout.setPadding(new Insets(5, 5, 5, 5));
		textLayout.getChildren().addAll(new Label("Emitter Speed"),
				new Label("Listener Speed"), new Label("Emitter Frequency"));
		return textLayout;
	}
	public VBox getTextFields() {
		VBox textFieldLayout = new VBox();
		textFieldLayout.setSpacing(spacing);
		textFieldLayout.setPadding(new Insets(5, 5, 5, 5));
		textFieldLayout.getChildren().addAll(txtEmitterSpeed, txtListenerSpeed,
				txtEmitterFrequency);
		return textFieldLayout;
	}
	public double getEmitterFrequency() {
		return Double.parseDouble(txtEmitterFrequency.getText());
	}
	public HBox buttonContainer() {
		setButtons();
		HBox layout = new HBox();
		layout.setSpacing(spacing);
		layout.setPadding(new Insets(5, 5, 5, 5));
		layout.getChildren().addAll(btnStart, btnPause, btnStop, btnReset, btnHelp);
		return layout;
	}
	public void setButtons() {
		btnStart.setOnAction(new ButtonListener());
		btnStop.setOnAction(new ButtonListener());
		btnReset.setOnAction(new ButtonListener());
		btnPause.setOnAction(new ButtonListener());
		btnHelp.setOnAction(new ButtonListener());
		btnPositionListener.setOnAction(new ButtonToggle());
		btnPositionEmitter.setOnAction(new ButtonToggle());
		btnDirectionListener.setOnAction(new ButtonToggle());
		btnDirectionEmitter.setOnAction(new ButtonToggle());
	}
	public void getAnimationValue() {
		final CustomNumberClass<Double> listenerPosition = new CustomNumberClass<Double>(
				animation.getListener().getNode().getTranslateX());
		final CustomNumberClass<Double> emitterPosition = new CustomNumberClass<Double>(
				animation.getListener().getNode().getTranslateX());
		final CustomNumberClass<Double> currentTime = new CustomNumberClass<Double>(
				animation.getListener().getCurrentTime().toSeconds());
		final CustomNumberClass<Double> previousTime = new CustomNumberClass<Double>(
				currentTime.getValue());
		final CustomNumberClass<Double> previousLisPosition = new CustomNumberClass<Double>(
				listenerPosition.getValue());
		final CustomNumberClass<Double> previousEmPosition = new CustomNumberClass<Double>(
				emitterPosition.getValue());
		animation.getEmitter().currentTimeProperty()
				.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable ov) {
						previousTime.setValue(currentTime.getValue());
						previousLisPosition.setValue(listenerPosition.getValue());
						previousEmPosition.setValue(emitterPosition.getValue());
						listenerPosition.setValue(animation.getListener().getNode()
								.getTranslateX());
						emitterPosition.setValue(animation.getEmitter().getNode()
								.getTranslateX());
						currentTime.setValue(animation.getListener().getCurrentTime()
								.toSeconds());
						double deltaTime = currentTime.getValue() - previousTime.getValue();
						listenerSpeed = (listenerPosition.getValue() - previousLisPosition
								.getValue()) / deltaTime;
						emitterSpeed = (emitterPosition.getValue() - previousEmPosition
								.getValue()) / deltaTime;
						double numerator = 1;
						double denominator = 1;
						if (listenerPosition.getValue() > previousLisPosition.getValue()
								&& emitterPosition.getValue() < previousEmPosition.getValue()) {
							numerator = speed_of_sound - listenerSpeed;
							denominator = speed_of_sound + emitterSpeed;
						} else if (listenerPosition.getValue() < previousLisPosition
								.getValue()
								&& emitterPosition.getValue() > previousEmPosition.getValue()) {
							numerator = speed_of_sound + listenerSpeed;
							denominator = speed_of_sound - emitterSpeed;
						} else {
							numerator = speed_of_sound + listenerSpeed;
							denominator = speed_of_sound + emitterSpeed;
						}
						setFinalFrequency(getOriginalFrequency()
								* (numerator / denominator));
						graph.addSerieData(animation.getListener().getCurrentTime()
								.toSeconds(), getFinalFrequency());
					}
				});
	}
	private boolean showHelp = false;
	class ButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Object component = e.getSource();
			if (component == btnStart) {
				animation.play();
				originalFrequency = getEmitterFrequency();
				btnStart.setDisable(true);
			} else if (component == btnStop) {
				btnStart.setDisable(true);
				btnPause.setDisable(true);
				animation.stop();
			} else if (component == btnReset) {
				animation.stop();
				menu.Menu.addCenterPane(new DopplerEffect());
			} else if (component == btnPause) {
				btnStart.setDisable(true);
				btnPause.setDisable(true);
				animation.stop();
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
	private boolean listenerPos = true;
	private boolean emitterPos = true;
	private boolean listenerDir = true;
	private boolean emitterDir = true;
	class ButtonToggle implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Object btn = e.getSource();
			double pos1 = 0, pos2 = 0, dir1 = 0, dir2 = 0;
			if (btn == btnPositionListener) {
				if (listenerPos) {
					pos1 = 0;
					listenerPos = false;
				} else {
					pos1 = 350;
					listenerPos = true;
				}
				animation.setIniLis(pos1);
				System.out.println(animation.xTranslationLis);
			} else if (btn == btnPositionEmitter) {
				if (emitterPos) {
					pos2 = 350 - 2 * animation.radius;
					emitterPos = false;
				} else {
					pos2 = 700 - 2 * animation.radius;
					emitterPos = true;
				}
				animation.setIniEm(pos2);
				System.out.println(animation.xTranslationEm);
			} else if (btn == btnDirectionListener) {
				if (listenerDir) {
					dir1 = 0;
					listenerDir = false;
					btnDirectionListener
							.setText("Current Direction: \"" + dirLeft + "\"");
				} else {
					btnDirectionListener.setText("Current Direction: \"" + dirRight
							+ "\"");
					dir1 = 350;
					listenerDir = true;
				}
				animation.setFinLis(dir1);
				System.out.println(animation.xTranslationFinalLis);
			} else if (btn == btnDirectionEmitter) {
				if (emitterDir) {
					dir2 = 350 - 2 * animation.radius;
					btnDirectionEmitter.setText("Current Direction: \"" + dirLeft + "\"");
					emitterDir = false;
				} else {
					btnDirectionEmitter
							.setText("Current Direction: \"" + dirRight + "\"");
					dir2 = 700 - 2 * animation.radius;
					emitterDir = true;
				}
				animation.setFinEm(dir2);
				System.out.println(animation.xTranslationFinalEm);
			}
			animation.setShapes(Color.BLUE, Color.RED);
			animation.setCompAnim();
		}
	}
}
