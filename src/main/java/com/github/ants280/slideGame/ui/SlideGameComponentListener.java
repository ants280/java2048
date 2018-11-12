package com.github.ants280.slideGame.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class SlideGameComponentListener extends ComponentAdapter
{
	private final Consumer<ComponentEvent> componentResizedConsumer;

	public SlideGameComponentListener(
			Consumer<ComponentEvent> componentResizedConsumer)
	{
		this.componentResizedConsumer = componentResizedConsumer;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		componentResizedConsumer.accept(e);
	}
}
