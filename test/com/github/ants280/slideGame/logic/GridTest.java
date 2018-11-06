package com.github.ants280.slideGame.logic;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class GridTest
{
	private static final int LENGTH = 4;
	private Grid grid;

	@Before
	public void setUp()
	{
		grid = new Grid(LENGTH);
	}

	@Test
	public void testInitialGridEmpty()
	{
		for (int r = 0; r < LENGTH; r++)
		{
			for (int c = 0; c < LENGTH; c++)
			{
				Assert.assertNull(
						String.format("Non-null tile found at [%d,%d]", r, c),
						grid.getTile(r, c));
			}
		}
	}

	@Test
	public void testSlideTilesLeft()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTilesLeft();

		Assert.assertEquals(Tile.V_2, grid.getTile(1, 0));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesRight()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTilesRight();

		Assert.assertEquals(Tile.V_2, grid.getTile(1, 3));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesUp()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTilesUp();

		Assert.assertEquals(Tile.V_2, grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesDown()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTilesDown();

		Assert.assertEquals(Tile.V_2, grid.getTile(3, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTiles_0202()
	{
		grid.setTile(0, 1, Tile.V_2);
		grid.setTile(0, 3, Tile.V_2);

		int moveScore = grid.slideTilesLeft();

		Assert.assertEquals(4, moveScore);
		Assert.assertEquals(Tile.V_4, grid.getTile(0, 0));
		Assert.assertNull(grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(0, 3));
	}

	@Test
	public void testSlideTiles_2244()
	{
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_2);
		grid.setTile(0, 2, Tile.V_4);
		grid.setTile(0, 3, Tile.V_4);

		int moveScore = grid.slideTilesLeft();

		Assert.assertEquals(12, moveScore);
		Assert.assertEquals(Tile.V_4, grid.getTile(0, 0));
		Assert.assertEquals(Tile.V_8, grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(0, 2));
		Assert.assertNull(grid.getTile(0, 3));
	}
}
