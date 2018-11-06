package com.github.ants280.slideGame.logic;

import java.util.Random;
import java.util.stream.IntStream;

public class Grid
{
	private static final boolean DEBUG = false;
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

		if (DEBUG)
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

		if (DEBUG)
		{
			System.out.printf("\tAdding %s Tile at [%d,%d]%n", tile, r, c);
		}

		setTile(r, c, tile);
	}

	public int slideTilesLeft()
	{
		if (DEBUG)
		{
			System.out.println("\tLEFT");
		}

		return slideRows(true);
	}

	public int slideTilesRight()
	{
		if (DEBUG)
		{
			System.out.println("\tRIGHT");
		}

		return slideRows(false);
	}

	public int slideTilesUp()
	{
		if (DEBUG)
		{
			System.out.println("\tUP");
		}

		return slideCols(true);
	}

	public int slideTilesDown()
	{
		if (DEBUG)
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
		Tile[] slidTiles = new Tile[length];
		
		int sum = 0;
		
		for (int i = 0; i + 1 < length; i++)
		{
			//TODO
		}

		return sum;
	}

	// package-private for testing
	void setTile(int r, int c, Tile tile)
	{
		if (DEBUG)
		{
			System.out.printf("Setting tile at [%d,%d] to %s%n", r, c, tile);
		}

		rows[r][c] = tile;
		cols[c][r] = tile;
	}

	public Tile getTile(int r, int c)
	{
		if (DEBUG)
		{
			if (rows[r][c] != cols[c][r])
			{
				System.err.printf(
						"ERROR: Same tile should be saved in rotated data "
						+ "structure at r=%d,c=%d.  Found %s and %s.%n",
						r, c, rows[r][c], cols[c][r]);
			}
		}

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
