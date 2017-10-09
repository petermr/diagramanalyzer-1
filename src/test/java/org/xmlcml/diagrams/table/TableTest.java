package org.xmlcml.diagrams.table;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.diagrams.phylo.PhyloTreeAnalyzerTest;

public class TableTest {

	@Test
	// ocr text
	public void testAR01_12GraphsAndText() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(DiagramsFixtures.MISC_DIR, "", "AR01_12", 2, 128);
	}

	@Test
	public void testBP2012() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(DiagramsFixtures.MISC_DIR, "", "BP2012", 2,
				180);
	}

}
