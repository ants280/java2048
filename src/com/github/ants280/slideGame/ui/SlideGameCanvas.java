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
		this.setBackground(SlideGameColors.EMPTY_TILE_COLOR);
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
		double tileColWidth = colWidth * tilePercentage;
		double tileRowHeight = rowHeight * tilePercentage;
		double spacerColWidth = colWidth - tileColWidth;
		double spacerRowHeight = rowHeight - tileRowHeight;
		double halfSpacerColWidth = spacerColWidth / 2d;
		double halfSpacerRowHeight = spacerRowHeight / 2d;

		g.clearRect(0, 0, width, height);
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
			double colOffset = c * colWidth;
			g.fillRect((int) (colOffset - halfSpacerColWidth),
					0, (int) spacerColWidth, height);
		}
		for (int r = 0; r <= gridLength; r++)
		{
			double rowOffset = r * rowHeight;
			g.fillRect(0, (int) (rowOffset - halfSpacerRowHeight),
					width, (int) spacerRowHeight);
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
		Tile tile = grid.getTile(r, c);
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
		int x = (int) ((c * colWidth) + halfSpacerColWidth);
		int y = (int) ((r * rowHeight) + halfSpacerRowHeight);
		g.fillRect(x, y, (int) tileColWidth, (int) tileRowHeight);
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
		int x = (int) (((c + 0.5d) * colWidth) - (textWidth / 2));
		int y = (int) (((r + 0.5d) * rowHeight) + (fontHeightPx / 2));
		g.drawString(tileText, x, y);
	}
}
