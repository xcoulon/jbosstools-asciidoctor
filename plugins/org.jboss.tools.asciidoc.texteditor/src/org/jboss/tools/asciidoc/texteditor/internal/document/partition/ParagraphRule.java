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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.asciidoc.texteditor.internal.preferences.ColorManager;
import org.jboss.tools.asciidoc.texteditor.internal.util.Logger;

/**
 * @author xcoulon
 *
 */
public class ParagraphRule implements IScannablePartitionRule {

	private final static String contentType = AsciidocDocumentPartitionScanner.PARAGRAPH;
	
	private final Token token;

	/**
	 * Line delimiter comparator which orders according to decreasing delimiter length.
	 * @since 3.1
	 */
	@SuppressWarnings("rawtypes")
	private Comparator fLineDelimiterComparator= new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((char[]) o2).length - ((char[]) o1).length;
			}
		
	};
	/**
	 * Cached line delimiters.
	 * @since 3.1
	 */
	private char[][] fLineDelimiters;
	/**
	 * Cached sorted {@linkplain #fLineDelimiters}.
	 * @since 3.1
	 */
	private char[][] fSortedLineDelimiters;
	
	/**
	 * Constructor
	 * @param token the token to be returned on success
	 */
	public ParagraphRule() {
		super();
		this.token = new Token(contentType);
	}

	@Override
	public IToken getSuccessToken() {
		return token;
	}

	@Override
	public IToken evaluate(final ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}


	@Override
	public IToken evaluate(final ICharacterScanner scanner, final boolean resume) {
		return doEvaluate(scanner, resume);
	}

	private IToken doEvaluate(final ICharacterScanner scanner, final boolean resume) {
		readParagraph(scanner);
		final AsciidocDocumentPartitionScanner docScanner = ((AsciidocDocumentPartitionScanner)scanner);
		Logger.trace("Evaluating {} after last token({}-{}) -> {}", getClass().getSimpleName(), docScanner.getTokenOffset(), docScanner.getTokenLength(), token.getData());
		return token;
	}
	
	/**
	 * Moves the scanner forward until the end of the paragraph, ie, when EOF or an empty line is detected (or a 
	 * line filled with whitespaces and/or tabs).
	 *
	 * @param scanner the character scanner to be used
	 * @return <code>true</code> if the end sequence has been detected
	 */
	protected void readParagraph(final ICharacterScanner scanner) {
		final char[][] lineDelimiters = getLineDelimiters(scanner);
		int c;
		boolean potentialEndOfParagraph = false;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			for (char[] lineDelimiter : lineDelimiters) {
				if(c == lineDelimiter[0]) {
					if(sequenceDetected(scanner, lineDelimiter, true)) {
						if(potentialEndOfParagraph) {
							// match end of paragraph
							return;
						}
						potentialEndOfParagraph = true;
					}
				}
			}
		}
	}
	
	/**
	 * Returns whether the next characters to be read by the character scanner
	 * are an exact match with the given sequence. No escape characters are allowed
	 * within the sequence. If specified the sequence is considered to be found
	 * when reading the EOF character.
	 *
	 * @param scanner the character scanner to be used
	 * @param sequence the sequence to be detected
	 * @param eofAllowed indicated whether EOF terminates the pattern
	 * @return <code>true</code> if the given sequence has been detected
	 */
	protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		for (int i= 1; i < sequence.length; i++) {
			int c= scanner.read();
			if (c == ICharacterScanner.EOF && eofAllowed) {
				return true;
			} else if (c != sequence[i]) {
				// Non-matching character detected, rewind the scanner back to the start.
				// Do not unread the first character.
				scanner.unread();
				for (int j= i-1; j > 0; j--)
					scanner.unread();
				return false;
			}
		}

		return true;
	}


	/**
	 * Returns the valid line delimiters for the current {@link IDocument}, sorted by their length.
	 * @param scanner the document scanner
	 * @return the line delimiters.
	 */
	@SuppressWarnings("unchecked")
	private char[][] getLineDelimiters(final ICharacterScanner scanner) {
		char[][] originalDelimiters = scanner.getLegalLineDelimiters();
		int count = originalDelimiters.length;
		if (fLineDelimiters == null || fLineDelimiters.length != count) {
			fSortedLineDelimiters = new char[count][];
		} else {
			while (count > 0 && Arrays.equals(fLineDelimiters[count - 1], originalDelimiters[count - 1]))
				count--;
		}
		if (count != 0) {
			fLineDelimiters = originalDelimiters;
			System.arraycopy(fLineDelimiters, 0, fSortedLineDelimiters, 0, fLineDelimiters.length);
			Arrays.sort(fSortedLineDelimiters, fLineDelimiterComparator);
		}
		return fSortedLineDelimiters;
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
			final Color color = ColorManager.getColor(getContentType());
			setDefaultReturnToken(new Token(new TextAttribute(color)));
			final List<IRule> rules = new ArrayList<IRule>();
			// Add rule for bold text
			rules.add(new SingleLineRule("*", "*", new Token(createTextAttribute(getContentType(), SWT.BOLD, SWT.NORMAL))));
			// Add rule for italic text
			rules.add(new SingleLineRule("_", "_", new Token(createTextAttribute(getContentType(), SWT.NORMAL, SWT.ITALIC))));
			// Add generic whitespace rule.
			// rules.add(new WhitespaceRule(new WhitespaceDetector()));
			setRules(rules.toArray(new IRule[rules.size()]));
		}

		@Override
		public String getContentType() {
			return contentType;
		}
	}

	
}
