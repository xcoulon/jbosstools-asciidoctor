package org.jboss.tools.asciidoc.texteditor.internal.document.partition;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * A whitespace detector
 * @author xcoulon
 *
 */
public class WhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}
}
