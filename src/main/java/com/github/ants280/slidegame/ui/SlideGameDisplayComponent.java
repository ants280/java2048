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
	private double cellSize;

	public SlideGameDisplayComponent(Grid grid)
	{
		super();

		this.grid = grid;

		this.init();
	}

	private void init()
	{
		this.setFont(new Font("times", Font.PLAIN, 12));
		this.xOffset = 0;
		this.yOffset = 0;
		this.cellSize = 50d;
		this.addComponentListener(
				new SlideGameComponentListener(
						componentEvent -> this.componentResized()));
	}

	private void componentResized()
	{
		int width = this.getWidth();
		int height = this.getHeight();
		int minDimension = Math.min(width, height);
		double newCellSize = minDimension / (grid.getLength() + 0d);

		if (cellSize != newCellSize)
		{
			xOffset = (width - minDimension) / 2d;
			yOffset = (height - minDimension) / 2d;
			cellSize = newCellSize;

			this.setFont(this.getFont().deriveFont((float) (cellSize / 3d)));
			this.repaint();
		}
	}

	//<editor-fold defaultstate="collapsed" desc="painting">
	@Override
	public void paintComponent(Graphics g)
	{
		((Graphics2D) g).setRenderingHints(ANTIALIAS_ON_RENDERING_HINT);

		double tileSize = cellSize * 0.90d;
		double spacerSize = cellSize - tileSize;
		double halfSpacerSize = spacerSize / 2d;

		this.paintGrid(
				g,
				spacerSize,
				halfSpacerSize);
		this.paintTiles(
				g,
				tileSize,
				halfSpacerSize);
	}

	private void paintGrid(
			Graphics g,
			double spacerSize,
			double halfSpacerSize)
	{
		g.setColor(SlideGameColors.SPACER_COLOR);

		int gridLengthPx = round(grid.getLength() * cellSize);
		int roundedSpacerSize = round(spacerSize);
		for (int i = 0; i <= grid.getLength(); i++)
		{
			double lineOffset = (i * cellSize) - halfSpacerSize;
			// vertical lines:
			g.fillRect(
					round(xOffset + lineOffset),
					round(yOffset),
					roundedSpacerSize,
					gridLengthPx);
			// horizontal lines:
			g.fillRect(
					round(xOffset),
					round(yOffset + lineOffset),
					gridLengthPx,
					roundedSpacerSize);
		}
	}

	private void paintTiles(
			Graphics g,
			double tileSize,
			double halfSpacerSize)
	{
		for (int c = 0; c < grid.getLength(); c++)
		{
			for (int r = 0; r < grid.getLength(); r++)
			{
				this.paintTile(
						g,
						c,
						r,
						tileSize,
						halfSpacerSize);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int c,
			int r,
			double tileSize,
			double halfSpacerSize)
	{
		Tile tile = grid.getTile(c, r);

		this.paintTileBackground(
				tile == null
						? SlideGameColors.EMPTY_TILE_COLOR
						: SlideGameColors.getColor(tile),
				g,
				c,
				r,
				tileSize,
				halfSpacerSize);

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
			int r,
			double tileSize,
			double halfSpacerSize)
	{
		g.setColor(tileColor);

		int x = round(xOffset + (c * cellSize) + halfSpacerSize);
		int y = round(yOffset + (r * cellSize) + halfSpacerSize);
		int width = round(tileSize);
		int height = round(tileSize);
		g.fillRect(x, y, width, height);
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
