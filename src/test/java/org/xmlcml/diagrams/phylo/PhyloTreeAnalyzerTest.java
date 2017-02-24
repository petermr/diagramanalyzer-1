package org.xmlcml.diagrams.phylo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramAnalyzer;
import org.xmlcml.diagrams.Fixtures;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.SVGText;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.ImageUtil;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;

import net.sourceforge.javaocr.pmr.CharacterInterpreter;
import nu.xom.Element;

public class PhyloTreeAnalyzerTest {

	private final static Logger LOG = Logger.getLogger(PhyloTreeAnalyzerTest.class);
	private static ImageProcessor DEFAULT_IMAGE_PROCESSOR = null;
//	private double segmentTolerance = 2.0;

	@Before
	public void setUp() {
		DEFAULT_IMAGE_PROCESSOR = ImageProcessor.createDefaultProcessor();
		;
	}

	@Test
	public void testImageProcessor() {
		DiagramAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.readAndProcessInputFile(new File(Fixtures.ROSS_DIR, "pone.0016923.g002.png"));
	}
	
	@Test
	public void test16923Read() throws IOException {
		Assert.assertTrue(Fixtures.ROSS_DIR.exists());
		BufferedImage image = ImageIO.read(new File(Fixtures.ROSS_DIR,
				"pone.0016923.g002.png"));
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
		pixelIslandList.sortBySizeDescending();
		// thinned by default
		int size = pixelIslandList.size();
		Assert.assertEquals("size", 226, size);
		Assert.assertEquals("main tree", 12939, pixelIslandList.get(0).size());
		Assert.assertEquals("scale", 383, pixelIslandList.get(1).size());
		size = pixelIslandList.get(2).size();
		Assert.assertTrue("largest character "+size, 93 <= size && size <= 97); // 94/5/6
		size = pixelIslandList.get(225).size();
		Assert.assertTrue("full stop "+size, 1 <= size && size <= 2);
	}

	@Test
	/** well behaved - all nodes identified
	 * 
	 */
	public void test16923GraphsAndText() throws IOException {
		File inputFile = new File(Fixtures.ROSS_DIR, "pone.0016923.g002.png");
		LOG.debug("processing...: ");
		BufferedImage image = DEFAULT_IMAGE_PROCESSOR.processImageFile(inputFile);
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
		pixelIslandList.sortSize();
		pixelIslandList.reverse();
		BufferedImage image0 = pixelIslandList.createImageAtOrigin();
		// thinnned, both tree and characters
		ImageIOUtil.writeImageQuietly(image0, new File(
				"target/phylo/16923.g002.thin.png"));
		// main tree
		LOG.debug("pixel islands: "+pixelIslandList.size());
		SVGG g = new SVGG();
		for (int i = 0; i < 2; i++) {
			PixelIsland island = pixelIslandList.get(i);
     		LOG.debug(island.size());
			g.appendChild(island.createSVG());
			PixelGraph graph = PixelGraph.createGraph(island);
			graph.createAndDrawGraph(g);
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File(
				"target/phylo/16923.g002.graphAndChars0.svg"));
		// OMIT islands 0/1  (tree and scale)
		extractAndDrawChars(inputFile, pixelIslandList, g, 2);
		SVGSVG.wrapAndWriteAsSVG(g, new File(
				"target/phylo/16923.g002.graphAndChars.svg"));

	}


	@Test
	public void test16124GraphsAndText() throws IOException {
		createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0016124.g011", 2, 128);
	}

	@Test
	// sanserif AND serif //fairly sensitive threshold
	public void test16124GraphsAndTextThreshold() throws IOException {
		createGraphsCharsAndPlot(Fixtures.ROSS_DIR, "pone.", "0016124.g011", 2,
				180 /* 220 */);
	}

	

	@Test
	@Ignore // too long
	public void testBoofCVBinarization() throws Exception {
		for (int threshold = 64; threshold < 256; threshold += 64) {
			for (File file : Fixtures.ROSS_DIR.listFiles()) {
				String filename = file.getName();
				if (FilenameUtils.getExtension(filename).equals("png")) {
					String base = FilenameUtils.getBaseName(filename);
					BufferedImage binary = ImageUtil.boofCVBinarization(
							ImageIO.read(file), threshold);
					ImageUtil
							.writeImageQuietly(binary, new File(
									"target/binary/" + base + "_" + threshold
											+ ".png"));
				}
			}
		}
	}
	
