package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;

public class SlideGameCanvas extends JComponent
{
	private final Grid grid;
	private final Font TILE_FONT = new Font("times", Font.PLAIN, 96); // TODO: make font scale

	public SlideGameCanvas(Grid grid)
	{
		super();

		this.grid = grid;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		// TODO: Ensure width = height.
		int width = this.getWidth();
		int height = this.getHeight();
		int gridLength = grid.getLength();
		double tilePercentage = 0.90d;
		double colWidth = width / gridLength;
		double rowHeight = height / gridLength;
		// if the tile width/height are not rounded to integers, the painting may leave gapps.
		double tileColWidth = round(colWidth * tilePercentage);
		double tileRowHeight = round(rowHeight * tilePercentage);
		double spacerColWidth = colWidth - tileColWidth;
		double spacerRowHeight = rowHeight - tileRowHeight;
		double halfSpacerColWidth = spacerColWidth / 2d;
		double halfSpacerRowHeight = spacerRowHeight / 2d;

		paintGrid(
				gridLength,
				g,
				width, height,
				colWidth, rowHeight,
				spacerColWidth, spacerRowHeight,
				halfSpacerColWidth, halfSpacerRowHeight);
		paintTiles(
				gridLength,
				g,
				colWidth, rowHeight,
				tileColWidth, tileRowHeight,
				halfSpacerColWidth, halfSpacerRowHeight);
	}

	private void paintGrid(
			int gridLength,
			Graphics g,
			int width, int height,
			double colWidth, double rowHeight,
			double spacerColWidth, double spacerRowHeight,
			double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		g.setColor(SlideGameColors.SPACER_COLOR);
		for (int c = 0; c <= gridLength; c++)
		{
			int x = round((c * colWidth) - halfSpacerColWidth);
			int y = 0;
			int verticalLineWidth = round(spacerColWidth);
			g.fillRect(
					x, y,
					verticalLineWidth, height);
		}
		for (int r = 0; r <= gridLength; r++)
		{
			int x = 0;
			int y = round((r * rowHeight) - halfSpacerRowHeight);
			int horizontalLineHeight = round(spacerRowHeight);
			g.fillRect(
					x, y,
					width, horizontalLineHeight);
		}
	}

	private void paintTiles(
			int gridLength,
			Graphics g,
			double colWidth, double rowHeight,
			double tileColWidth, double tileRowHeight,
			double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		for (int c = 0; c < gridLength; c++)
		{
			for (int r = 0; r < gridLength; r++)
			{
				paintTile(
						g,
						c, r,
						colWidth, rowHeight,
						tileColWidth, tileRowHeight,
						halfSpacerColWidth, halfSpacerRowHeight);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int c, int r,
			double colWidth, double rowHeight,
			double tileColWidth, double tileRowHeight,
			double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		Tile tile = grid.getTile(c, r);
		if (tile != null)
		{
			paintTileBackground(
					SlideGameColors.getColor(tile),
					g,
					c, r,
					colWidth, rowHeight,
					tileColWidth, tileRowHeight,
					halfSpacerColWidth, halfSpacerRowHeight);
			paintTileText(
					tile.getDisplayValue(),
					g,
					c, r,
					colWidth, rowHeight);
		}
	}

	private void paintTileBackground(
			Color tileColor,
			Graphics g,
			int c, int r,
			double colWidth, double rowHeight,
			double tileColWidth, double tileRowHeight,
			double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		g.setColor(tileColor);
		int x = round((c * colWidth) + halfSpacerColWidth);
		int y = round((r * rowHeight) + halfSpacerRowHeight);
		int width = round(tileColWidth);
		int height = round(tileRowHeight);
		g.fillRect(x, y, width, height);
	}

	private void paintTileText(
			String tileText,
			Graphics g,
			int c, int r,
			double colWidth, double rowHeight)
	{
		g.setColor(SlideGameColors.TILE_TEXT_COLOR);
		double fontHeightPx = TILE_FONT.getSize() * 0.75d;
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(tileText);
		int x = round((((c + 0.5d) * colWidth) - (textWidth / 2)));
		int y = round((((r + 0.5d) * rowHeight) + (fontHeightPx / 2)));
		g.drawString(tileText, x, y);
	}

	private static int round(double value)
	{
		return Math.toIntExact(Math.round(value));
	}
}
