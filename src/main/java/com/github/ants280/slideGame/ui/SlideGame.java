package com.github.ants280.slideGame.ui;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SlideGame implements Runnable
{
	public static void main(final String[] args)
	{
		// Set the menu of the Frame on the mac menu.
		if (System.getProperty("mrj.version") != null)
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		SwingUtilities.invokeLater(new SlideGame());
	}

	private SlideGame()
	{
	}

	@Override
	public void run()
	{
		setLookAndFeel();

		Window frame = new SlideGameFrame();
		Thread.setDefaultUncaughtExceptionHandler(
				new SlideGameUncaughtExceptionHandler(frame));

		// Center the Window on the screen.
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException
				| InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
}
