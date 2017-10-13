package org.xmlcml.diagrams.misc;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.diagrams.plot.PlotTest;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelEdge;
import org.xmlcml.image.pixel.PixelEdgeList;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelNodeList;
import org.xmlcml.image.pixel.PixelRingList;
import org.xmlcml.image.pixel.EdgeSegments;
import org.xmlcml.image.processing.ZhangSuenThinning;

public class RSCTest {

	private final static Logger LOG = Logger.getLogger(RSCTest.class);
	
	public final static String RSC = "rsc";
	public final static String FILENAME5047 = "a605047f-3.png";
	public final static String FILENAME6508 = "a606508b-2.png";
	public final static String FILENAME7219 = "a607219d-1.png";
	public final static String FILENAME7736 = "a607736f-1.png";
	public final static File RSC_DIR = new File(RSC);
	public final static String[] FILENAMES = {
		FILENAME5047,
		FILENAME6508,
		FILENAME7219,
		FILENAME7736,
	};
	ImageProcessor DEFAULT_IMAGE_PROCESSOR;
	
	@Before
	public void setup() {
		DEFAULT_IMAGE_PROCESSOR = new ImageProcessor();
	}
	
	@Test
	@Ignore //file missing
	public void testRSC1() throws IOException {
		File rawfile = new File(RSC_DIR, FILENAME5047);
		DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		ImageIOUtil.writeImageQuietly(DEFAULT_IMAGE_PROCESSOR.getImage(), new File("target/"+ RSC+"/raw.png"));
		PixelIslandList islandList = DEFAULT_IMAGE_PROCESSOR
				.getOrCreatePixelIslandList();
		PixelIsland mainIsland = new PixelIsland(islandList.get(0));
		
		DEFAULT_IMAGE_PROCESSOR.setThinning(new ZhangSuenThinning());
		DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		ImageIOUtil.writeImageQuietly(DEFAULT_IMAGE_PROCESSOR.getImage(), new File("target/"+ RSC+"/thinned.png"));
		

		SVGG g = new SVGG();
		PixelRingList pixelRingList = mainIsland.getOrCreateInternalPixelRings();
		pixelRingList.plotRings(g, PlotTest.FILL);
		File ringsFile = new File("target/" + RSC + "/rings.svg");
		SVGSVG.wrapAndWriteAsSVG(g, ringsFile);
		assert(ringsFile.exists());

		PixelList outline = pixelRingList.get(1).getPixelsTouching(pixelRingList.get(0));
		outline.plotPixels(g, "yellow");
		// this is the outline of the symbol
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/" + RSC
				+ "/outline.svg"));

		// just plots the diamond
		g = new SVGG();
		outline = pixelRingList.get(0).getPixelsWithOrthogonalContactsTo(pixelRingList.get(1), mainIsland);
		outline.plotPixels(g, PlotTest.FILL[0]);
		PixelIsland outlineIsland = PixelIsland.createSeparateIslandWithClonedPixels(outline, true);
		PixelGraph graph = PixelGraph.createGraph(outlineIsland);
		LOG.trace("graph "+graph.getPixelList());
		PixelNodeList nodeList = graph.getNodeList();
//			Assert.assertEquals("nodes", 1, nodeList.size());
		LOG.trace("nodes "+nodeList);
		PixelEdgeList edgeList = graph.getEdgeList();
//			Assert.assertEquals("edges", 1, edgeList.size());
		for (PixelEdge edge : edgeList) {
			EdgeSegments segmentList = edge.getOrCreateSegmentList(2);
//			Assert.assertEquals("segments "+segmentList, 1, segmentList.size());
			g.appendChild(segmentList.getOrCreateSVG());
		}
		
		
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/" + RSC + "/outline2.svg"));
	}

}
