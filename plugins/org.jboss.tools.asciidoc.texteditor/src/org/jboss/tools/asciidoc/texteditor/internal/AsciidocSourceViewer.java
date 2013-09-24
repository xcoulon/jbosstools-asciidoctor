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

package org.jboss.tools.asciidoc.texteditor.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author xcoulon
 * 
 */
public class AsciidocSourceViewer extends SourceViewer {

	public AsciidocSourceViewer(Composite parent, IVerticalRuler ruler, int styles,
			final IPreferenceStore preferenceStore) {
		super(parent, ruler, styles);
		/*
		//FIXME : only configure for this editor, not all text editors
		preferenceStore.setValue(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, false);
		PreferenceConverter.setDefault(preferenceStore, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND,
				IColorConstants.SOLARIZED_BASE03);
		preferenceStore.setValue(AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT, false);
		PreferenceConverter.setDefault(preferenceStore, AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND,
				IColorConstants.SOLARIZED_BASE01);
		preferenceStore.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR, false);
		PreferenceConverter.setDefault(preferenceStore, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR,
				IColorConstants.SOLARIZED_BASE02);
		preferenceStore.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR, false);
		PreferenceConverter.setDefault(preferenceStore, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR,
				IColorConstants.SOLARIZED_BASE02);
		*/
	}

}
