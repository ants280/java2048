package com.github.ants280.slideGame.logic;

import java.util.Random;

public class Grid
{
	private static final MoveDirection[] MOVE_DIRECTIONS = MoveDirection.values();
	private int length;
	private Tile[][] rows;
	private Tile[][] cols;
	private final Random random;
	private int goalTileValue;
	private boolean goalTileCreated;

	public Grid()
	{
		this(4);
	}

	public Grid(int length)
	{
		this(length, 2048);
	}

	/**
	 * Creates an empty, square grid of tiles.
	 *
	 * @param length The width and height of rows and columns in the grid.
	 * @param goalTileValue The value of the Tile used to determine when the
	 * game is over.
	 */
	public Grid(int length, int goalTileValue)
	{
		if (length < 2)
		{
			throw new IllegalArgumentException("Length must be large enough "
					+ "to slide tiles.  Found: " + length);
		}
		// copied from https://stackoverflow.com/questions/600293/how-to-check-if-a-number-is-a-power-of-2 :
		if (goalTileValue < 8 || (goalTileValue & (goalTileValue - 1)) != 0)
		{
			throw new IllegalArgumentException("Goal tile value must be a value of 2");
		}

		this.length = length;
		this.rows = createTiles(length);
		this.cols = createTiles(length);
		this.random = new Random();
		this.goalTileValue = goalTileValue;
		this.goalTileCreated = false;

		validateLength(length);
		validateGoalTileValue(goalTileValue);
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		validateLength(length);
		this.length = length;
		this.rows = createTiles(length);
		this.cols = createTiles(length);
		this.goalTileCreated = false;
	}

	public int getGoalTileValue()
	{
		return goalTileValue;
	}

	public void setGoalTileValue(int goalTileValue)
	{
		validateGoalTileValue(goalTileValue);
		this.goalTileValue = goalTileValue;
		this.clear();
	}

	private void validateLength(int length)
	{
		int minimumLength = (int) Math.ceil(Math.sqrt(log2(goalTileValue) - 1));

		if (length < minimumLength)
		{
			throw new IllegalArgumentException(String.format(
					"Length of %d is too small to win with a goalTileValue of %d.  Must be at least %d",
					length, goalTileValue, minimumLength));
		}
	}

	private void validateGoalTileValue(int goalTileValue)
	{
		int maximumGoalTileValue = (int) Math.pow(2, Math.pow(length, 2));

		if (goalTileValue > maximumGoalTileValue)
		{
			throw new IllegalArgumentException(String.format(
					"Goal tile of value of %d is too large to win with a grid length of %d.  Must be at least %d",
					goalTileValue, length, maximumGoalTileValue));
		}
	}

	public Tile getTile(int r, int c)
	{
		return rows[r][c];
	}

	public void clear()
	{
		for (int r = 0; r < length; r++)
		{
			for (int c = 0; c < length; c++)
			{
				setTile(r, c, null);
			}
		}

		goalTileCreated = false;
	}

	/**
	 * Add a random tile to an empty spot on the grid.
	 *
	 * TODO: This may be slow with fairly-filled game boards due to random
	 * adding.
	 */
	public void addRandomTile()
	{
		if (isFilled())
		{
			throw new IllegalArgumentException("Cannot add random tile");
		}

		int r, c;

		Tile tile = random.nextInt(10) == 0 ? Tile.TWO.getNext() : Tile.TWO;
		//tile = Tile.TWO.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext(); // useful for testing game winning :D
		do
		{
			r = random.nextInt(length);
			c = random.nextInt(length);
		}
		while (getTile(r, c) != null);

		setTile(r, c, tile);
	}

	public int slideTiles(MoveDirection moveDirection)
	{
		return slideTiles(
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.RIGHT,
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.UP);
	}

	public boolean canSlideTiles(MoveDirection moveDirection)
	{
		return canSlideTiles(
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.RIGHT,
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.UP);
	}

