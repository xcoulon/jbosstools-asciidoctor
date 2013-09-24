/**
 * 
 */
package org.jboss.tools.asciidoc.texteditor.internal.document;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner;

/**
 * The Asciidoc Document Provider
 * @author xcoulon
 *
 */
public class AsciidocDocumentProvider extends FileDocumentProvider {

	private final AsciidocDocumentPartitionScanner partitionScanner = new AsciidocDocumentPartitionScanner();

	/**
	 * Creates a document from the given element and registers the partition scanner
	 */
	protected IDocument createDocument(final Object element) throws CoreException {
		final IDocument document = super.createDocument(element);
		if (document != null) {
			final IDocumentPartitioner partitioner = new FastPartitioner(partitionScanner,
					partitionScanner.getLegalRegions());
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
			document.addDocumentPartitioningListener(new IDocumentPartitioningListener() {
				
				@Override
				public void documentPartitioningChanged(IDocument document) {
					System.out.println("Document partition changed");
					int offset = 0;
					while (offset <= document.getLength()) {
						final ITypedRegion partition = document.getDocumentPartitioner().getPartition(offset);
						System.out.println(" [" + partition.getOffset() + "-" + (partition.getOffset()+ partition.getLength()) + "] " + partition.getType());
						offset = partition.getOffset()+ partition.getLength() + 1;
					}
					
				}
			});
		}
		return document;
	}
	
	@Override
    protected IDocument createEmptyDocument() {
        return new AsciidocDocument();
    }

	/**
	 * @return the partitionScanner
	 */
	public AsciidocDocumentPartitionScanner getPartitionScanner() {
		return partitionScanner;
	}	
}
