package org.xmlcml.diagrams.molecules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.diagrams.phylo.PhyloTreeAnalyzerTest;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;

public class MoleculeTest {

	private final static Logger LOG = Logger.getLogger(MoleculeTest.class);

	static ImageProcessor DEFAULT_IMAGE_PROCESSOR = null;

	@Before
	public void setUp() {
		DEFAULT_IMAGE_PROCESSOR = ImageProcessor.createDefaultProcessor();;
	}

	@Test
	/** well behaved - all nodes identified*/
	
	public void testPostermolGraphsAndText() throws IOException {
		File inputFile = new File(DiagramsFixtures.PROCESSING_DIR, "postermol.png");
		BufferedImage image0 = ImageIO.read(inputFile);
		ImageIOUtil.writeImageQuietly(image0, new File("target/molecules/postermolRaw.png"));
		PixelIslandList pixelIslandList = null;
//		for (int thresh = 70; thresh <= 130; thresh+=20) {
//			DEFAULT_IMAGE_PROCESSOR.setThreshold(thresh);
//			BufferedImage image = DEFAULT_IMAGE_PROCESSOR.processImage(image0);
//			ImageIOUtil.writeImageQuietly(image, new File("target/molecules/postermol_"+thresh+"Raw1.png"));
//			pixelIslandList = PixelIslandList.createPixelIslandList(image);
//			LOG.debug(thresh+": "+pixelIslandList.size());
//			pixelIslandList.removeStepsSortAndReverse();
//			BufferedImage image00 = pixelIslandList.createImageAtOrigin();
//			ImageIOUtil.writeImageQuietly(image00, new File("target/molecules/postermol_"+thresh+".thin.png"));
//		}
		DEFAULT_IMAGE_PROCESSOR.setThreshold(70); // this is very sensitive
		BufferedImage image = DEFAULT_IMAGE_PROCESSOR.processImage(image0);
		pixelIslandList = DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		pixelIslandList.thinThickStepsOld();
		int size = pixelIslandList.size();
		Assert.assertTrue("islands "+size, 12 <= size && size <= 14 );
			// main tree
		int nGraphs = Math.min(20, pixelIslandList.size());
		SVGG g = new SVGG();
		for (int i = 0; i < nGraphs; i++) {
			PixelIsland island = pixelIslandList.get(i);
			g.appendChild(island.createSVG());
			PixelGraph graph = PixelGraph.createGraph(island);
			graph.createAndDrawGraph(g);
		}
		PhyloTreeAnalyzerTest.extractAndDrawChars(inputFile, pixelIslandList, g, 2);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/postermol.graphAndChars.svg"));

	}

	@Test
	public void testNatprod1GraphsAndText() throws IOException {
		// already processed
		File inputFile = new File(DiagramsFixtures.PROCESSING_DIR, "natprod1.png");
		BufferedImage image = ImageIO.read(inputFile);
		ImageIOUtil.writeImageQuietly(image, new File("target/molecules/natprod1Raw.png"));
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
		LOG.debug(pixelIslandList.size());
		pixelIslandList.thinThickStepsOld();
		BufferedImage image0 = pixelIslandList.createImageAtOrigin();
		ImageIOUtil.writeImageQuietly(image0, new File("target/molecules/natprod.thin.png"));
		SVGG g = new SVGG();
		int ngraph = pixelIslandList.size();
		ngraph = 3;
		for (int i = 0; i < ngraph; i++) {
			PixelIsland island = pixelIslandList.get(i);
			g.appendChild(island.createSVG());
			PixelGraph graph = PixelGraph.createGraph(island);
			graph.createAndDrawGraph(g);
		}
		PhyloTreeAnalyzerTest.extractAndDrawChars(inputFile, pixelIslandList, g, 2);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/natprod1.graphAndChars.svg"));

	}


}
