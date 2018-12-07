package com.github.ants280.slidegame.logic;

import java.util.HashMap;
import java.util.Map;

public class Tile implements Comparable<Tile>
{
	private final Integer value;
	private final String displayValue;
	private static final Map<Integer, Tile> VALUE_CACHE = new HashMap<>();
	public static final Tile TWO = new Tile(2);

	static
	{
		VALUE_CACHE.put(2, TWO);
	}

	private Tile(Integer value)
	{
		this.value = value;
		this.displayValue = value.toString();
	}

	public Integer getValue()
	{
		return value;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}

	public Tile getNext()
	{
		int nextValue = Math.multiplyExact(2, value);

		if (!VALUE_CACHE.containsKey(nextValue))
		{
			VALUE_CACHE.put(nextValue, new Tile(nextValue));
		}

		return VALUE_CACHE.get(nextValue);
	}

	@Override
	public int hashCode()
	{
		return this.value;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| (this.getClass() == obj.getClass()
				&& this.value.equals(((Tile) obj).getValue()));
	}

	@Override
	public int compareTo(Tile o)
	{
		return value.compareTo(o.getValue());
	}

	@Override
	public String toString()
	{
		return String.format("Tile{%d}", value);
	}
}
