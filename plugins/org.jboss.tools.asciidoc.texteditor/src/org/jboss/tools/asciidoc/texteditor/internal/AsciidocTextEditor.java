/**
 * 
 */
package org.jboss.tools.asciidoc.texteditor.internal;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.jboss.tools.asciidoc.texteditor.internal.document.AsciidocDocumentProvider;
import org.jboss.tools.asciidoc.texteditor.internal.util.Logger;

/**
 * @author xcoulon
 *
 */
public class AsciidocTextEditor extends TextEditor {

	@Override
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		final AsciidocSourceViewer sourceViewer = new AsciidocSourceViewer(parent, ruler, styles, getPreferenceStore());
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(sourceViewer);
		sourceViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				final AsciidocSourceViewer selectionProvider = (AsciidocSourceViewer) event.getSelectionProvider();
				final IDocument document = selectionProvider.getDocument();
				final ITextSelection textSelection = (ITextSelection)event.getSelection();
				
				final int offset = textSelection.getOffset();
				Logger.debug("Selection changed: {} -> {}", offset, document.getDocumentPartitioner().getPartition(offset));
			}
		});
		return sourceViewer;
	}

	@Override
	protected void initializeEditor() {
		setDocumentProvider(new AsciidocDocumentProvider());
		super.initializeEditor();
	    setSourceViewerConfiguration(new AsciidocTextEditorConfiguration((AsciidocDocumentProvider)getDocumentProvider()));
	}

}
