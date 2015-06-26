/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.common.grid;

import java.util.UUID;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell to display text.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellText extends AbstractGridCell {

	private String text;

	private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0,
			0, 0);

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public GridCellText() {
		this.text = "NONE"; //$NON-NLS-1$
	}

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param text
	 *            the intial text in the cell.
	 * 
	 */
	public GridCellText(String text) {
		this.text = text;
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		Color bgColor = gc.getBackground();

		Rectangle bounds = renderer.getDrawBounds();

		gc.setBackground(this.getBackgroundColor(renderer, gc));

		Color fgColor = gc.getForeground();
		gc.setForeground(GridCellText.TEXT_COLOR);
		FontMetrics metrics= gc.getFontMetrics();
		//calculate the avaiable space and performe a wrap
		int char_wrapper= bounds.width/metrics.getAverageCharWidth() -1;
		int lines = this.text.length() / char_wrapper;
		int start = 0;
		int end = Math.min(this.text.length(),char_wrapper);
		int line_height = bounds.y;
		while(end < this.text.length()-1){
			while(this.text.charAt(end) != ' '){
				end=Math.max(0,end-1);
			}
			gc.drawText(this.text.substring(start, end), bounds.x + 2 ,line_height);
			start = end +1;
			end += char_wrapper;
			line_height += metrics.getHeight();
		}
		gc.drawText(this.text.substring(start), bounds.x + 2 ,line_height);

		// restore bg color
		gc.setBackground(bgColor);
		// restore fg color
		gc.setForeground(fgColor);
	}

	protected void setText(String text) {
		this.text = text;
	}

	@Override
	public void cleanUp() {
		// intentionally empty
	}

	@Override
	public int getPreferredHeight() {
		return AbstractGridCell.DEFAULT_CELL_HEIGHT;
	}

	@Override
	public void addCellButton(CellButton button) {
		this.getButtonContainer().addCellButton(button);
	}

	@Override
	public void clearCellButtons() {
		this.getButtonContainer().clearButtons();
	}

	@Override
	public UUID getUUID() {
		return null;
	}

	@Override
	public void activate() {
		// intentionally empty

	}
}
