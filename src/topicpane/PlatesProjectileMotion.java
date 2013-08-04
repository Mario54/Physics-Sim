package topicpane;

import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import reader.ReadText;
import topicpane.animation.CarBean;
import javafx.scene.shape.Rectangle;

public class PlatesProjectileMotion extends Pane {

    // User Interface Controls
	private VBox inputs = new VBox();
    private HBox inputBoxTop = new HBox();
    private HBox inputBoxBottom = new HBox();
    private BorderPane mainLayout = new BorderPane();
    private VBox chartBox = new VBox();
    private VBox helpBox = new VBox();
    private Group animationBox = new Group();
    private Label initialSpeedXLbl = new Label("Initial Speed in X: ");
    private TextField initialSpeedX = new TextField("40");
    private Label initialSpeedYLbl = new Label("Initial Speed in Y:");
    private TextField initialSpeedY = new TextField("30");
    private Label electricFieldLbl = new Label("Electric Field:");
    private TextField electricField = new TextField("5");
    private Label highPlateChargeLbl = new Label("Charge on High Plate:");
    private TextField highPlateCharge = new TextField("+");
    private Label lowPlateChargeLbl = new Label("Charge on Low Plate:");
    private TextField lowPlateCharge = new TextField("-");
    private Label dotChargeLbl = new Label("Charge of dot:");
    private TextField dotCharge = new TextField("3");
    private Button start = new Button("Start Animation");
    private Button pause = new Button("Pause");
    private Button reset = new Button("Reset");
    private Button help = new Button("Help");
    
    // Graph Components
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
    
    private int centerX = 200, centerY = 200, width = 700, height = 15, distance = 200, radius = 7;
    private double electricFieldConstant;
    private final double massConstant = 1;
    private double x, y;
    private final Circle dot = new Circle(centerX, centerY, radius);
    private final Rectangle rectangle1 = new Rectangle(centerX, -distance + centerY, width, height);
    private final Rectangle rectangle2 = new Rectangle(centerX, distance + centerY, width, height);
    private final TimeCounter counter = new TimeCounter();
    private CarBean carBean = new CarBean();
    
    private String lowCharge;
    private String highCharge;
    private double electricFieldValue;
    private int chargeOnDot;
    private double speedX;
    private double speedY;
    private double initialTime;
    private double initialVelocityX;
    private double initialVelocityY;
    private int frame = 10;

    private Label instructionComp = new Label();
	private boolean paused = false;
	private long timePaused;
    
