package com.github.ants280.slideGame.logic;

import java.util.Random;
import java.util.stream.IntStream;

public class Grid
{
	private static final boolean PRINT_MOVES = false;
	private final boolean debug;
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
		this(length, false);
	}

	/**
	 * Test constructor (package-private). Enables debuging.
	 *
	 * @param length The width and height of rows and columns in the grid.
	 */
	Grid(int length, boolean debug)
	{
		if (length < 2)
		{
			throw new IllegalArgumentException("Length must be large enough "
					+ "to slide tiles.  Found: " + length);
		}

		this.debug = debug;
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
		while (getTile(r, c) == null);

		if (PRINT_MOVES)
		{
			System.out.printf("\tAdding %s Tile at [%d,%d]%n", tile, r, c);
		}

		setTile(r, c, tile);
	}

	public int slideTilesLeft()
	{
		if (PRINT_MOVES)
		{
			System.out.println("\tLEFT");
		}

		return slideRows(true);
	}

	public int slideTilesRight()
	{
		if (PRINT_MOVES)
		{
			System.out.println("\tRIGHT");
		}

		return slideRows(false);
	}

	public int slideTilesUp()
	{
		if (PRINT_MOVES)
		{
			System.out.println("\tUP");
		}

		return slideCols(true);
	}

	public int slideTilesDown()
	{
		if (PRINT_MOVES)
		{
			System.out.println("\tDOWN");
		}

		return slideCols(false);
	}

	private int slideRows(boolean towardZero)
	{
		return slideTiles(true, towardZero);
	}

	private int slideCols(boolean towardZero)
	{
		return slideTiles(true, towardZero);
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

		Tile[] targetArray = slideRows ? rows[index] : cols[index];
		Tile[] slideArray = new Tile[length];
		int slideIndex = towardZero ? 0 : length - 1;
		for (int i = towardZero ? length - 1 : 0;
				towardZero ? i >= 0 : i + 1 < length;
				i += towardZero ? -1 : 1)
		{
			if (targetArray[i] != null)
			{
				if (slideIndex > 0
						&& slideArray[slideIndex - 1] == targetArray[i])
				{
					Tile nextTile = targetArray[i].getNext();
//					targetArray[slideIndex] = nextTile;
//					otherMatrix[slideIndex][index] = nextTile;
					slideArray[slideIndex - 1] = nextTile;
					sum += nextTile.getValue();
				}
				else if (slideIndex != i)
				{
//					targetArray[slideIndex] = targetArray[i];
//					otherMatrix[slideIndex][index] = otherMatrix[i][index];
					slideArray[slideIndex] = targetArray[i];
					slideIndex += towardZero ? 1 : -1;
				}
			}
		}

		for (int i = towardZero ? length - 1 : 0;
				towardZero ? i >= 0 : i + 1 < length;
				i += towardZero ? -1 : 1)
		{
			setTile(
					slideRows ? index : i,
					slideRows ? i : index,
					slideArray[i]);
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
}
