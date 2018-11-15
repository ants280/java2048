package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.MoveDirection;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class SlideGameManager
{
	private final Grid grid;
	private final JFrame slideGameRootComponent;
	private final JComponent slideGameCanvas;
	private final JLabel gameOverLabel;
	private final JLabel scoreLabel;
	private final JLabel highScoreLabel;
	private final JLabel goalLabel;
	private final JLabel moveLabel;
	private final Timer moveLabelClearingTimer;
	private final KeyListener keyListener;
	private final MouseListener mouseListener;
	private int score;
	private int highScore;
	private boolean gameOver;
	private boolean gameWon;
	private boolean listenersAdded;
	private MouseEvent mousePressedLocation;
	private static final Map<Integer, MoveDirection> MOVE_DIRECTIONS
			= new HashMap<>();

	static
	{
		putMoveDirectionCodes(MoveDirection.UP, KeyEvent.VK_W, KeyEvent.VK_UP);
		putMoveDirectionCodes(MoveDirection.LEFT, KeyEvent.VK_A, KeyEvent.VK_LEFT);
		putMoveDirectionCodes(MoveDirection.DOWN, KeyEvent.VK_S, KeyEvent.VK_DOWN);
		putMoveDirectionCodes(MoveDirection.RIGHT, KeyEvent.VK_D, KeyEvent.VK_RIGHT);
	}

	public SlideGameManager(
			Grid grid,
			JFrame slideGameRootComponent,
			JComponent slideGameCanvas,
			JLabel gameOverLabel,
			JLabel scoreLabel,
			JLabel highScoreLabel,
			JLabel goalLabel,
			JLabel moveLabel)
	{
		this.grid = grid;
		this.slideGameRootComponent = slideGameRootComponent;
		this.slideGameCanvas = slideGameCanvas;
		this.gameOverLabel = gameOverLabel;
		this.scoreLabel = scoreLabel;
		this.highScoreLabel = highScoreLabel;
		this.goalLabel = goalLabel;
		this.moveLabel = moveLabel;
		this.moveLabelClearingTimer = new Timer(
				(int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS),
				actionEvent -> this.clearMoveLabel());
		this.keyListener = new SlideGameKeyListener(
				this::keyReleased);
		this.mouseListener = new SlideGameMouseListener(
				this::mousePressed,
				this::mouseReleased);
		this.score = 0;
		this.highScore = 0;
		this.gameOver = false;
		this.gameWon = false;
		this.listenersAdded = false;

		moveLabelClearingTimer.setRepeats(false);
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
		updateGoalLabel();
	}

	public void makeMove(MoveDirection moveDirection)
	{
		boolean validMove = moveDirection != null
				&& grid.canSlideTiles(moveDirection);

		if (validMove)
		{

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

		updateMoveLabel(moveDirection, validMove);
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
		updateGoalLabel();
		clearMoveLabel();
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
		gameOverLabel.setText(
				gameOver ? (gameWon ? "You Win!" : "You Lose.") : "");
		scoreLabel.setText("SCORE: " + score);
		highScoreLabel.setText("BEST: " + highScore);
	}

	private void updateGoalLabel()
	{
		goalLabel.setText(String.format(
				"Goal: Create %d tile",
				grid.getGoalTileValue()));
	}

	private void updateMoveLabel(
			MoveDirection moveDirection,
			boolean validMove)
	{
		if (moveDirection != null)
		{
			moveLabel.setForeground(validMove ? Color.BLACK : Color.RED);
			moveLabel.setText(String.format(
					(validMove ? "Moved %s" : "Cannot move %s"),
					moveDirection.getDisplayValue()));
			moveLabelClearingTimer.stop();
			moveLabelClearingTimer.start();
		}
	}

	private void clearMoveLabel()
	{
		moveLabelClearingTimer.stop();
		moveLabel.setText("");
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
		if (e.getButton() != MouseEvent.BUTTON1
				|| mousePressedLocation == null)
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
				makeMove(MoveDirection.LEFT);
			}
			else if (deltaX > 0)
			{
				makeMove(MoveDirection.RIGHT);
			}
		}
		else if (absDeltaY > absDeltaX)
		{
			if (deltaY < 0)
			{
				makeMove(MoveDirection.UP);
			}
			else if (deltaY > 0)
			{
				makeMove(MoveDirection.DOWN);
			}
		}
	}

	private static void putMoveDirectionCodes(
			MoveDirection moveDirection,
			int... keyCodes)
	{
		Arrays.stream(keyCodes)
				.forEach(keyCode
						-> MOVE_DIRECTIONS.put(keyCode, moveDirection));
	}
}
