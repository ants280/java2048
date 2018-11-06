package com.github.ants280.slideGame.logic;

import java.util.Random;
import java.util.stream.IntStream;

public class Grid
{
	private static final boolean PRINT_MOVES = false;
	private final int length;
	private final Tile[][] rows;
	private final Tile[][] cols;
	private final Random random;

	/**
	 * Creates an empty, square grid of tiles.
	 *
	 * @param length The width and height of rows and columns in the grid.
	 */
	public Grid(int length)
	{
		if (length < 2)
		{
			throw new IllegalArgumentException("Length must be large enough "
					+ "to slide tiles.  Found: " + length);
		}

		this.length = length;
		this.rows = createTiles(length);
		this.cols = createTiles(length);
		long seed = System.currentTimeMillis();
		this.random = new Random(seed);

		if (PRINT_MOVES)
		{
			System.out.printf(
					"Created grid with length %d and seed %d%n", length, seed);
		}
	}

	public int getLength()
	{
		return length;
	}

	public Tile getTile(int r, int c)
	{
		return rows[r][c];
	}

	/**
	 * Add a random tile to an empty spot on the grid.
	 */
	public void addRandomTile()
	{
		// This may be slow with large game lengths.
		// TODO: this will infinite-loop if the grid is filled, so it is buggy.
		int r, c;

		Tile tile = random.nextInt(10) == 0 ? Tile.V_4 : Tile.V_2;
		do
		{
			r = random.nextInt(length);
			c = random.nextInt(length);
		}
		while (getTile(r, c) != null);

		if (PRINT_MOVES)
		{
			System.out.printf("\tAdding %s Tile at [%d,%d]%n", tile, r, c);
		}

		setTile(r, c, tile);
	}

	public int slideTiles(MoveDirection moveDirection)
	{
		return slideTiles(
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.RIGHT,
				moveDirection == MoveDirection.LEFT || moveDirection == MoveDirection.UP);
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
		boolean canCombineWithPreviousSlide = true;
		for (int i = slideIndex;
				towardZero ? i < length : i >= 0;
				i += delta)
		{
			if (sourceArray[i] != null)
			{
				if (slideIndex > 0
						&& canCombineWithPreviousSlide
						&& tempArrayArray[slideIndex - 1] == sourceArray[i])
				{
					Tile nextTile = sourceArray[i].getNext();
					tempArrayArray[slideIndex - 1] = nextTile;
					sum += nextTile.getValue();
					canCombineWithPreviousSlide = false;
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
		return IntStream.range(0, length)
				.map(i -> length)
				.mapToObj(Tile[]::new)
				.toArray(Tile[][]::new);
	}
	
	public static enum MoveDirection
	{
		LEFT, RIGHT, UP, DOWN;
	}
}
