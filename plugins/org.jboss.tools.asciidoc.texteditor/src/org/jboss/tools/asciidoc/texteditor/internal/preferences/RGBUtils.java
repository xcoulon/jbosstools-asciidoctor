/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.asciidoc.texteditor.internal.preferences;

import org.eclipse.swt.graphics.RGB;

/**
 * @author xcoulon
 *
 */
public class RGBUtils {
	
	private static final int HEX = 16;
	
	/**
	 * Converts a CSS HEX Value (sucha as #123456) to an RGB value.
	 * @param cssHexValue
	 * @return the corresponding RGB
	 */
	public static RGB toRGB(final String cssHexValue) {
		final int red= Integer.parseInt(cssHexValue.substring(1, 3), HEX);
		final int green= Integer.parseInt(cssHexValue.substring(3, 5), HEX);
		final int blue= Integer.parseInt(cssHexValue.substring(5), HEX);
		return new RGB(red, green, blue);
	}

}
