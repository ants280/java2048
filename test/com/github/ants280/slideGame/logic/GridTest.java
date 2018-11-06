package com.github.ants280.slideGame.logic;

import com.github.ants280.slideGame.logic.Grid.MoveDirection;
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
	public void testAddRandomTileOnEmptyBoard()
	{
		// it should not cause an infinite loop
		grid.addRandomTile();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddRandomTileOnUnSlideableBoard()
	{
		grid = new Grid(2);
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_4);
		grid.setTile(1, 0, Tile.V_4);
		grid.setTile(1, 1, Tile.V_2);

		// it should not cause an infinite loop
		grid.addRandomTile();
	}

	@Test
	public void testSlideTilesLeft()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(Tile.V_2, grid.getTile(1, 0));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesRight()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTiles(MoveDirection.RIGHT);

		Assert.assertEquals(Tile.V_2, grid.getTile(1, 3));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesUp()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTiles(MoveDirection.UP);

		Assert.assertEquals(Tile.V_2, grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTilesDown()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.slideTiles(MoveDirection.DOWN);

		Assert.assertEquals(Tile.V_2, grid.getTile(3, 1));
		Assert.assertNull(grid.getTile(1, 1));
	}

	@Test
	public void testSlideTiles_0202()
	{
		grid.setTile(0, 1, Tile.V_2);
		grid.setTile(0, 3, Tile.V_2);

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

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

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(12, moveScore);
		Assert.assertEquals(Tile.V_4, grid.getTile(0, 0));
		Assert.assertEquals(Tile.V_8, grid.getTile(0, 1));
		Assert.assertNull(grid.getTile(0, 2));
		Assert.assertNull(grid.getTile(0, 3));
	}

	@Test
	public void testSlideTiles_22_allRows()
	{
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_2);
		grid.setTile(1, 0, Tile.V_2);
		grid.setTile(1, 1, Tile.V_2);
		grid.setTile(2, 0, Tile.V_2);
		grid.setTile(2, 1, Tile.V_2);
		grid.setTile(3, 0, Tile.V_2);
		grid.setTile(3, 1, Tile.V_2);

		int moveScore = grid.slideTiles(MoveDirection.LEFT);

		Assert.assertEquals(16, moveScore);
	}

	@Test
	public void testSlideTilesDown_16s4s()
	{
		grid.setTile(0, 0, Tile.V_16);
		grid.setTile(1, 0, Tile.V_16);
		grid.setTile(2, 0, Tile.V_4);
		grid.setTile(3, 0, Tile.V_4);

		int moveScore = grid.slideTiles(MoveDirection.DOWN);

		Assert.assertEquals(40, moveScore);
	}

	@Test
	public void testHas2048Tile_empty()
	{
		Assert.assertFalse(grid.has2048Tile());
	}

	@Test
	public void testHas2048Tile_smallTiles()
	{
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_2);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertFalse(grid.has2048Tile());
	}

	@Test
	public void testHas2048Tile_slide1024Tiles()
	{
		grid.setTile(0, 0, Tile.V_1024);
		grid.setTile(0, 1, Tile.V_1024);

		grid.slideTiles(MoveDirection.LEFT);

		Assert.assertTrue(grid.has2048Tile());
	}

	@Test
	public void testCanSlideTilesInAnyDirection_empty()
	{
		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testCanSlideTiles_middle()
	{
		grid.setTile(1, 1, Tile.V_2);

		Assert.assertTrue(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.RIGHT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.UP));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.DOWN));
	}

	@Test
	public void testCanSlideTiles_topLeft()
	{
		grid.setTile(0, 0, Tile.V_2);

		Assert.assertFalse(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.RIGHT));
		Assert.assertFalse(grid.canSlideTiles(MoveDirection.UP));
		Assert.assertTrue(grid.canSlideTiles(MoveDirection.DOWN));
	}

	@Test
	public void testCanSlideTiles_combine()
	{
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_4);
		grid.setTile(0, 2, Tile.V_4);
		grid.setTile(0, 3, Tile.V_8);

		Assert.assertTrue(grid.canSlideTiles(MoveDirection.LEFT));
	}

	@Test
	public void testCanSlideTiles_staggered()
	{
		grid = new Grid(2);
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_4);
		grid.setTile(1, 0, Tile.V_4);
		grid.setTile(1, 1, Tile.V_2);

		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testCanSlideTiles_staggered_oddLength()
	{
		grid = new Grid(3);
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_4);
		grid.setTile(0, 2, Tile.V_8);

		Assert.assertFalse(grid.canSlideTiles(MoveDirection.LEFT));
		Assert.assertFalse(grid.canSlideTiles(MoveDirection.RIGHT));
	}

	@Test
	public void testClear()
	{
		grid.setTile(1, 1, Tile.V_2);

		grid.clear();

		Assert.assertFalse(grid.canSlideInAnyDirection());
	}

	@Test
	public void testClearOnWin()
	{
		grid.setTile(0, 0, Tile.V_1024);
		grid.setTile(0, 1, Tile.V_1024);

		grid.slideTiles(MoveDirection.LEFT);
		grid.clear();

		Assert.assertFalse(grid.has2048Tile());
	}

	@Test
	public void testStall()
	{
		grid.setTile(0, 0, Tile.V_2);
		grid.setTile(0, 1, Tile.V_2);
		grid.setTile(0, 2, Tile.V_4);
		grid.setTile(0, 3, Tile.V_2);
		grid.setTile(1, 0, Tile.V_8);
		grid.setTile(1, 1, Tile.V_64);
		grid.setTile(1, 2, Tile.V_8);
		grid.setTile(1, 3, Tile.V_16);
		grid.setTile(2, 0, Tile.V_4);
		grid.setTile(2, 1, Tile.V_2);
		grid.setTile(2, 2, Tile.V_32);
		grid.setTile(2, 3, Tile.V_8);
		grid.setTile(3, 0, Tile.V_2);
		grid.setTile(3, 1, Tile.V_2);
		grid.setTile(3, 2, Tile.V_4);
		grid.setTile(3, 3, Tile.V_2);

		Assert.assertTrue(grid.isFilled());
	}
}
