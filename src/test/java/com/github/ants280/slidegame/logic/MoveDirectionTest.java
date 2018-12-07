package com.github.ants280.slidegame.logic;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class MoveDirectionTest
{
	@Test
	public void testGetDisplayValue_distinct()
	{
		MoveDirection[] values = MoveDirection.values();

		String[] distinctMoveDirectionDisplayValues
				= Arrays.stream(values)
						.map(MoveDirection::getDisplayValue)
						.distinct()
						.toArray(String[]::new);

		Assert.assertEquals(
				values.length,
				distinctMoveDirectionDisplayValues.length);
	}
}
