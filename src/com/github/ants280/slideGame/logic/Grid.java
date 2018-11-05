package com.github.ants280.slideGame.logic;

import java.util.stream.IntStream;

public class Grid
{
	private final int length;
	private final Tile[][] rows;
	private final Tile[][] cols;

	public Grid(int length)
	{
		this.length = length;
		this.rows = createTiles(length);
		this.cols = createTiles(length);
	}

	private static Tile[][] createTiles(int length)
	{
		return IntStream.range(0, length)
				.map(i -> length)
				.mapToObj(Tile[]::new)
				.toArray(Tile[][]::new);
	}

	public boolean slideTilesLeft()
	{
		return slideRows(true);
	}

	public boolean slideTilesRight()
	{
		return slideRows(false);
	}

	public boolean slideTilesUp()
	{
		return slideCols(true);
	}

	public boolean slideTilesDown()
	{
		return slideCols(false);
	}

	boolean slideRows(boolean slideTowardStart)
	{
		return slideTiles(true, slideTowardStart);
	}

	boolean slideCols(boolean slideTowardStart)
	{
		return slideTiles(true, slideTowardStart);
	}

	private boolean slideTiles(boolean slideRows, boolean slideTowardStart)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
