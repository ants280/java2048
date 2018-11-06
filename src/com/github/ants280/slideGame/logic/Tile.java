package com.github.ants280.slideGame.logic;

/**
 * The various tiles of the slideGame. Colors copied from .tile {
 * .tile.tile-[//d+] .tile-inner background } See {
 *
 * @https://github.com/gabrielecirulli/2048/blob/master/style/main.css}.
 */
public enum Tile
{
	V_2(0xeee4da),
	V_4(0xede0c8),
	V_8(0xf2b179),
	V_16(0xf59563),
	V_32(0xf67c5f),
	V_64(0xf65e3b),
	V_128(0xedcf72),
	V_256(0xedcc61),
	V_512(0xedc850),
	V_1024(0xedc53f),
	V_2048(0xedc22e);

	private final int value;
	private final String displayValue;
	private final int color;
	private static final Tile[] VALUES = values();

	private Tile(int color)
	{
		if (color < 0 || color > 0xffffff)
		{
			throw new IllegalArgumentException("Too small/large: " + color);
		}

		this.value = (int) Math.pow(2, ordinal() + 1);
		this.displayValue = Integer.toString(value);
		this.color = color;
	}

	public int getValue()
	{
		return value;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}

	public int getColor()
	{
		return color;
	}

	public Tile getNext()
	{
		int index = ordinal();
		
		return (index + 1 == VALUES.length) ? this : VALUES[index + 1];
	}
}
