package menu;
import topicpane.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class Menu extends Application {
	private static ChoiceBox<Object> cbMechanics = new ChoiceBox<Object>();
	private static ChoiceBox<Object> cbWaves = new ChoiceBox<Object>();
	private static ChoiceBox<Object> cbElectricity = new ChoiceBox<Object>();
	private static Button btnMenu = new Button("Back to Menu");
	private static Button btnExit = new Button("Exit");
	private static Pane centerPane = new StackPane();
	private static MenuPane mainMenu = new MenuPane();
	private static BorderPane border = new BorderPane();
	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		HBox topLayout = new HBox();
		topLayout.setAlignment(Pos.CENTER);
		topLayout.setPadding(new Insets(15, 15, 15, 15));
		topLayout.setSpacing(35);
		addComponents(topLayout);
		border.setTop(topLayout);
		root.getChildren().add(border);
		border.setCenter(centerPane);
		centerPane.getChildren().add(CenterPaneDefaultValue());
		Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}
	public Pane CenterPaneDefaultValue() {
		btnMenu.setDisable(true);
		cbMechanics.setDisable(false);
		cbWaves.setDisable(false);
		cbElectricity.setDisable(false);
		return new MenuPane();
	}
	public void addComponents(HBox layout) {
		cbMechanics.setItems(FXCollections.observableArrayList("Mechanics",
				new Separator(), "Projectile Motion", "Friction"));
		cbWaves.setItems(FXCollections.observableArrayList(
				"Optics, Waves and Modern Physics", new Separator(),
				"Simple Harmonic Motion", "Doppler Effect"));
		cbElectricity.setItems(FXCollections.observableArrayList(
				"Electricity and Magnetism", new Separator(), "RC Circuit",
				"Electrical Plates Projectile Motion"));
		cbMechanics.getSelectionModel().selectFirst();
		cbWaves.getSelectionModel().selectFirst();
		cbElectricity.getSelectionModel().selectFirst();
		cbMechanics.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> obsVal,
							Number oldVal, Number newVal) {
						cbWaves.getSelectionModel().selectFirst();
						cbElectricity.getSelectionModel().selectFirst();
						btnMenu.setDisable(false);
						switch (newVal.intValue()) {
						case 2:
							addCenterPane(new ProjectileMotion());
							break;
						case 3:
							addCenterPane(new Friction());
							break;
						}
					}
				});
		cbWaves.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> obsVal,
							Number oldVal, Number newVal) {
						cbMechanics.getSelectionModel().selectFirst();
						cbElectricity.getSelectionModel().selectFirst();
						btnMenu.setDisable(false);
						switch (newVal.intValue()) {
						case 2:
							addCenterPane(new SHM());
							break;
						case 3:
							addCenterPane(new DopplerEffect());
							break;
						}
					}
				});
		cbElectricity.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> obsVal,
							Number oldVal, Number newVal) {
						cbWaves.getSelectionModel().selectFirst();
						cbMechanics.getSelectionModel().selectFirst();
						btnMenu.setDisable(false);
						switch (newVal.intValue()) {
						case 2:
							addCenterPane(new RCCircuit());
							break;
						case 3:
							addCenterPane(new PlatesProjectileMotion());
							break;
						}
					}
				});
		btnMenu.setOnAction(new ButtonListener());
		btnExit.setOnAction(new ButtonListener());
		layout.getChildren().add(cbMechanics);
		layout.getChildren().add(cbWaves);
		layout.getChildren().add(cbElectricity);
		layout.getChildren().add(btnMenu);
		layout.getChildren().add(btnExit);
	}
	public static void addCenterPane(Pane paneToAdd) {
		centerPane.getChildren().clear();
		centerPane.getChildren().add(paneToAdd);
	}
	public static void main(String args[]) {
		launch(args);
	}
	class ButtonListener implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if (e.getSource() == btnMenu) {
				addCenterPane(CenterPaneDefaultValue());
				cbMechanics.getSelectionModel().selectFirst();
				cbElectricity.getSelectionModel().selectFirst();
				cbWaves.getSelectionModel().selectFirst();
			} else if (e.getSource() == btnExit) {
				Platform.exit();
			}
		}
	}
}
