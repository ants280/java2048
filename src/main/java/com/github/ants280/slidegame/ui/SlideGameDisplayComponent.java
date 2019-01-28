package com.github.ants280.slidegame.ui;

import com.github.ants280.slidegame.logic.Grid;
import com.github.ants280.slidegame.logic.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public class SlideGameDisplayComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	private final transient Grid grid;
	private static final RenderingHints ANTIALIAS_ON_RENDERING_HINT
			= new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
	private double xOffset;
	private double yOffset;
	private int cellSize;
	private int tileSize;
	private int spacerSize;
	private double halfSpacerSize;

	public SlideGameDisplayComponent(Grid grid)
	{
		super();

		this.grid = grid;

		this.init();
	}

	private void init()
	{
		this.setFont(new Font("times", Font.PLAIN, 12));
		this.addComponentListener(
				new SlideGameComponentListener(
						componentEvent -> this.componentResized()));
	}

	private void componentResized()
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int minDimension = Math.min(width, height);
		int newCellSize = round(minDimension / (grid.getLength() + 0d));

		if (cellSize != newCellSize)
		{
			xOffset = (width - minDimension) / 2d;
			yOffset = (height - minDimension) / 2d;
			cellSize = newCellSize;
			tileSize = round(cellSize * 0.90d);
			spacerSize = cellSize - tileSize;
			halfSpacerSize = spacerSize / 2d;

			this.setFont(this.getFont().deriveFont((float) (cellSize / 3d)));
			this.repaint();
		}
	}

	//<editor-fold defaultstate="collapsed" desc="painting">
	@Override
	public void paintComponent(Graphics g)
	{
		((Graphics2D) g).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);

		this.paintGrid(g);
		this.paintTiles(g);
	}

	private void paintGrid(Graphics g)
	{
		g.setColor(SlideGameColors.SPACER_COLOR);

		int gridLengthPx = grid.getLength() * cellSize;
		for (int i = 0; i <= grid.getLength(); i++)
		{
			double lineOffset = (i * cellSize) - halfSpacerSize;
			// vertical lines:
			g.fillRect(
					round(xOffset + lineOffset),
					round(yOffset),
					spacerSize,
					gridLengthPx);
			// horizontal lines:
			g.fillRect(
					round(xOffset),
					round(yOffset + lineOffset),
					gridLengthPx,
					spacerSize);
		}
	}

	private void paintTiles(Graphics g)
	{
		for (int c = 0; c < grid.getLength(); c++)
		{
			for (int r = 0; r < grid.getLength(); r++)
			{
				this.paintTile(
						g,
						c,
						r);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int c,
			int r)
	{
		Tile tile = grid.getTile(c, r);

		this.paintTileBackground(
				tile == null
						? SlideGameColors.EMPTY_TILE_COLOR
						: SlideGameColors.getColor(tile),
				g,
				c,
				r);

		if (tile != null)
		{
			this.paintTileText(
					tile.getDisplayValue(),
					g,
					c,
					r);
		}
	}

	private void paintTileBackground(
			Color tileColor,
			Graphics g,
			int c,
			int r)
	{
		g.setColor(tileColor);

		int x = round(xOffset + (c * cellSize) + halfSpacerSize);
		int y = round(yOffset + (r * cellSize) + halfSpacerSize);
		g.fillRect(x, y, tileSize, tileSize);
	}

	private void paintTileText(
			String tileText,
			Graphics g,
			int c,
			int r)
	{
		g.setColor(SlideGameColors.TILE_TEXT_COLOR);

		int textWidth = g.getFontMetrics().stringWidth(tileText);
		double fontHeight = g.getFont().getSize2D() * 0.75d;
		int x = round(xOffset + ((c + 0.5d) * cellSize - textWidth / 2d));
		int y = round(yOffset + ((r + 0.5d) * cellSize + fontHeight / 2d));
		g.drawString(tileText, x, y);
	}

	private static int round(double value)
	{
		return (int) (value + 0.5d);
	}
	//</editor-fold>
}
