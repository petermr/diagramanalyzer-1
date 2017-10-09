package org.xmlcml.diagrams.funnel;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.diagrams.molecules.MolecularAnalyzer;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ColorStore;
import org.xmlcml.graphics.svg.util.ColorStore.ColorizerType;
import org.xmlcml.image.pixel.PixelEdge;
import org.xmlcml.image.pixel.PixelEdgeList;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelNodeList;
import org.xmlcml.image.pixel.PixelSegmentList;

public class FunnelTest {
	private static final Logger LOG = Logger.getLogger(FunnelTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testFunnelAnalyzer() {
		double tolerance = 0.9;
		MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
		File imageFile = new File(DiagramsFixtures.FUNNEL_DIR, "funnel1.gif");
		Assert.assertTrue("FUNNEL", imageFile.exists());
		LOG.trace("file "+imageFile+"; exists "+imageFile.exists());
		molecularAnalyzer.makeMolecule(imageFile);
		PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
		LOG.trace("islandList "+pixelIslandList.toString());
		SVGG g = new SVGG();
		g.appendChild(pixelIslandList.getOrCreateSVGG());
		Iterator<String> iterator = ColorStore.getColorIterator(ColorizerType.CONTRAST);
		for (PixelIsland island : pixelIslandList) {
			PixelGraph graph = new PixelGraph(island);
			PixelEdgeList edgeList = graph.getEdgeList();
			PixelNodeList nodeList = graph.getNodeList();
			if (edgeList.size() == 0) {
				LOG.debug("zero ");
			}
			for (PixelEdge edge : edgeList) {
				PixelSegmentList segments = edge.getOrCreateSegmentList(tolerance);
				LOG.debug("segmentCount: "+segments.size());
				SVGG segG = segments.getOrCreateSVG();
				segG.setCSSStyle("stroke-width:1.0;stroke:"+iterator.next()+";");
				g.appendChild(segG);
			}
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/funnel/funnel1.svg"));
	}
}
