/**
 * 
 */
package org.jboss.tools.asciidoc.texteditor.internal;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.jboss.tools.asciidoc.texteditor.internal.document.AsciidocDocumentProvider;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.IScannablePartitionRule;
import org.jboss.tools.asciidoc.texteditor.internal.document.presentation.PresentationReconciler;

/**
 * @author xcoulon
 * 
 */
public class AsciidocTextEditorConfiguration extends SourceViewerConfiguration {

	private final AsciidocDocumentPartitionScanner partitionScanner;
	
	public AsciidocTextEditorConfiguration(final AsciidocDocumentProvider documentProvider) {
		this.partitionScanner = documentProvider.getPartitionScanner();
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		String[] partitions = partitionScanner.getLegalRegions();
		String[] all = new String[partitions.length + 1];
		all[0] = IDocument.DEFAULT_CONTENT_TYPE;
		System.arraycopy(partitions, 0, all, 1, partitions.length);
		return all;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		final PresentationReconciler reconciler = new PresentationReconciler();
		for(IScannablePartitionRule rule: partitionScanner.getRules()) {
			//new PartitionScanner(), new SingleLineCommentScanner(), new MultiLineCommentScanner(),
			//new TitleLevel1PartitionScanner(), new TitleLevel2PartitionScanner()
			reconciler.addScanner(rule.getPartitionScanner());
		}
		return reconciler;
	}

}