	@Test
	@Ignore // replace by segments
	public void test16124Segments() throws Exception {
		createAndDrawPolylines("0016124.g011");
	}

	@Test
	@Ignore // replace by segments
	public void test22249Segments() throws Exception {
		createAndDrawPolylines("0022249.g003");
	}

	@Test
	/** result is still pixelLists, not polyLines
	 * 
	 * @throws Exception
	 */
	public void test36933SegmentsCheckThinning() throws Exception {
		BufferedImage image = ImageIO.read(new File(Fixtures.ROSS_DIR, "pone.0036933.g001.png"));
		image = DEFAULT_IMAGE_PROCESSOR.processImage(image);
		ImageIOUtil.writeImageQuietly(image, new File("target/phylo/0036933.g001/thinned1.png"));
		PixelIslandList islandList = PixelIslandList.createSuperThinnedPixelIslandList(image);
		SVGSVG.wrapAndWriteAsSVG(islandList.getOrCreateSVGG(), new File("target/phylo/0036933.g001/thinned1.svg"));
	}
	
	
	
	@Test
	public void testNexML9() {
		Element root = new Element("root");
		NewickGenerator newickGenerator = new NewickGenerator();
		Element c1 = newickGenerator.createAndAddNewNode(root, "c1");
		Element c2 = newickGenerator.createAndAddNewNode(root, null);
		Element c21 = newickGenerator.createAndAddNewNode(c2, null);
		Element c211 = newickGenerator.createAndAddNewNode(c21, "c211");
		Element c212 = newickGenerator.createAndAddNewNode(c21, "c212");
		Element c22 = newickGenerator.createAndAddNewNode(c2, null);
		Element c221 = newickGenerator.createAndAddNewNode(c22, "c221");
		Element c222 = newickGenerator.createAndAddNewNode(c22, "c222");
		String newick = newickGenerator.makeNewick(root);
		Assert.assertEquals("N",  "(c1,((c211,c212),(c221,c222)));", newick);
	}

	
	/** get newick for tree.
	 * 
	 */
	@Test
	public void testGetNewickThroughCommandLine() throws IOException {
		final String NWK36933 = "target/phylo/36933.nwk";
		File NWK36933_FILE = new File(NWK36933);
		NWK36933_FILE.delete();
		String[] args = {
				"--debug",
				"--input",   new File(Fixtures.ROSS_DIR, "pone.0036933.g001.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--output", "target/phylo/36933/", // output directory (maybe not needed)
				"--newick", NWK36933 // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
		LOG.error("NO FILE WRITTEN");
//		Assert.assertTrue("newick", NWK36933_FILE.exists());
//
//		String nwk = FileUtils.readFileToString(new File(NWK36933));
//		Assert.assertEquals("36933: "+nwk, 
//		"((n113,((n196,n74),((n49,(n109,((((n21,n46),n73),n154),n9))),((n98,n63),((((n79,n123),n78),(n164,n5)),(n33,(n11,n168))))))),((((((n30,n193),(n181,(n177,n102))),((n80,n93),((n40,((n44,n145),(n178,n234))),((n161,n29),n206)))),((((n39,n152),((n116,n235),(n230,(n204,((n71,n59),n82))))),(n20,(n233,n52))),((((n189,n25),(n126,n54)),(n217,((n51,n12),(n223,n195)))),(((n176,(n3,n45)),(n170,(n127,(n94,n220)))),(n57,((n53,(n112,n163)),(n222,n108))))))),(((n107,n89),((n28,n6),n190)),(((n140,(n106,n211)),n65),(n0,((n43,n85),((n159,(n182,n23)),(n31,(n48,(n139,(((n18,n90),n115),(n55,n183))))))))))),((((n128,(n136,(n99,n22))),((n137,n146),(n186,n165))),((n101,(n66,(n212,n133))),(n129,n144))),((n88,(n135,n120)),(n35,((n34,(n24,n84)),((n175,n149),n36)))))));".length(),
//		nwk.length());
		// can't compare trees directly as the order and labels aren't deterministic
//		Assert.assertTrue("36933: "+nwk.length(), 
//		Math.abs("((n113,((n196,n74),((n49,(n109,((((n21,n46),n73),n154),n9))),((n98,n63),((((n79,n123),n78),(n164,n5)),(n33,(n11,n168))))))),((((((n30,n193),(n181,(n177,n102))),((n80,n93),((n40,((n44,n145),(n178,n234))),((n161,n29),n206)))),((((n39,n152),((n116,n235),(n230,(n204,((n71,n59),n82))))),(n20,(n233,n52))),((((n189,n25),(n126,n54)),(n217,((n51,n12),(n223,n195)))),(((n176,(n3,n45)),(n170,(n127,(n94,n220)))),(n57,((n53,(n112,n163)),(n222,n108))))))),(((n107,n89),((n28,n6),n190)),(((n140,(n106,n211)),n65),(n0,((n43,n85),((n159,(n182,n23)),(n31,(n48,(n139,(((n18,n90),n115),(n55,n183))))))))))),((((n128,(n136,(n99,n22))),((n137,n146),(n186,n165))),((n101,(n66,(n212,n133))),(n129,n144))),((n88,(n135,n120)),(n35,((n34,(n24,n84)),((n175,n149),n36)))))));".length() -
//		nwk.length()) < 20);
	}

	/** get newick for tree.
	 * 
	 * this doesn't have root...
	 * 
	 */
	@Test
	public void testGetNewick16923() {
		String[] args = {
				"--debug",
				"--input",   new File(Fixtures.ROSS_DIR, "pone.0016923.g002.png").toString(),   // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--newick", "target/phylo/0016923/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** draw as SVG.
	 * 
	 */
	@Test
	public void testSVG16923() {
		String[] args = {
				"--debug",
				"--input",   new File(Fixtures.ROSS_DIR, "pone.0016923.g002.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/phylo/0016923/test.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--newick", "target/phylo/0016923/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}


	/** get newick for tree.
	 * 
	 * this doesn't have root... we add one
	 * 
	 */
	@Test
	public void testGetNewick94172() {
		String[] args = {
				"--debug",
				"--input",   new File(Fixtures.COMPOUND_DIR, "journal.pone.0094172.g002-2.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/phylo/0094172/test.svg",
				"--newick", "target/phylo/0094172/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** get newick for tree and add distances.
	 * 
	 * this doesn't have root... we add one
	 * also needs large tolerance
	 */
	@Test
	public void testGetDistances() {
		String[] args = {
				"--debug",
				"--input",   new File(Fixtures.MISC_DIR, "small1.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "4.0",
				"--svgfile", "target/phylo/small1/test.svg",
				"--newick", "target/phylo/small1/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** get newick for tree and add distances.
	 * 
	 * this doesn't have root... we add one
	 * 
	 */
	@Test
	public void testGetNewick94172Distances() {
		String[] args = {
				"--debug", 
				"--input",   new File(Fixtures.COMPOUND_DIR, "journal.pone.0094172.g002-2.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--svgfile", "target/phylo/0094172/test.svg",
				"--newick", "target/phylo/0094172/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** get newick for tree and add distances.
	 * 
	 * this has a left root
	 * 
	 */
	@Test
	public void testGetNewick17932Distances() {
		String[] args = {
				"--debug", 
				"--input",   new File(Fixtures.ROSS_DIR, "pone.0017932.g052.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--svgfile", "target/phylo/0017932/test.svg",
				"--newick", "target/phylo/0017932/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}
	

	/** get newick for tree and add distances.
	 * 
	 * this has a left root
	 * 
	 */
	@Test
	// POSSIBLE SHOWCASE
	public void testGetNewickIJSEM174() {
		String[] args = {
//				"--debug 
				"--input",   new File(Fixtures.ROSS_DIR, "ijs.0.000174-0-000.pbm.png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "5.0",
				"--svgfile",  "target/phylo/ijs_000174/test.svg",
				"--newick", "target/phylo/ijs_000174/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}
	
	/** get newick for tree and add distances.
	 * 
	 * this has a left root
	 * 
	 */
	@Test
	public void testGetCrustacean() {
		String[] args = {
				"--debug", 
				"--input",   new File(Fixtures.ROSS_DIR, "out-001.jpg").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "5.0",
				"--svgpixels", "fill:red",
				"--svgfile", "target/phylo/out_001/test.svg", // calculate newick and output
				"--newick", "target/phylo/out_001/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}
	
	/** has stippled areas and hangs on BoofCV binarisation.
	 * 
	 */
	@Test
	@Ignore
	public void testBadIJSEM() throws IOException {
		String root = "ijs.0.02520-0-002.pbm";
		String[] args = {
//					"--debug 
				"--input",   new File("./ijsembad/"+root+".png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "5.0",
				"--newick", "target/phylo/ijsembad/"+root+"_test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/**
	ijs.0.023580-0-001.pbm.png
	ijs.0.002931-0-000.pbm.png
	ijs.0.02055-0-001.pbm.png
	ijs.0.019737-0-000.pbm.png
	ijs.0.002683-0-000.pbm.png
	ijs.0.003152-0-001.pbm.png
	ijs.0.005413-0-001.pbm.png
	ijs.0.016626-0-000.pbm.png
	ijs.0.017962-0-000.pbm.png
	ijs.0.022202-0-000.pbm.png
	ijs.0.02319-0-001.pbm.png
	ijs.0.024356-0-000.pbm.png
	ijs.0.025007-0-000.pbm.png
	ijs.0.023168-0-000.pbm.png
	ijs.0.02466-0-000.pbm.png
	ijs.0.000588-0-000.pbm.png
	ijs.0.000794-0-000.pbm.png
	ijs.0.000968-0-000.pbm.png
	ijs.0.001719-0-001.pbm.png
	ijs.0.004838-0-000.pbm.png
	ijs.0.005421-0-000.pbm.png
	ijs.0.006247-0-001.pbm.png
	ijs.0.007914-0-001.pbm.png
	ijs.0.013367-0-000.pbm.png
	ijs.0.014720-0-000.pbm.png
	ijs.0.015339-0-003.pbm.png
	ijs.0.016915-0-000.pbm.png
	ijs.0.018622-0-000.pbm.png
	ijs.0.019729-0-000.pbm.png
	ijs.0.024489-0-000.pbm.png
	*/

	/** very small output
	 * 
	 * @param xml
	 * @return
	 */
	@Test
	@Ignore
	public void testSmallIJSEM() throws IOException {
		String root = "ijs.0.02520-0-002.pbm";
		String[] args = {
//					"--debug 
				"--input",   new File("./ijsembad/"+root+".png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "5.0",
				"--newick", "target/phylo/ijsembad/"+root+"_test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}
	
	/** test lengths
	 * 
	 * @param xml
	 * @return
	 */
	@Test
	public void testLengths() throws IOException {
		String[] args = {
//					"--debug 
				"--input", new File(Fixtures.ROSS_DIR, "6taxabalance.png").toString(),
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--svgfile", "target/phylo/6taxabalance/test.svg",
				"--svgpixels", "fill:black",
				"--newick", "target/phylo/6taxabalance/test.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
		// approx (541_29:4.2,(((542_278:1.0,541_341:1.0):1.1,542_216:2.1):1.0,(541_153:1.1,541_91:1.1):2.1):1.0);
	}
	
	/** check trees which appear damaged in translation
	 * 
	 * mainly trees which have fewer nodes than expected. Here we check the 
	 * output visually
	 * 
	 * @param xml
	 * @return
	 */
	@Test
	public void testProblemTrees() {
		// one zerolength tip
			String[] args = {
					"--debug",
					"--input",   new File(Fixtures.PROBLEM_DIR, "ijs.0.000620-0-002.pbm.png").toString(),  // source image
					"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
					"--root", "left",
					"--lengths",
					"--tolerance", "4.0",
					"--svgfile", "target/phylo/000620-0-002/test.svg",
					"--newick", "target/phylo/000620-0-002/test.nwk" // calculate newick and output
					};
			DiagramAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
			phyloTree.parseArgsAndRun(args);

	}
	
	@Test
	public void testProblemTrees1() {
		// BUG IN NEWICK - // FIXME
			String[] args = {
					"--debug",
					"--input",   new File(Fixtures.PROBLEM_DIR, "ijs.0.000737-0-001.pbm.png").toString(),  // source image
//					"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
					"--root", "left",
					"--lengths",
					"--tolerance", "4.0",
					"--svgfile", "target/phylo/000737-0-001/test.svg",
					"--newick", "target/phylo/000737-0-001/test.nwk" // calculate newick and output
					};
			PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
			phyloTree.parseArgsAndRun(args);

	}

	@Test
	public void testProblemTrees2() {
		// BUG NO NWK OUTPUT AT ALL
		String BASE = "000802-0-002";
			String[] args = {
					"--debug",
					"--input",   new File(Fixtures.PROBLEM_DIR, "ijs.0."+BASE+".pbm.png").toString(),  // source image
					"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
					"--root", "left",
					"--lengths",
					"--tolerance", "4.0",
					"--svgfile", "target/phylo/"+BASE+"/test.svg",
					"--newick", "target/phylo/"+BASE+"/test.nwk" // calculate newick and output
					};
			PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
			phyloTree.parseArgsAndRun(args);

	}

	@Test
	public void testProcessText() {
		String root = "ijs.0.014811-0-002.pbm";
		String[] args = {
				"--input",   new File("./ijsemopen/"+root+".png").toString(),  // source image
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--lengths",
				"--tolerance", "5.0",
				"--newick", "target/phylo/ijsembad/"+root+"_test.nwk", // calculate newick and output
				"--ocr",
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);

	}


	// =========================
	
	// only used in two ignored tests
	private PixelGraph createAndDrawPolylines(String name) throws IOException {
		PixelGraph pixelGraph = MiscPhyloTest.createImageAndEdges(name).get(0);
		SVGG g = pixelGraph.createSegmentedEdges();
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/phylo/"+name+"/segments.svg"));
		return pixelGraph;
	}

	/** utility plotter.
	 * 
	 * @param dir
	 * @param prefix
	 * @param root
	 * @param start
	 * @param threshold
	 * @throws IOException
	 */
	public static void createGraphsCharsAndPlot(File dir, String prefix, String root,
			int start, int threshold) throws IOException {
		File inputFile = new File(dir, prefix + root + ".png");
		if (!inputFile.exists()) {
			throw new RuntimeException("file does not exist: " + inputFile);
		}
		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(inputFile).getOrCreatePixelIslandList();
		BufferedImage image0 = pixelIslandList.createImageAtOrigin();
		ImageIOUtil.writeImageQuietly(image0, new File("target/phylo/" + root
				+ ".thin.png"));
		pixelIslandList.thinThickStepsOld();
		// main tree
		SVGG g = new SVGG();
		for (int i = 0; i < start; i++) {
			PixelIsland island = pixelIslandList.get(i);
			g.appendChild(island.createSVG());
			PixelGraph graph = PixelGraph.createGraph(island);
			graph.createAndDrawGraph(g);
		}
		extractAndDrawChars(inputFile, pixelIslandList, g, start);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/phylo/" + root
				+ ".graphAndChars.svg"));
	}

	/** utility test and plot.
	 * 
	 * @param inputFile
	 * @param pixelIslandList
	 * @param g
	 * @param start
	 * @throws IOException
	 */
	public static void extractAndDrawChars(File inputFile, PixelIslandList pixelIslandList, SVGG g, int start)
			throws IOException {
		BufferedImage imageRaw = ImageIO.read(inputFile);
		imageRaw = ImageUtil.boofCVBinarization(imageRaw, 128);
		CharacterInterpreter interpreter = new CharacterInterpreter();
		interpreter.loadTrainingImages(new File(
				"src/main/resources/fonts/trainingImages/"));
		for (int i = start; i < pixelIslandList.size(); i++) {
			BufferedImage image0 = null;
			Int2Range ibbox = null;
			try {
				PixelIsland pixelIsland = pixelIslandList.get(i);
				Real2Range bbox = pixelIsland.getBoundingBox();
				ibbox = new Int2Range(pixelIsland.getBoundingBox());
				if (ibbox.getXRange().getRange() == 0
						|| ibbox.getYRange().getRange() == 0)
					continue; // zero width?
				image0 = ImageUtil.clipSubImage(imageRaw, ibbox);
				if (image0 == null)
					continue;
				String result = "";
				if (image0.getWidth() == 0 || image0.getHeight() == 0) {
					result = "."; // thin stuff
				} else {
					result = interpreter.scanBufferedImage(image0, null);
				}
				SVGText text = new SVGText(bbox.getCorners()[0], result);
				text.setFontSize(40.);
				g.appendChild(text);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(ibbox + "; " + image0);
				LOG.error("Cannot read scan w/h/bbox: " + image0.getWidth()
						+ "/" + image0.getHeight() + "; " + ibbox + "; " + e
						+ " " + i);
			}
		}
	}

	
}
