/**
 * 
 */
package org.jboss.tools.asciidoc.texteditor.internal.document.partition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.jboss.tools.asciidoc.texteditor.internal.document.AsciidocDocument;

/**
 * Defines the partitions of an Asciidoc Document
 * @author xcoulon
 *
 */
public class AsciidocDocumentPartitionScanner extends RuleBasedPartitionScanner {

	
	public static final String SINGLE_LINE_COMMENT = "__asciidoc_single_line_comment";
	public static final String MULTI_LINE_COMMENT = "__asciidoc_multi_line_comment";
	public static final String PARAGRAPH = "__asciidoc_paragraph";
	public static final String TITLE_LEVEL_1 = "__asciidoc_title1";
	public static final String TITLE_LEVEL_2 = "__asciidoc_title2";
	
	public static final String[] PARTITIONS = new String[] { TITLE_LEVEL_1, TITLE_LEVEL_2, MULTI_LINE_COMMENT, SINGLE_LINE_COMMENT };

	private final List<IScannablePartitionRule> rules = new ArrayList<>();
	private String[] legalRegions = null;

	/**
	 * Constructor.
	 * Initializes the rules to partition an {@link AsciidocDocument}
	 */
	public AsciidocDocumentPartitionScanner() {
		rules.add(new MultiLineCommentPartitionRule());
		rules.add(new SingleLineCommentPartitionRule());
		rules.add(new TitleLevel1PartitionRule());
		rules.add(new TitleLevel2PartitionRule());
		rules.add(new WhitespaceLinePartitionRule());
		// paragraph rule should be the last one to be configured !
		rules.add(new ParagraphRule());
		// end of init
		final IPredicateRule[] predicateRules = new IPredicateRule[rules.size()];
		this.legalRegions = new String[rules.size()];
		for (int i = 0; i < rules.size(); i++) {
			final IScannablePartitionRule rule = rules.get(i);
			predicateRules[i] = rule;
			legalRegions[i] = rule.getContentType();
			
		}
		setPredicateRules(predicateRules);
	}

	/**
	 * @return the legal regions for an {@link AsciidocDocument}
	 */
	public String[] getLegalRegions() {
		return legalRegions;
	}

	/**
	 * @return the {@link IScannablePartitionRule}s configured to partition an {@link AsciidocDocument} 
	 */
	public List<IScannablePartitionRule> getRules() {
		return rules;
	}

	
}
