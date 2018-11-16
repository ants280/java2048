package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
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
	private static final long serialVersionUID = 5L;
	private final SlideGameManager slideGameManager;
	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
			2, 10, 2, 10); // top, left, bottom, right
	private static final Border LINE_BORDER
			= BorderFactory.createCompoundBorder(
					EMPTY_BORDER,
					BorderFactory.createLineBorder(
							SlideGameColors.SPACER_COLOR,
							1, // width
							true)); // rounded

	public SlideGameFrame()
	{
		super("Slide Game");

		Grid grid = new Grid(4);
		JComponent slideGameDisplayComponent
				= new SlideGameDisplayComponent(grid);
		JLabel gameOverLabel = createJLabel(false);
		JLabel scoreLabel = createJLabel(true);
		JLabel highScoreLabel = createJLabel(true);
		JLabel goalLabel = createJLabel(false);
		JLabel moveLabel = createJLabel(false);
		this.slideGameManager = new SlideGameManager(
				grid,
				this,
				slideGameDisplayComponent,
				gameOverLabel,
				scoreLabel,
				highScoreLabel,
				goalLabel,
				moveLabel);

		initSize(
				slideGameDisplayComponent,
				gameOverLabel,
				scoreLabel,
				highScoreLabel,
				goalLabel,
				moveLabel);
	}

	private void initSize(
			JComponent slideGameDisplayComponent,
			JLabel gameOverLabel,
			JLabel scoreLabel,
			JLabel highScoreLabel,
			JLabel goalLabel,
			JLabel moveLabel)
	{
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(gameOverLabel);
		topPanel.add(Box.createGlue());
		topPanel.add(scoreLabel);
		topPanel.add(highScoreLabel);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(goalLabel);
		bottomPanel.add(Box.createGlue());
		bottomPanel.add(moveLabel);
		slideGameDisplayComponent.setBorder(EMPTY_BORDER);

		this.setJMenuBar(createJMenuBar());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(slideGameDisplayComponent);

		this.setMinimumSize(new Dimension(400, 447));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	private JLabel createJLabel(boolean addBorder)
	{
		JLabel label = new JLabel();
		label.setBorder(addBorder ? LINE_BORDER : EMPTY_BORDER);
		return label;
	}

	private JMenuBar createJMenuBar()
	{
		JMenuItem setGridLength_MI = new JMenuItem("Set grid length");
		setGridLength_MI.addActionListener(
				actionEvent -> this.showSetGridLengthPopup());

		JMenuItem setGoalTileValue_MI = new JMenuItem("Set goal tile value");
		setGoalTileValue_MI.addActionListener(
				actionEvent -> this.showSetGoalTileValuePopup());

		JMenuItem newGame_MI = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame_MI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newGame_MI.addActionListener(
				actionEvent -> slideGameManager.newGame());

		JMenuItem exit_MI = new JMenuItem("Exit", KeyEvent.VK_X);
		exit_MI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		exit_MI.addActionListener(
				actionEvent -> Runtime.getRuntime().exit(0));

		JMenuItem help_MI = new JMenuItem("Help", KeyEvent.VK_H);
		help_MI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
		help_MI.addActionListener(
				actionEvent -> this.showHelpPopup());

		JMenuItem about_MI = new JMenuItem("About", KeyEvent.VK_F1);
		about_MI.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F1, KeyEvent.CTRL_DOWN_MASK));
		about_MI.addActionListener(
				actionEvent -> this.showAboutPopup());

		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(setGridLength_MI);
		actionMenu.add(setGoalTileValue_MI);
		actionMenu.addSeparator();
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
				= "Slide the tiles all to the top, left, bottom, and right of "
				+ "the grid to\ncombine tiles of equal values. When tiles "
				+ "combine, the combined tile's value\nis their sum of values."
				+ " Combine tiles by typing the appropriate arrow key,\nusing "
				+ "WASD controls, or clicking and dragging in the desired "
				+ "direction on the\ngrid. After each turn a new tile is "
				+ "added. The game is over if the grid is\nfull and no moves "
				+ "can be made. It is won when a tile of the goal value is\n"
				+ "created.";
		String title = "Help for " + getTitle();
		showPopup(message, title, JOptionPane.QUESTION_MESSAGE);
	}

	private void showPopup(String message, String title, int messageType)
	{
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	private void showSetGridLengthPopup()
	{
		String message = "Set grid length for " + getTitle();
		int goalTileValue = slideGameManager.getGoalTileValue();
		int minimumGridLength = (int) Math.ceil(Math.sqrt(
				(Math.log(goalTileValue) / Math.log(2d)) - 1));
		Object[] selectionValues = IntStream.range(
				minimumGridLength, minimumGridLength + 10)
				.boxed()
				.toArray();
		int initialSelectionValue = slideGameManager.getGridLength();
		showOptionDialog(
				message,
				selectionValues, initialSelectionValue,
				slideGameManager::setGridLength);
	}

	private void showSetGoalTileValuePopup()
	{
		String message = "Set goal tile value for " + getTitle();
		int gridLength = slideGameManager.getGridLength();
		int maximumGoalTileValue = (int) Math.pow(2, Math.pow(gridLength, 2));
		Object[] selectionValues = IntStream.range(0, 10)
				.map(i -> (int) Math.pow(2, i + 3))
				.filter(possibleGoalTileValue
						-> possibleGoalTileValue <= maximumGoalTileValue)
				.boxed()
				.toArray();
		int initialSelectionValue = slideGameManager.getGoalTileValue();
		showOptionDialog(
				message,
				selectionValues, initialSelectionValue,
				slideGameManager::setGoalTileValue);
	}

	private void showOptionDialog(
			String message,
			Object[] selectionValues, Object initialSelectionValue,
			Consumer<Integer> setValueFunction)
	{
		Object optionChoice = JOptionPane.showInputDialog(
				this,
				message, "Change field for " + getTitle(),
				JOptionPane.QUESTION_MESSAGE, null,
				selectionValues, initialSelectionValue);
		if (optionChoice != null)
		{
			setValueFunction.accept(Integer.parseInt(optionChoice.toString()));
		}
	}
}
