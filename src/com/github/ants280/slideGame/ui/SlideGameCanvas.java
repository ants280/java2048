package com.github.ants280.slideGame.ui;

import com.github.ants280.slideGame.logic.Grid;
import com.github.ants280.slideGame.logic.Tile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JComponent;

public class SlideGameCanvas extends JComponent
{
	private final Grid grid;
	private final Font TILE_FONT = new Font("times", Font.PLAIN, 96); // TODO: make font scale
	// TODO: store colors in Tile?
	public static final Color EMPTY_TILE_COLOR = new Color(0xCDC1B4);
	public static final Color TILE_TEXT_COLOR = new Color(0x776E65);
	public static final Color SPACER_COLOR = new Color(0xBBADA0);
	private static final Map<Tile, Color> TILE_COLOR_CACHE = Arrays.stream(Tile.values())
			.collect(Collectors.toMap(Function.identity(), SlideGameCanvas::getColorFromTile));

	public SlideGameCanvas(Grid grid)
	{
		super();

		this.grid = grid;
		this.setBackground(EMPTY_TILE_COLOR);
	}
	
	/**
	 * {@inheritDoc} Contains Double-Buffering logic to make painting more
	 * smooth.
	 *
	 * @param g The Canvass Graphics to pain on.
	 */
	/*
	@Override
	public void update(Graphics g)
	{
		//System.out.println(this.isDoubleBuffered());
		BufferedImage lastDrawnImage
			= (BufferedImage) this.createImage(this.getWidth(),
				this.getHeight());

		//Draws the shape onto the BufferedImage
		this.paint(lastDrawnImage.getGraphics());

		//Draws the BufferedImage onto the PaintPanel
		g.drawImage(lastDrawnImage, 0, 0, this);
	}
	 */
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

		// draw vertical and horizontal bars
		g.setColor(SPACER_COLOR);
		for (int c = 0; c <= length; c++)
		{
			double colOffset = c * colWidth;
			g.fillRect(
					(int) (colOffset - halfSpacerColWidth),
					0, (int) spacerColWidth, height);
		}
		for (int r = 0; r <= length; r++)
		{
			double rowOffset = r * rowHeight;
			g.fillRect(
					0, (int) (rowOffset - halfSpacerRowHeight)
					, width, (int) spacerRowHeight);
		}
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
			// draw tile background
			g.setColor(TILE_COLOR_CACHE.get(tile));
			g.fillRect(
					(int) (c * colWidth + halfSpacerColWidth), // x
					(int) (r * rowHeight + halfSpacerRowHeight), // y
					(int) tileColWidth, // width
					(int) tileRowHeight); // height);
			// draw tile text
			String displayValue = tile.getDisplayValue();
			g.setColor(TILE_TEXT_COLOR);
			double fontHeightPx = TILE_FONT.getSize() * 0.75d;
			FontMetrics fontMetrics = g.getFontMetrics();
			int textWidth = fontMetrics.charsWidth(displayValue.toCharArray(), 0, displayValue.length());
			int colWidthOffset = (int) (((c + 0.5d) * colWidth) - (textWidth / 2));
			int rowHeightOffset = (int) (((r + 0.5d) * rowHeight) + (fontHeightPx / 2));
			g.drawString(displayValue, colWidthOffset, rowHeightOffset);
		}
	}
	
	private static Color getColorFromTile(Tile tile)
	{
		return new Color((int) tile.getColor());
	}
}
