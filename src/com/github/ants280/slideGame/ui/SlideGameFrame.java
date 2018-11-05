package com.github.ants280.slideGame.ui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class SlideGameFrame extends JFrame
{
	public SlideGameFrame()
	{
		super("Slide Game");

		initSize();
	}

	private void initSize()
	{
		this.setMinimumSize(new Dimension(400, 100));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
