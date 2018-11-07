package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SlideGameFrame extends JFrame
{
	private final SlideGameManager slideGameManager;

	public SlideGameFrame()
	{
		super("Slide Game");

		Grid grid = new Grid(4);
		SlideGameCanvas slideGameCanvas = new SlideGameCanvas(grid);

		JLabel gameOverLabel = new JLabel();
		JLabel scoreLabel = new JLabel();
		JLabel highScoreLabel = new JLabel();
		Border border = BorderFactory.createLineBorder(SlideGameColors.SPACER_COLOR);
		scoreLabel.setBorder(border);
		highScoreLabel.setBorder(border);

		this.slideGameManager = new SlideGameManager(grid, this, slideGameCanvas, gameOverLabel, scoreLabel, highScoreLabel);

		initSize(slideGameCanvas, gameOverLabel, scoreLabel, highScoreLabel);
	}

	private void initSize(SlideGameCanvas slideGameCanvas, JLabel gameOverLabel, JLabel scoreLabel, JLabel highScoreLabel)
	{
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(gameOverLabel);
		topPanel.add(Box.createGlue());
		topPanel.add(scoreLabel);
		topPanel.add(highScoreLabel); // TODO: the high score label is a little close to the score label.

		this.setJMenuBar(createJMenuBar());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(slideGameCanvas);

		this.setMinimumSize(new Dimension(400, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	private JMenuBar createJMenuBar()
	{
		JMenuItem newGame_MI = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newGame_MI.addActionListener(actionEvent -> slideGameManager.newGame());

		JMenuItem exit_MI = new JMenuItem("Exit", KeyEvent.VK_X);
		exit_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		exit_MI.addActionListener(actionEvent -> Runtime.getRuntime().exit(0));

		JMenuItem help_MI = new JMenuItem("Help", KeyEvent.VK_H);
		help_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
		help_MI.addActionListener(actionEvent -> this.showHelpPopup());

		JMenuItem about_MI = new JMenuItem("About", KeyEvent.VK_F1);
		about_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
		about_MI.addActionListener(actionEvent -> this.showAboutPopup());

		JMenu actionMenu = new JMenu("Action");
		// TODO: set size, goal sliders
		//actionMenu.add(setGridSize_MI);
		//actionMenu.add(setGoalTile_MI);
		//actionMenu.addSeparator();
		actionMenu.add(newGame_MI);
		actionMenu.add(exit_MI);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(help_MI);
		helpMenu.add(about_MI);

		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(actionMenu);
		mainMenu.add(helpMenu);

		return mainMenu;
	}

	private void showAboutPopup()
	{
		String message
				= "By: Jacob Patterson"
				+ "\n"
				+ "\nCopyright(©) 2018"
				+ "\n"
				+ "\nBased on the game by Gabriele Cirulli.";
		String title = "About " + getTitle();
		showPopup(message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private void showHelpPopup()
	{
		String message
				= "Slide the tiles all to the top, bottom, left, and right of the grid to\n"
				+ "combine tiles of equal values. When tiles combine, their combined value is\n"
				+ "the some of the value. Combine tiles by typing the appropriate arrow key,\n"
				+ "using WSAD controls, or clicking and dragging in the desired direction on the\n"
				+ "grid. After each turn a new tile is added. WThe game is over if the grid is\n"
				+ "full and no moves can be made and is won when the 2048 tile is created.";

		String title = "Help for " + getTitle();
		showPopup(message, title, JOptionPane.QUESTION_MESSAGE);
	}

	private void showPopup(String message, String title, int messageType)
	{
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
}
