package org.xmlcml.diagrams.misc;

import org.junit.Test;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.diagrams.phylo.PhyloTreePixelAnalyzer;
import org.xmlcml.diagrams.phylo.PhyloTreeAnalyzerTest;

public class MiscTest {


	/** get analyze table for tree.
	 * 
	 * this doesn't have root...
	 * 
	 */
	@Test
	public void testGetBP2012() {
		String[] args = {
				"--input",  "src/test/resources/org/xmlcml/diagrams/misc/BP2012.png",  // source image
//				"--maxIsland", "50", // take the largest island (no magic, if not the first you have to work out which)	
				"--island", "0"
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}


	
}
