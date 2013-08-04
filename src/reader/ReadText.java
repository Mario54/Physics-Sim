package reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadText {

	private String text = "";

	public String getInstruction(String path) {
		File txtFile = new File(path);
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(txtFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		while (fileScanner.hasNextLine()) {
			text += fileScanner.nextLine() + "\n";
		}
		fileScanner.close();
		return text;
	}

	public String getText() {
		return text;
	}

}
