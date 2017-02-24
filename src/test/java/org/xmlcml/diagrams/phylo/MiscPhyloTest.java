package org.xmlcml.diagrams.phylo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.diagrams.Fixtures;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.processing.ZhangSuenThinning;

/** class to explore examples but not a regression test
 * 
 * normally commented out.
 * 
 * @author pm286
 *
 */
@Ignore
public class MiscPhyloTest {

	private final static Logger LOG = Logger.getLogger(MiscPhyloTest.class);
	
	private static ImageProcessor DEFAULT_IMAGE_PROCESSOR;

	@Before
	public void setUp() {
		DEFAULT_IMAGE_PROCESSOR = new ImageProcessor();
	}
	
	@Test
	// BEETLE pics
	public void test16662GraphsAndTextThreshold() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0016662.g001", 2,
				180 /* 220 */);
	}

	@Test
	// some italic
	public void test17932GraphsAndText() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0017932.g051", 2,
				180);
	}

	@Test
	// tree and madagascar
	public void test2718GraphsAndText() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.MISC_DIR, "",
				"14153252278_41a2718f1c_o", 2, 240);
	}

	@Test
	public void testMadagascarGraphsAndText() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.MISC_DIR, "", "madagascar", 2, 240);
	}

	@Test
	// some italic + simple tree
	public void test17170GraphsAndText() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0017170.g002",
				30, 180);
	}

	@Test
	/** well behaved - all nodes identified.
	 * 
	 * interpret tree
	 * */
	public void test16923Tree() throws IOException {
		PixelIslandList pixelIslandList = PixelIslandList.createSuperThinnedPixelIslandList(
				DEFAULT_IMAGE_PROCESSOR.processImageFile(new File(Fixtures.ROSS_DIR, "pone.0016923.g002.png")));
		PixelIsland island = pixelIslandList.get(0);
		Assert.assertEquals("largest", 123, island.size());
		PixelGraph graph = PixelGraph.createGraph(island);

	}

	@Test
	public void test16923_003Tree() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0016923.g002", 2,
				180);
	}

	@Test
	// pastel blocks
	public void test22249Tree() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0022249.g003", 2,
				100);
	}

//	@Test
//	// pastel blocks
//	public void test22249TreeEdges() throws IOException {
//		PhyloTreeTest.createGraphsCharsAndPlotEdges(Fixtures.ROSS_DIR, "pone.", "0022249.g003", 1,
//				100);
//	}

	@Test
	public void test22249TreeEdges1() throws IOException {
		String name = "0022249.g003";
		File outputDir = new File("target/plot/"+name+"/");
		File inputFile = new File(Fixtures.ROSS_DIR, "pone." + name + ".png");
		if (!inputFile.exists()) {
			throw new RuntimeException("file does not exist: " + inputFile);
		}
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(inputFile).getOrCreatePixelIslandList();
		
		pixelIslandList.analyzeEdgesAndPlot();
	}

	@Test
	public void test22249TreeEdgesAndNodes() throws IOException {
		String name = "0022249.g003";
		File outputDir = new File("target/plot/"+name+"/");
		File inputFile = new File(Fixtures.ROSS_DIR, "pone." + name + ".png");
		if (!inputFile.exists()) {
			throw new RuntimeException("file does not exist: " + inputFile);
		}
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(inputFile).getOrCreatePixelIslandList();
		// 1 island
		pixelIslandList.analyzeEdgesAndPlot();
	}


	@Test
	// serif
	public void test18360Tree() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0018360.g001", 2,
				240);
	}

	@Test
	// circular
	public void test31695TreeEdges() throws IOException {
		createImageAndEdges("0031695.g001");
		
	}

	@Test
	// circular with rotated text
	public void test36933Tree() throws IOException {
		createImageAndEdges("0036933.g001");
	}

	@Test
	// 
	public void test16124Tree() throws IOException {
		createImageAndEdges("0016124.g011");
	}

	@Test
	// gray and dark gray areas
	public void test63008Tree() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0063008.g001", 2,
				30);
	}

	@Test
	public void test86675Tree() throws IOException {
		PhyloTreeAnalyzerTest.createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0086675.g002", 2,
				180);
	}

	
	
	// =========================
	
	static List<PixelGraph> createImageAndEdges(String name) throws IOException {
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		File inputFile = new File(Fixtures.ROSS_DIR, "pone." + name + ".png");
		LOG.trace(inputFile+" "+inputFile.exists());
		phyloTree.setInputFile(inputFile);
		phyloTree.setNewickFile(new File("target/plot/"+name+"/newick.nwk"));
		phyloTree.setThinning(new ZhangSuenThinning());
		phyloTree.processImageFile();
		ImageIOUtil.writeImageQuietly(phyloTree.getImage(), new File("target/"+name+"/"+"firstThin.png"));
		PixelIslandList pixelIslandList = phyloTree.getOrCreatePixelIslandList();
//		pixelIslandList.setParameters(phyloTree.getImageProcessor().getParameters());
		LOG.debug("parameters "+pixelIslandList.getParameters());
		LOG.debug("islands: "+pixelIslandList.size());
		for (int i = 0; i < Math.min(10, pixelIslandList.size()); i++) {
			LOG.debug("i: "+pixelIslandList.get(i));
		}
		SVGSVG.wrapAndWriteAsSVG(pixelIslandList.get(0).getSVGG(), new File("target/misc/"+name+"/fill_0.svg"));
		SVGSVG.wrapAndWriteAsSVG(pixelIslandList.getOrCreateSVGG(), new File("target/misc/"+name+"/fill.svg"));
		
		List<PixelGraph> pixelGraphList = pixelIslandList.analyzeEdgesAndPlot();
		return pixelGraphList;
	}

}