    public PlatesProjectileMotion() {
        this.getChildren().add(mainLayout);
        
        ReadText readText = new ReadText();
        instructionComp.setText(readText.getInstruction("src/topicpane/platesProjMotionHelp.txt"));
        

        inputBoxTop.getChildren().addAll(initialSpeedXLbl, initialSpeedX, initialSpeedYLbl,
                initialSpeedY, electricFieldLbl, electricField, start, pause, reset, help);
        inputBoxTop.setSpacing(10);
        inputBoxTop.setPadding(new Insets(15,15,15,100));
        inputBoxTop.setAlignment(Pos.BASELINE_LEFT);

        inputBoxBottom.getChildren().addAll(highPlateChargeLbl, highPlateCharge,
                lowPlateChargeLbl, lowPlateCharge, dotChargeLbl, dotCharge);
        inputBoxBottom.setSpacing(20);
        inputBoxBottom.setPadding(new Insets(15,15,15,100));
        inputBoxBottom.setAlignment(Pos.BASELINE_LEFT);
        highPlateCharge.setPrefWidth(50);
        lowPlateCharge.setPrefWidth(50);
        
        inputs.getChildren().addAll(inputBoxTop, inputBoxBottom);

        mainLayout.setBottom(inputs);
        helpBox.setAlignment(Pos.BOTTOM_CENTER);
        animationBox.getChildren().addAll(rectangle1, dot, rectangle2);

        chartBox.getChildren().addAll(positionXChart, positionYChart, velocityYChart);
        mainLayout.setCenter(animationBox);
        mainLayout.setLeft(chartBox);
        mainLayout.setRight(helpBox);

        @SuppressWarnings({"unchecked", "rawtypes"})
        final Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
                new EventHandler() {
                    @Override
                    public void handle(Event event) {
                    	if (!paused)
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

        positionXChart.setCreateSymbols(false);
        positionXChart.getData().add(positionXSeries);
        positionXChart.setPrefHeight(300);
        positionYChart.setCreateSymbols(false);
        positionYChart.getData().add(positionYSeries);
        positionYChart.setPrefHeight(300);
        velocityYChart.setCreateSymbols(false);
        velocityYChart.getData().add(velocityYSeries);
        velocityYChart.setPrefHeight(300);

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
            	paused = false;
                 positionXSeries.getData().clear();
                 positionYSeries.getData().clear();
                 velocityYSeries.getData().clear();
                
                electricFieldValue = Double.valueOf(electricField.getText());
                chargeOnDot = Integer.valueOf(dotCharge.getText());
                speedX = Double.valueOf(initialSpeedX.getText());
                speedY = Double.valueOf(initialSpeedY.getText());
                highCharge = highPlateCharge.getText();
                lowCharge = lowPlateCharge.getText();

                if (!(highCharge.equals(lowCharge))) {
                    if (((chargeOnDot > 0) && (highCharge).equals("+"))
                        || ((chargeOnDot < 0) && (highCharge).equals("+"))) {

                        electricFieldConstant = electricFieldValue;
                        carBean.setSpeedX(speedX);
                        carBean.setSpeedY(speedY);

                    } 
                    else if (((chargeOnDot > 0) && (highCharge).equals("-"))
                            || ((chargeOnDot < 0) && (highCharge).equals("-"))) {

                        electricFieldConstant = 0 - electricFieldValue;
                        carBean.setSpeedX(speedX);
                        carBean.setSpeedY(speedY);
                        
                    }
                } 
                
                else {
                    carBean.setSpeedX(0);
                    carBean.setSpeedY(0);
                    electricFieldConstant = 0;
                }

                carBean.setX(centerX);
                carBean.setY(centerY);
                initialVelocityX = carBean.getSpeedX();
                initialVelocityY = carBean.getSpeedY();
                carBean.setAccelerationY((chargeOnDot * electricFieldConstant) / massConstant);
                initialTime = (int) System.currentTimeMillis();
                counter.reset();
                timeline.play();

            }
        });
        
        
        
        
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
            	if (paused) {
        			pause(false);
        			pause.setText("Pause");
        		} else {
        			pause(true);
        			pause.setText("Play");
        		}             
                 
            }
        });
        
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
            	paused = true;
            	 positionXSeries.getData().clear();
                 positionYSeries.getData().clear();
                 velocityYSeries.getData().clear();
            }
        });
        
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if (helpBox.getChildren().isEmpty()) {
                	helpBox.getChildren().add(instructionComp);
                }
                
                else {
                	helpBox.getChildren().clear();
                }
            }
        });
    }

    private void refreshScene() {
        frame++;
        int b = 1000;
        
        double time = (int) System.currentTimeMillis() - initialTime;

        double velocityY = (initialVelocityY
                - ((double) carBean.getAccelerationY() * (time / b)));

        if ((carBean.getY() <= (centerY + distance - radius) && (carBean.getY() >= (centerY - (distance - radius - 2))))) {
            

            carBean.setSpeedY(velocityY);
            carBean.setX((double) (centerX + (initialVelocityX * (time / b))));
            carBean.setY((double) (centerY - (carBean.getSpeedY() * (time / b)
                    + 0.5 * carBean.getAccelerationY() * Math.pow((time / b), 2))));

            x = (carBean.getX());
            y = (carBean.getY());

            gotoxy(x, y);

            if (frame >= 50) {
                frame = 0;

                positionXSeries.getData().add(
                        new XYChart.Data<Number, Number>(time / 1000, (x - centerX)));
                positionYSeries.getData().add(
                        new XYChart.Data<Number, Number>(time / 1000, -(y - centerY)));
                velocityYSeries.getData().add(
                        new XYChart.Data<Number, Number>(time / 1000, carBean
                        .getSpeedY()));
            }
        }
    }

    private void gotoxy(double x, double y) {
        dot.setCenterX(x);
        dot.setCenterY(y);
    }
    
    public void pause(boolean pause) {
		paused = pause;

		if (!paused) {
			initialTime += System.currentTimeMillis() - timePaused;
		} else {
			timePaused = System.currentTimeMillis();
		}
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
