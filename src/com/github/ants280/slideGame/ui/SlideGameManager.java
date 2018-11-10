package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SlideGameManager
{
	private final Grid grid;
	private final JFrame slideGameRootComponent;
	private final JComponent slideGameCanvas;
	private final JLabel gameOverLabel;
	private final JLabel scoreLabel;
	private final JLabel highScoreLabel;
	private final KeyListener keyListener;
	private final MouseListener mouseListener;
	private int score;
	private int highScore;
	private boolean gameOver;
	private boolean gameWon;
	private boolean listenersAdded;
	private MouseEvent mousePressedLocation;

	private static final Map<Integer, Grid.MoveDirection> MOVE_DIRECTIONS = new HashMap<>();

	static
	{
		Stream.of(KeyEvent.VK_W, KeyEvent.VK_UP)
				.forEach(keyCode -> MOVE_DIRECTIONS.put(keyCode, Grid.MoveDirection.UP));
		Stream.of(KeyEvent.VK_A, KeyEvent.VK_LEFT)
				.forEach(keyCode -> MOVE_DIRECTIONS.put(keyCode, Grid.MoveDirection.LEFT));
		Stream.of(KeyEvent.VK_S, KeyEvent.VK_DOWN)
				.forEach(keyCode -> MOVE_DIRECTIONS.put(keyCode, Grid.MoveDirection.DOWN));
		Stream.of(KeyEvent.VK_D, KeyEvent.VK_RIGHT)
				.forEach(keyCode -> MOVE_DIRECTIONS.put(keyCode, Grid.MoveDirection.RIGHT));
	}

	public SlideGameManager(
			Grid grid,
			JFrame slideGameRootComponent,
			JComponent slideGameCanvas,
			JLabel gameOverLabel,
			JLabel scoreLabel,
			JLabel highScoreLabel)
	{
		this.grid = grid;
		this.slideGameRootComponent = slideGameRootComponent;
		this.slideGameCanvas = slideGameCanvas;
		this.gameOverLabel = gameOverLabel;
		this.scoreLabel = scoreLabel;
		this.highScoreLabel = highScoreLabel;
		this.keyListener = new SlideGameKeyListener(this::keyReleased);
		this.mouseListener = new SlideGameMouseListener(this::mousePressed, this::mouseReleased);
		this.score = 0;
		this.highScore = 0;
		this.gameOver = false;
		this.gameWon = false;
		this.listenersAdded = false;

		initGame();
	}

	public int getGridLength()
	{
		return grid.getLength();
	}

	public void setGridLength(int length)
	{
		grid.setLength(length);
		newGame();
	}

	public int getGoalTileValue()
	{
		return grid.getGoalTileValue();
	}

	public void setGoalTileValue(int goalTileValue)
	{
		grid.setGoalTileValue(goalTileValue);
		newGame();
	}

	public void makeMove(Grid.MoveDirection moveDirection)
	{
		if (moveDirection == null || !grid.canSlideTiles(moveDirection))
		{
			return;
		}

		int moveScore = grid.slideTiles(moveDirection);

		this.incrementScore(moveScore);

		if (!grid.canSlideInAnyDirection() || grid.goalTileCreated())
		{
			gameWon = grid.goalTileCreated();
			endGame();
		}
		else
		{
			grid.addRandomTile();

			if (!grid.canSlideInAnyDirection())
			{
				endGame();
			}
		}

		slideGameCanvas.repaint();
	}

	private void initGame()
	{
		gameOver = false;
		gameWon = false;
		score = 0;
		grid.addRandomTile();
		grid.addRandomTile();
		addListeners();
		updateScoreLabels();
	}

	public void newGame()
	{
		grid.clear();
		initGame();
		slideGameCanvas.repaint();
	}

	private void endGame()
	{
		gameOver = true;
		removeListeners();
		updateScoreLabels();
	}

	private void addListeners()
	{
		if (!listenersAdded)
		{
			slideGameRootComponent.addKeyListener(keyListener);
			slideGameCanvas.addMouseListener(mouseListener);
			mousePressedLocation = null;
			listenersAdded = true;
		}
	}

	private void removeListeners()
	{
		slideGameRootComponent.removeKeyListener(keyListener);
		slideGameCanvas.addMouseListener(mouseListener);
		mousePressedLocation = null;
		listenersAdded = false;
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
		gameOverLabel.setText(gameOver ? (gameWon ? "You Win!" : "You Lose.") : "");
		scoreLabel.setText("SCORE: " + score);
		highScoreLabel.setText("BEST: " + highScore);
	}

	private void keyReleased(KeyEvent e)
	{
		makeMove(MOVE_DIRECTIONS.get(e.getKeyCode()));
	}

	private void mousePressed(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1)
		{
			return;
		}

		mousePressedLocation = e;
	}

	private void mouseReleased(MouseEvent e)
	{
		if (e.getButton() != MouseEvent.BUTTON1 || mousePressedLocation == null)
		{
			return;
		}

		int deltaX = e.getX() - mousePressedLocation.getX();
		int deltaY = e.getY() - mousePressedLocation.getY();
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);

		if (absDeltaX > absDeltaY)
		{
			if (deltaX < 0)
			{
				makeMove(Grid.MoveDirection.LEFT);
			}
			else if (deltaX > 0)
			{
				makeMove(Grid.MoveDirection.RIGHT);
			}
		}
		else if (absDeltaY > absDeltaX)
		{
			if (deltaY < 0)
			{
				makeMove(Grid.MoveDirection.UP);
			}
			else if (deltaY > 0)
			{
				makeMove(Grid.MoveDirection.DOWN);
			}
		}
	}
}
