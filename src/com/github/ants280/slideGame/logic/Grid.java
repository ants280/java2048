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
			sum += slideTiles(i, slideRows, towardZero);
		}

		return sum;
	}

	private int slideTiles(int index, boolean slideRows, boolean towardZero)
	{
		int sum = 0;

		Tile[] targetArray = slideRows ? rows[index] : cols[index];
		Tile[][] otherMatrix = slideRows ? cols : rows;
		int slideIndex = towardZero ? length - 1 : 0;
		for (int i = towardZero ? length - 1 : 0;
				towardZero ? i >= 0 : i + 1 < length;
				i += towardZero ? -1 : 1)
		{
			if (targetArray[i] != null)
			{
				if (slideIndex > 0 && targetArray[slideIndex] == targetArray[i])
				{
					Tile nextTile = targetArray[slideIndex].getNext();
					targetArray[slideIndex] = nextTile;
					otherMatrix[slideIndex][index] = nextTile;
					sum += nextTile.getValue();
				}
				else if (slideIndex != i)
				{
					targetArray[slideIndex] = targetArray[i];
					otherMatrix[slideIndex][index] = otherMatrix[i][index];
					slideIndex += towardZero ? -1 : 1;
				}
			}
		}

		if (debug)
		{
			// ensure the data structure is not comprimised
			for (int r = 0; r < length; r++)
			{
				for (int c = 0; c < length; c++)
				{
					if (rows[c][c] != cols[c][c])
					{
						throw new RuntimeException(String.format(
								"ERROR: Same tile should be saved in rotated "
										+ "data structure at [r=%d,c=%d].  "
										+ "Found %s and %s.%n",
								c, c, rows[c][c], cols[c][c]));
					}
				}
			}
		}

		return sum;
	}

	// package-private for testing
	void setTile(int r, int c, Tile tile
	)
	{
		rows[r][c] = tile;
		cols[c][r] = tile;
	}

	public Tile getTile(int r, int c)
	{
		return rows[r][c];
	}

	private static Tile[][] createTiles(int length)
	{
		return IntStream.range(0, length)
				.map(i -> length)
				.mapToObj(Tile[]::new)
				.toArray(Tile[][]::new);
	}
}
