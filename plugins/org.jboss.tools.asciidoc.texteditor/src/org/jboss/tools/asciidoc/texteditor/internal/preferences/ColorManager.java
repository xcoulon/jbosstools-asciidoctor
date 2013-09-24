package org.jboss.tools.asciidoc.texteditor.internal.preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner;

public class ColorManager {

	protected Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);

	private static ColorManager instance = new ColorManager();
	
	public static ColorManager getDefault() {
		return instance;
	}
	
	/**
	 * Private constructor for the singleton
	 */
	private ColorManager() { 
		super();
	}
	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	
	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}
	
	public static Color getColor(final String colorID) {
		switch (colorID) {
		case AsciidocDocumentPartitionScanner.MULTI_LINE_COMMENT:
		case AsciidocDocumentPartitionScanner.SINGLE_LINE_COMMENT:
			return getDefault().getColor(IColorConstants.SOLARIZED_BASE01);
		case AsciidocDocumentPartitionScanner.TITLE_LEVEL_1:
			return getDefault().getColor(IColorConstants.SOLARIZED_RED);
		case AsciidocDocumentPartitionScanner.TITLE_LEVEL_2:
			return getDefault().getColor(IColorConstants.SOLARIZED_ORANGE);
		default:
			return getDefault().getColor(IColorConstants.SOLARIZED_BASE1);
		}
	}
}
