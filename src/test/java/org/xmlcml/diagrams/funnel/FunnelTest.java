package org.xmlcml.diagrams.funnel;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ColorStore;
import org.xmlcml.graphics.svg.util.ColorStore.ColorizerType;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.EdgeSegments;
import org.xmlcml.image.pixel.EdgeSegmentsList;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelRingList;

import junit.framework.Assert;

public class FunnelTest {
	private static final Logger LOG = Logger.getLogger(FunnelTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testFunnelSegments() {
		double tolerance = 0.9;
		DiagramAnalyzerOLD diagramAnalyzer = new DiagramAnalyzerOLD();
		String filename[] = "funnel1.gif".split("\\.");
//		String filename[] = "funnel2.jpg".split("\\.");
//		String filename[] = "funnel3.png".split("\\.");
		File imageFile = new File(DiagramsFixtures.FUNNEL_DIR, filename[0] + "." + filename[1]);
		
		diagramAnalyzer.getOrCreateGraphList(imageFile);
		PixelIslandList pixelIslandList = diagramAnalyzer.getOrCreatePixelIslandList();
		Assert.assertEquals("islands",  14, pixelIslandList.size());
		SVGG g = new SVGG();
		// pixels
		Iterator<String> iterator = ColorStore.getColorIterator(ColorizerType.CONTRAST);
		int[] sizes = new int[] {3560,71,48,48,48,48,47,47,47,47,47,47,47,47};
		int[] nodes = new int[] {33,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		int[] edgeSegmentListSizes = new int[] {36,4,1,1,1,1,1,1,1,1,1,1,1,1};
		int[][] edgeSegmentCounts = new int[][] {
			new int[]{1,2,1,1,1,2,1,1,1,2,1,1,1,4,1,6,4,3,5,1,4,2,6,2,5,3,3,3,3,3,3,3,3,5,8,5},
			new int[]{1,1,4,3},
			new int[]{8},
	        new int[]{9},
	        new int[]{8},
	        new int[]{9},
	        new int[]{8},
	        new int[]{8},
	        new int[]{8},
	        new int[]{8},
	        new int[]{9},
	        new int[]{8},
	        new int[]{8},
	        new int[]{10},
			};
		Boolean[] cyclic = new Boolean[] {false,false,true,true,true,true,true,true,true,true,true,true,true,true};
		for (int isl = 0; isl < sizes.length; isl++) {
			PixelIsland island = pixelIslandList.get(isl);
			PixelGraph graph = new PixelGraph(island);
			Assert.assertEquals("island", sizes[isl], island.size());
			Assert.assertEquals("nodes", nodes[isl], graph.getNodeList().size());
			EdgeSegmentsList edgeSegmentsList = graph.getOrCreateEdgeSegmentsList(tolerance);
			Assert.assertEquals("cyclic "+isl, cyclic[isl], graph.getOrCreateIsSingleCycle());
			Assert.assertEquals("segments "+isl, edgeSegmentListSizes[isl], edgeSegmentsList.size());
			for (int jseg = 0; jseg < edgeSegmentsList.size(); jseg++) {
				EdgeSegments jSegments = edgeSegmentsList.get(jseg);
				Assert.assertEquals("size "+isl+"_"+jseg, edgeSegmentCounts[isl][jseg], jSegments.size());
				SVGG segG = jSegments.getOrCreateSVG();
				segG.setCSSStyle("stroke-width:1.0;stroke:"+iterator.next()+";");
				g.appendChild(segG);
			}
			graph.createAndDrawGraph(g);
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/funnel/" + filename[0] + ".svg"));
	}


	@Test
	public void testFunnelPixelRings() {
		for (String filename : new String[] {
				"funnel1.gif","funnel2.jpg","funnel3.png"		}) {
			String [] filenames = filename.split("\\.");
			File imageFile = new File(DiagramsFixtures.FUNNEL_DIR, filenames[0] + "." + filenames[1]);
			SVGG g = plotRings(imageFile);
			SVGSVG.wrapAndWriteAsSVG(g, new File("target/funnel/" + filenames[0] + ".rings.svg"));
		}
	}


	private SVGG plotRings(File imageFile) {
		ImageProcessor imageProcessor = ImageProcessor.createDefaultProcessor();
		imageProcessor.setThinning(null);
		imageProcessor.readAndProcessFile(imageFile);
		PixelIslandList islandList = imageProcessor.getOrCreatePixelIslandList();
		SVGG g = new SVGG();
		// pixels
		Iterator<String> iterator = ColorStore.getColorIterator(ColorizerType.CONTRAST);
		for (PixelIsland island : islandList) {
			PixelRingList pixelRingList = island.getOrCreateInternalPixelRings();
			for (PixelList pixelList : pixelRingList) {
				SVGG gg = pixelList.getOrCreateSVG();
				gg.setCSSStyle("stroke-width:1.0;stroke:"+iterator.next()+";");
//				g.appendChild(gg);
			}
			PixelList outline = pixelRingList.getOuterPixelRing();
			if (outline != null) {
				SVGG gg = outline.getOrCreateSVG();
				gg.setCSSStyle("stroke-width:0.2;stroke:"+"black"+"; fill: none;");
				g.appendChild(gg);
			}
		}
		return g;
	}
}

