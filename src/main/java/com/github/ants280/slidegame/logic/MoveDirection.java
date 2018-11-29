package com.github.ants280.slidegame.logic;

public enum MoveDirection
{
	LEFT("Left"),
	RIGHT("Right"),
	UP("Up"),
	DOWN("Down");

	private final String displayValue;

	private MoveDirection(String displayValue)
	{
		this.displayValue = displayValue;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}
}
