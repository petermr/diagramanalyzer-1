package org.xmlcml.diagrams.misc;

import org.junit.Test;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;
import org.xmlcml.diagrams.phylo.PhyloTreePixelAnalyzer;

/** selected examples which show the system working.
 * 
 * also act as regression tests.
 * 
 * @author pm286
 *
 */
public class ExamplesTest {

	@Test
	public void test0() {
		String[] args = {
				"--input",  "src/test/resources/org/xmlcml/diagrams/misc/BP2012.png",  // source image
	//			"--maxIsland", "50", // take the largest island (no magic, if not the first you have to work out which)	
				"--island", "0"
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

}
