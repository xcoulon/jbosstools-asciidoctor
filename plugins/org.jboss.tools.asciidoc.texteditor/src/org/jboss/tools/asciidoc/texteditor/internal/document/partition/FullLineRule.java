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

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IToken;

/**
 * An extension of the {@link EndOfLineRule} in which the rule starts at the
 * beginning of the line, not anywhere before the end. The partition covers the
 * whole line if the pattern matches at the beginning of the line.
 * 
 * @author xcoulon
 * 
 */
public class FullLineRule extends EndOfLineRule {

	/**
	 * Full constructor
	 * 
	 * @param startSequence
	 *            the sequence that should start the line
	 * @param token
	 *            the token to be returned on success
	 * @param escapeCharacter
	 *            the escape character
	 * @param escapeContinuesLine
	 *            indicates whether the specified escape character is used for
	 *            line continuation, so that an end of line immediately after
	 *            the escape character does not terminate the line, even if
	 *            <code>breakOnEOL</code> is true
	 */
	public FullLineRule(final String startSequence, final IToken token, final char escapeCharacter,
			final boolean escapeContinuesLine) {
		super(startSequence, token, escapeCharacter, escapeContinuesLine);
		setColumnConstraint(0);
	}


}
