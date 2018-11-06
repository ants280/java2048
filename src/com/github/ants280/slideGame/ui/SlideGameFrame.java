package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.Grid.MoveDirection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SlideGameFrame extends JFrame
{
	private final Grid grid;
	private final SlideGameCanvas slideGameCanvas;
	private final JLabel scoreLabel;
	private final JLabel highScoreLabel;
	private int score;
	private int highScore;
	
	private static final Map<Integer, MoveDirection> keyCodeMoveDirections = new HashMap<>(); // TODO: is final, but unmodifiable :(
	static
	{
		Stream.of(KeyEvent.VK_W, KeyEvent.VK_UP)
				.forEach(keyCode -> keyCodeMoveDirections.put(keyCode, MoveDirection.UP));
		Stream.of(KeyEvent.VK_A, KeyEvent.VK_LEFT)
				.forEach(keyCode -> keyCodeMoveDirections.put(keyCode, MoveDirection.LEFT));
		Stream.of(KeyEvent.VK_S, KeyEvent.VK_DOWN)
				.forEach(keyCode -> keyCodeMoveDirections.put(keyCode, MoveDirection.DOWN));
		Stream.of(KeyEvent.VK_D, KeyEvent.VK_RIGHT)
				.forEach(keyCode -> keyCodeMoveDirections.put(keyCode, MoveDirection.RIGHT));
	}

	public SlideGameFrame()
	{
		super("Slide Game");
		this.grid = new Grid(4);
		this.slideGameCanvas = new SlideGameCanvas(grid);
		this.addKeyListener(new SlideGameKeyListener());

		this.scoreLabel = new JLabel();
		this.highScoreLabel = new JLabel();
		Border border = BorderFactory.createLineBorder(SlideGameCanvas.SPACER_COLOR);
		scoreLabel.setBorder(border);
		highScoreLabel.setBorder(border);
		this.score = 0;
		this.highScore = 0;

		grid.addRandomTile();
		grid.addRandomTile();

		updateScoreLabels();
		initSize();
	}

	private void initSize()
	{
		JPanel labelPanel = new JPanel();
		labelPanel.add(scoreLabel, BorderLayout.WEST);
		labelPanel.add(highScoreLabel, BorderLayout.EAST);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(Box.createGlue());
		topPanel.add(labelPanel);

		this.setJMenuBar(createJMenuBar());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(slideGameCanvas);

		this.setMinimumSize(new Dimension(400, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	private void incrementScore(int additionalScore)
	{
		if (additionalScore != 0)
		{
			this.score += additionalScore;
			if (score > highScore)
			{
				highScore = score;
			}
			updateScoreLabels();
		}
	}

	private void updateScoreLabels()
	{
		this.scoreLabel.setText("SCORE: " + score);
		this.highScoreLabel.setText("BEST: " + highScore);
	}

	private JMenuBar createJMenuBar()
	{
		JMenuItem newGame_MI = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newGame_MI.addActionListener(actionEvent -> this.restartGame());

		JMenuItem exit_MI = new JMenuItem("Exit", KeyEvent.VK_X);
		exit_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		exit_MI.addActionListener(actionEvent -> Runtime.getRuntime().exit(0));

		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(newGame_MI);
		actionMenu.add(exit_MI);

//		JMenu helpMenu = new JMenu("Help");
//		helpMenu.add(help_MI);
//		helpMenu.add(about_MI);
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(actionMenu);
//		mainMenu.add(helpMenu);

		return mainMenu;
	}

	private void restartGame()
	{
		// TODO: restart game logic
	}

	// It might be nice to make this a separate class.
	private class SlideGameKeyListener extends KeyAdapter implements KeyListener
	{
		@Override
		public void keyReleased(KeyEvent keyEvent)
		{
			MoveDirection moveDirection = keyCodeMoveDirections.get(keyEvent.getKeyCode());
			
			if (moveDirection == null)
			{
				return;
			}
			
			int moveScore = grid.slideTiles(moveDirection);

			SlideGameFrame.this.incrementScore(moveScore);
			// TODO: check if game is over
			// TODO: Ensure tiles can be added
			grid.addRandomTile();
			slideGameCanvas.repaint();
		}
	}
}
