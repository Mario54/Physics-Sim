package topicpane.animation;
import java.text.*;
import java.util.*;
import javafx.geometry.Point2D;
public interface RCCircuitInterface {
	int widthCB = 20;
	int heightCB = 150;
	int heightR = 30;
	int distCB = 20;
	int widthR = 200;
	int batteryX = 700;
	int capacitorX = 400;
	int resistor1X = 900;
	int strokeWidth = 7;
	Point2D topR = new Point2D(1300, 150);
	Point2D botL = new Point2D(250, 500);
	Map objectsX = new HashMap<String, Double>();
	DecimalFormat df = new DecimalFormat("#.00");
}
