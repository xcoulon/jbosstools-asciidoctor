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

package org.jboss.tools.asciidoc.texteditor.internal.document;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner.MULTI_LINE_COMMENT;
import static org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner.PARAGRAPH;
import static org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner.SINGLE_LINE_COMMENT;
import static org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner.TITLE_LEVEL_1;
import static org.jboss.tools.asciidoc.texteditor.internal.document.partition.AsciidocDocumentPartitionScanner.TITLE_LEVEL_2;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.part.FileEditorInput;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.tools.asciidoc.texteditor.internal.util.TestProject;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xcoulon
 *
 */
public class DocumentPartitionningTest {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentPartitionningTest.class);
	
	@Rule
	public final TestProject project = new TestProject("sample-project");
	
	private static Matcher<List<ITypedRegion>> containsType(final String partitionType) {
		return new BaseMatcher<List<ITypedRegion>>() {

			@Override
			public boolean matches(Object item) {
				@SuppressWarnings("unchecked")
				final List<ITypedRegion> partitions = (List<ITypedRegion>) item;
				for(ITypedRegion partition : partitions) {
					if(partition.getType().equals(partitionType)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("At least one partition of type '" + partitionType + "'");
			}

			@Override
		    public void describeMismatch(Object item, Description description) {
				@SuppressWarnings("unchecked")
				final List<ITypedRegion> partitions = (List<ITypedRegion>) item;
				final List<String> partitionTypes = new ArrayList<String>();
				for(ITypedRegion partition : partitions) {
					partitionTypes.add(partition.getType());
				}
		        description.appendText("was ").appendValue(partitionTypes.toString());
		    }
		};
	}
	
	/**
	 * Creates an {@link IDocument} with an {@link IDocumentPartitioner} from the given file and returns the {@link ITypedRegion}s of the document. 
	 * @param file the file to open
	 * @return the document partitions
	 * @throws CoreException
	 * @throws BadLocationException
	 */
	private static List<ITypedRegion> getPartitions(final IFile file) throws CoreException, BadLocationException {
		final FileEditorInput fileEditorInput = new FileEditorInput(file);
		final AsciidocDocumentProvider documentProvider = new AsciidocDocumentProvider();
		documentProvider.connect(fileEditorInput);
		final IDocument document = documentProvider.getDocument(fileEditorInput);
		final List<ITypedRegion> partitions = new ArrayList<ITypedRegion>();
		LOGGER.info("Document partitions:");
		int offset = 0;
		while(offset < document.getLength()) {
			final ITypedRegion partition = document.getPartition(offset);
			partitions.add(partition);
			LOGGER.info("  " + partition);
			offset = partition.getOffset() + partition.getLength() + 1;
		}
		return partitions;
	}
	
	@Test
	public void shouldDetectPartitions() throws CoreException, BadLocationException {
		// pre-conditions
		final IFile file = (IFile) project.findMember("sample.adoc");
		assertThat(file, notNullValue());
		// operation
		final List<ITypedRegion> partitions = getPartitions(file);
		// verifications
		assertThat(partitions, notNullValue());
		// 11 asciidoc related regions + 1 default following the multi-comment partition
		assertThat(partitions.size(), equalTo(12));
		assertThat(partitions, containsType(SINGLE_LINE_COMMENT));
		assertThat(partitions, containsType(MULTI_LINE_COMMENT));
		assertThat(partitions, containsType(PARAGRAPH));
		assertThat(partitions, containsType(TITLE_LEVEL_1));
		assertThat(partitions, containsType(TITLE_LEVEL_2));
	}

	@Test
	public void shouldDetectParagraph() throws CoreException, BadLocationException {
		// pre-conditions
		final IFile file = (IFile) project.findMember("paragraph.adoc");
		assertThat(file, notNullValue());
		// operation
		final List<ITypedRegion> partitions = getPartitions(file);
		// verifications
		assertThat(partitions, notNullValue());
		assertThat(partitions.size(), equalTo(2));
		for (ITypedRegion partition : partitions) {
			assertThat(partition.getType(), equalTo(PARAGRAPH));
		}
	}

}
