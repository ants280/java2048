package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.Tile;
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
		this.setBackground(SlideGameColors.EMPTY_TILE_COLOR);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		// TODO: Ensure width = height.
		int width = this.getWidth();
		int height = this.getHeight();
		g.clearRect(0, 0, width, height);

		int length = grid.getLength();
		double tilePercentage = 0.90d;
		double colWidth = width / length;
		double rowHeight = height / length;
		double tileColWidth = colWidth * tilePercentage;
		double tileRowHeight = rowHeight * tilePercentage;
		double spacerColWidth = colWidth - tileColWidth;
		double spacerRowHeight = rowHeight - tileRowHeight;
		double halfSpacerColWidth = spacerColWidth / 2d;
		double halfSpacerRowHeight = spacerRowHeight / 2d;

		paintGrid(g, width, height, length, colWidth, rowHeight, spacerColWidth, spacerRowHeight, halfSpacerColWidth, halfSpacerRowHeight);
		paintTiles(length, g, colWidth, rowHeight, tileColWidth, tileRowHeight, halfSpacerColWidth, halfSpacerRowHeight);
	}

	private void paintGrid(Graphics g, int width, int height, int length, double colWidth, double rowHeight, double spacerColWidth, double spacerRowHeight, double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		// draw vertical and horizontal bars
		g.setColor(SlideGameColors.SPACER_COLOR);
		for (int c = 0; c <= length; c++)
		{
			double colOffset = c * colWidth;
			g.fillRect((int) (colOffset - halfSpacerColWidth),
					0, (int) spacerColWidth, height);
		}
		for (int r = 0; r <= length; r++)
		{
			double rowOffset = r * rowHeight;
			g.fillRect(0, (int) (rowOffset - halfSpacerRowHeight),
					width, (int) spacerRowHeight);
		}
	}

	private void paintTiles(int length, Graphics g, double colWidth, double rowHeight, double tileColWidth, double tileRowHeight, double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		// draw tiles
		for (int r = 0; r < length; r++)
		{
			for (int c = 0; c < length; c++)
			{
				paintTile(
						g,
						r, c,
						colWidth, rowHeight,
						tileColWidth, tileRowHeight,
						halfSpacerColWidth, halfSpacerRowHeight);
			}
		}
	}

	private void paintTile(
			Graphics g,
			int r, int c,
			double colWidth, double rowHeight,
			double tileColWidth, double tileRowHeight,
			double halfSpacerColWidth, double halfSpacerRowHeight)
	{
		Tile tile = grid.getTile(r, c);
		if (tile != null)
		{
			paintTileBackground(g, tile, c, colWidth, halfSpacerColWidth, r, rowHeight, halfSpacerRowHeight, tileColWidth, tileRowHeight);
			paintTileText(tile, g, c, colWidth, r, rowHeight);
		}
	}

	private void paintTileText(Tile tile, Graphics g, int c, double colWidth, int r, double rowHeight)
	{
		String displayValue = tile.getDisplayValue();
		g.setColor(SlideGameColors.TILE_TEXT_COLOR);
		double fontHeightPx = TILE_FONT.getSize() * 0.75d;
		FontMetrics fontMetrics = g.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(displayValue);
		int x = (int) (((c + 0.5d) * colWidth) - (textWidth / 2));
		int y = (int) (((r + 0.5d) * rowHeight) + (fontHeightPx / 2));
		g.drawString(displayValue, x, y);
	}

	private void paintTileBackground(Graphics g, Tile tile, int c, double colWidth, double halfSpacerColWidth, int r, double rowHeight, double halfSpacerRowHeight, double tileColWidth, double tileRowHeight)
	{
		g.setColor(SlideGameColors.getColor(tile));
		g.fillRect(
				(int) (c * colWidth + halfSpacerColWidth), // x
				(int) (r * rowHeight + halfSpacerRowHeight), // y
				(int) tileColWidth, // width
				(int) tileRowHeight); // height
	}
}