	public boolean canSlideInAnyDirection()
	{
		for (MoveDirection moveDirection : MOVE_DIRECTIONS)
		{
			if (canSlideTiles(moveDirection))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isFilled()
	{
		for (int r = 0; r < length; r++)
		{
			for (int c = 0; c < length; c++)
			{
				if (getTile(r, c) == null)
				{
					return false;
				}
			}
		}

		return true;
	}

	public boolean goalTileCreated()
	{
		return goalTileCreated;
	}

	private boolean canSlideTiles(boolean slideRows, boolean towardZero)
	{
		for (int i = 0; i < length; i++)
		{
			if (canSlideTiles(slideRows, i, towardZero))
			{
				return true;
			}
		}

		return false;
	}

	private int slideTiles(boolean slideRows, boolean towardZero)
	{
		int sum = 0;

		for (int i = 0; i < length; i++)
		{
			sum += slideTiles(slideRows, i, towardZero);
		}

		return sum;
	}

	/**
	 * Determines if the row or column can be slid in the specified direction.
	 * That is, if sliding the row/column moves/changes the tiles at all.
	 *
	 * @param slideRows Whether or not a row or column is being slid
	 * (consolidated).
	 * @param index The row or column index.
	 * @param towardZero Whether or not to slide the row/column up/left (toward
	 * zero) or down/right (toward the end of the array).
	 * @return The if the row/column can be slid in the specified direction.
	 */
	private boolean canSlideTiles(boolean slideRows, int index, boolean towardZero)
	{
		Tile[] sourceArray = slideRows ? rows[index] : cols[index];
		int slideIndex = towardZero ? 0 : length - 1;
		Tile lastSlidTile = null;
		int delta = towardZero ? 1 : -1;
		for (int i = slideIndex;
				towardZero ? i < length : i >= 0;
				i += delta)
		{
			if (sourceArray[i] != null)
			{
				if (lastSlidTile == sourceArray[i]
						|| i != slideIndex)
				{
					return true;
				}
				else
				{
					lastSlidTile = sourceArray[i];
					slideIndex += delta;
				}
			}
		}

		return false;
	}

	/**
	 * Slide the tiles at the specified index in the specified direction.
	 *
	 * @param slideRows Whether or not a row or column is being slid
	 * (consolidated).
	 * @param index The row or column index.
	 * @param towardZero Whether or not to slide the row/column up/left (toward
	 * zero) or down/right (toward the end of the array).
	 * @return The sum of the newly created, combined tiles.
	 */
	private int slideTiles(boolean slideRows, int index, boolean towardZero)
	{
		int sum = 0;

		Tile[] sourceArray = slideRows ? rows[index] : cols[index];
		Tile[] tempArrayArray = new Tile[length];
		int slideIndex = towardZero ? 0 : length - 1;
		int delta = towardZero ? 1 : -1;
		boolean canCombineWithPreviousSlide = false;
		for (int i = slideIndex;
				towardZero ? i < length : i >= 0;
				i += delta)
		{
			if (sourceArray[i] != null)
			{
				if (canCombineWithPreviousSlide
						&& tempArrayArray[slideIndex - delta] == sourceArray[i])
				{
					Tile nextTile = sourceArray[i].getNext();
					tempArrayArray[slideIndex - delta] = nextTile;
					sum += nextTile.getValue();
					canCombineWithPreviousSlide = false;
					if (nextTile.getValue() == goalTileValue)
					{
						goalTileCreated = true;
					}
				}
				else
				{
					tempArrayArray[slideIndex] = sourceArray[i];
					slideIndex += delta;
					canCombineWithPreviousSlide = true;
				}
			}
		}

		for (int i = 0; i < length; i++)
		{
			setTile(
					slideRows ? index : i,
					slideRows ? i : index,
					tempArrayArray[i]);
		}

		return sum;
	}

	/**
	 * Set the tile at the specified location. Updates the both the rows and
	 * columns to keep the reflective data structure intact.
	 *
	 * Package-private for easy arrangements of tests.
	 */
	void setTile(int r, int c, Tile tile)
	{
		rows[r][c] = tile;
		cols[c][r] = tile;
	}

	private static Tile[][] createTiles(int length)
	{
		Tile[][] tiles = new Tile[length][];

		for (int i = 0; i < length; i++)
		{
			tiles[i] = new Tile[length];
		}

		return tiles;
	}

	private static double log2(int value)
	{
		return Math.log(value) / Math.log(2);
	}

	public static enum MoveDirection
	{
		LEFT, RIGHT, UP, DOWN;
	}
}
