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

package org.jboss.tools.asciidoc.texteditor.internal.document.presentation;

import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.AbstractPartitionScanner;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.IPartitionScanner;


/**
 * @author xcoulon
 *
 */
public class PresentationReconciler extends org.eclipse.jface.text.presentation.PresentationReconciler {

	/**
	 * Register the given {@link AbstractPartitionScanner} as both an {@link IPresentationDamager} and
	 * an {@link IPresentationRepairer} to this {@link org.eclipse.jface.text.presentation.PresentationReconciler} 
	 * @param scanner the scanner to register
	 */
	public void addScanner(final IPartitionScanner scanner) {
		final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
		setDamager(dr, scanner.getContentType());
		setRepairer(dr, scanner.getContentType());
	}
	
}
