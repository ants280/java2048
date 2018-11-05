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
}
