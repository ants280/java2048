package com.github.ants280.slidegame.logic;

import org.junit.Assert;
import org.junit.Test;

public class TileTest
{

	@Test
	public void testGetDisplayValue_distinct()
	{
		//TODO
//		MoveDirection[] values = MoveDirection.values();
//
//		String[] distinctMoveDirectionDisplayValues
//				= Arrays.stream(values)
//						.map(MoveDirection::getDisplayValue)
//						.distinct()
//						.toArray(String[]::new);
//
//		Assert.assertEquals(
//				values.length,
//				distinctMoveDirectionDisplayValues.length);
	}

	@Test
	public void testHashCode()
	{
		//TODO
	}

	@Test
	public void testEquals()
	{
		//TODO
	}

	@Test
	public void testCompareTo_eq()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		int compareTo = tile1.compareTo(tile2);

		Assert.assertEquals(0, compareTo);
	}

	@Test
	public void testCompareTo_lt()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		int compareTo = tile1.compareTo(tile2);

		Assert.assertTrue(compareTo < 0);
	}

	@Test
	public void testCompareTo_gt()
	{
		Tile tile1 = Tile.TWO;
		Tile tile2 = tile1;

		int compareTo = tile2.compareTo(tile1);

		Assert.assertTrue(compareTo < 0);
	}

	@Test
	public void testToString()
	{
		//TODO
	}

}
