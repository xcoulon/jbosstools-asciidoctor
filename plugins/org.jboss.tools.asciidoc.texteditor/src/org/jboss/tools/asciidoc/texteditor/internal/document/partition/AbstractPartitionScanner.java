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

package org.jboss.tools.asciidoc.texteditor.internal.document.partition;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.asciidoc.texteditor.AsciidocTextEditorActivator;
import org.jboss.tools.asciidoc.texteditor.internal.preferences.ColorManager;
import org.jboss.tools.asciidoc.texteditor.internal.preferences.IPreferenceConstants;

/**
 * @author xcoulon
 * 
 */
public abstract class AbstractPartitionScanner extends RuleBasedScanner implements IPartitionScanner {

	/**
	 * Generates a {@link TextAttribute} for the given colorId, using the
	 * {@link IPreferenceStore} to determine the style to apply.
	 * 
	 * @param colorId the text color id 
	 * @param defaultBold ?  if the text should be bold by default (ie, if plugin's {@link IPreferenceStore} has no specific value for the given colorId
	 * @param italic if the text should be italic by default (ie, if plugin's {@link IPreferenceStore} has no specific value for the given colorId
	 * @return the style ({@link TextAttribute}) that will be applied on the text matching the rule in the partition.
	 * @see {@link ColorManager}
	 */
	static TextAttribute createTextAttribute(final String colorId, final int defaultBold, final int defaultItalic) {
		final Color color = ColorManager.getColor(colorId);
		final IPreferenceStore store = AsciidocTextEditorActivator.getDefault().getPreferenceStore();
		final int style = getBoldStyle(store, colorId + IPreferenceConstants.BOLD_SUFFIX, defaultBold)
				+ getItalicStyle(store, colorId + IPreferenceConstants.ITALIC_SUFFIX, defaultItalic);
		return new TextAttribute(color, null, style);
	}
	
	private static int getBoldStyle(final IPreferenceStore store, final String key, final int defaultValue) {
		if(store.contains(key)) {
			return store.getBoolean(key) ? SWT.BOLD : SWT.NORMAL;
		}
		return defaultValue;
	}

	private static int getItalicStyle(final IPreferenceStore store, final String key, final int defaultValue) {
		if(store.contains(key)) {
			return store.getBoolean(key) ? SWT.ITALIC : SWT.NORMAL;
		}
		return defaultValue;
		
	}
}
