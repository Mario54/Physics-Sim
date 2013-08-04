package menu;
import java.io.File;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
public class MenuPane extends Pane {
	private String instruction = "";
	private Text instructionComp = new Text(instruction);
	public MenuPane() {
		try {
			getInstruction();
		} catch (Exception e) {
			System.err.println("File no found");
		}
		this.getChildren().add(membershipGroup());
	}
	public void getInstruction() throws Exception {
		File txtInstruction = new File("src/menu/instruction.txt");
		Scanner fileScanner = new Scanner(txtInstruction);
		while (fileScanner.hasNextLine()) {
			instruction += fileScanner.nextLine() + "\n";
		}
		fileScanner.close();
	}
	public VBox membershipGroup() {
		VBox layout = new VBox();
		layout.setPadding(new Insets(0, 0, 0, 50));
		instructionComp.setText(instruction);
		instructionComp.setId("info-text");
		layout.getChildren().add(instructionComp);
		return layout;
	}
}
