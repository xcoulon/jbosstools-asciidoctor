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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.jboss.tools.asciidoc.texteditor.internal.util.Logger;

/**
 * @author xcoulon
 *
 */
public class TitleLevel1PartitionRule extends PatternRule implements IScannablePartitionRule {

	private final static String contentType = AsciidocDocumentPartitionScanner.TITLE_LEVEL_1;
	
	public TitleLevel1PartitionRule() {
		super("= ", null, new Token(contentType), (char)0, true);
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		final AsciidocDocumentPartitionScanner docScanner = ((AsciidocDocumentPartitionScanner)scanner);
		final IToken result = super.evaluate(scanner, resume);
		Logger.trace("Evaluating {} after last token({}-{}) -> {}", getClass().getSimpleName(), docScanner.getTokenOffset(), docScanner.getTokenLength(), result.getData());
		return result;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public IPartitionScanner getPartitionScanner() {
		return new PartitionScanner();
	}
	
	public static class PartitionScanner extends AbstractPartitionScanner {

		public PartitionScanner() {
			final List<IRule> rules = new ArrayList<IRule>();
			final Token token = new Token(
					createTextAttribute(getContentType(), SWT.BOLD, SWT.NORMAL));
			rules.add(new FullLineRule("= ", token, (char)0, true));
			// Add generic whitespace rule.
			rules.add(new WhitespaceRule(new WhitespaceDetector()));

			setRules(rules.toArray(new IRule[rules.size()]));
			// Set a default rule in case of EOF or missing end sequence
			setDefaultReturnToken(token);
		}

		@Override
		public String getContentType() {
			return contentType;
		}
		
	}

}
