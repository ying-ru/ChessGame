package ui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public MainFrame() {
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setSize((int) (screenSize.getWidth() / 2.4),
				(int) (screenSize.getHeight() / 2.4));
		setTitle("No芋暗棋");
		setVisible(true);
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
