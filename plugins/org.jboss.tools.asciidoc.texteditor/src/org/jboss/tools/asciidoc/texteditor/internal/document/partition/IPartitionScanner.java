package org.jboss.tools.asciidoc.texteditor.internal.document.partition;

import org.eclipse.jface.text.rules.ITokenScanner;

public interface IPartitionScanner extends ITokenScanner {

	/**
	 * Returns the Content Type of the Text Partition that this scanner will process.
	 */
	public abstract String getContentType();

}