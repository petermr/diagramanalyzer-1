package org.xmlcml.diagrams.molecules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.euclid.Int2;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.IntArray;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.graphics.svg.util.ColorStore;
import org.xmlcml.graphics.svg.util.ColorStore.ColorizerType;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelEdge;
import org.xmlcml.image.pixel.PixelEdgeList;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelNodeList;
import org.xmlcml.image.pixel.PixelRingList;
import org.xmlcml.image.pixel.PixelSegment;
import org.xmlcml.image.pixel.EdgeSegments;

public class MolecularAnalyzerTest {

	private static final String BLACK = "black";
	private static final String BAD = "bad";
	private static final String DOT_SVG = ".svg";
	private static final String RED = "red";
	private static final String YELLOW = "yellow";
	private static final String CYAN = "cyan";

	private final static Logger LOG = Logger.getLogger(MolecularAnalyzerTest.class);
	
	private final static File TREC = new File(DiagramsFixtures.MOLECULE_DIR, "TREC2011testset");
	private final static File TREC_MINI = new File(DiagramsFixtures.MOLECULE_DIR, "TRECmini");
	private final static File TREC_OUT = new File("target/molecules/TREC2011testset");
	
	private final static String[] STROKES = {RED, "green", "blue", "orange", "magenta", "brown", BLACK, "pink"};

	@Before
	public void setUp() {
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR = ImageProcessor.createDefaultProcessor();;
	}

	@Test
	public void testMolecularAnalyzer() {
		double tolerance = 0.8;
		MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
		File[] files = DiagramsFixtures.MOLECULE_DIR.listFiles();
		File imageFile = new File(DiagramsFixtures.MOLECULE_DIR, "TRECmini/US06335364-20020101-C00020.png");
		molecularAnalyzer.makeMolecule(imageFile);
		PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
		SVGG g = new SVGG();
		g.appendChild(pixelIslandList.getOrCreateSVGG());
		Assert.assertEquals(8, pixelIslandList.size());
		
		IntArray edgeSizeArray = new IntArray(new int[]{4,7,1,1,1,1,2,5});
		int iedge = 0;
		Iterator<String> iterator = ColorStore.getColorIterator(ColorizerType.CONTRAST);
		for (PixelIsland island : pixelIslandList) {
			PixelGraph graph = new PixelGraph(island);
			PixelEdgeList edgeList = graph.getEdgeList();
			Assert.assertEquals(edgeSizeArray.elementAt(iedge), edgeList.size());
			LOG.debug("===="+iedge+"====");
			for (PixelEdge edge : edgeList) {
				EdgeSegments segments = edge.getOrCreateSegmentList(tolerance);
				LOG.debug("segmentCount: "+segments.size());
				SVGG segG = segments.getOrCreateSVG();
				segG.setCSSStyle("stroke-width:1.0;stroke:"+iterator.next()+";");
				g.appendChild(segG);
			}
			iedge++;
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/C00020.svg"));
	}
	
	@Test
	public void testWedges() {
		testMolecularAnalyzerList(Arrays.asList(new File[]{new File(DiagramsFixtures.MOLECULE_DIR, "multiwedge.png")}));
	}

	@Test
	/**
	 * analyse 23 images of molecules and fragments from TREC.
	 * 
	 * the output is shown in graphic form in target/molecules/
	 * 
	 * This has bugs for some molecules
	 */
	public void testMolecularAnalyzerList() {
		testMolecularAnalyzerList(new ArrayList<File>(FileUtils.listFiles(TREC_MINI, new String[]{"png"}, false)));
	}
	
	public void testMolecularAnalyzerList(List<File> files) {
		for (File imageFile : files) {
			MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
			molecularAnalyzer.makeMolecule(imageFile);
			PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
			String fileName = imageFile.getName();
			SVGG g = new SVGG();
			SVGG mol = pixelIslandList.getOrCreateSVGG();
			g.appendChild(mol);
			for (PixelIsland island : pixelIslandList) {
				createEdgesAsLines(island);
			}
			SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/"+fileName+DOT_SVG));
		}
	}

	private SVGG createEdgesAsLines(PixelIsland island) {
		SVGG g = new SVGG();
		PixelGraph graph = new PixelGraph(island);
		PixelEdgeList edgeList = graph.getEdgeList();
		for (PixelEdge edge : edgeList) {
			SVGG edgeSVG = edge.getOrCreateSVG();
			g.appendChild(edgeSVG);
			for (PixelSegment segment : edge.getOrCreateSegmentList(6.0, 19, 0.7)) {
				SVGLine line = segment.getSVGLine();
				g.appendChild(line);
			}
		}
		return g;
	}

	@Test
	/**
	 * analyse lots of images of molecules and fragments from TREC.
	 * 
	 * the output is shown in graphic form in target/molecules/
	 * 
	 * This has bugs for some molecules
	 */
	@Ignore // too large
	public void testTREC2011() throws Exception {
		Int2 maxTextSize = new Int2(20, 20);
		File[] dirs = new File("TREC2011testset").listFiles();
		for (File dir : dirs) {
			if (!dir.isDirectory()) continue;
			if (dir.getName().equals(BAD)) continue;
			List<File> files = new ArrayList<File>(FileUtils.listFiles(dir, new String[]{"png"}, false));
				for (File imageFile : files) {
				MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
				try {
					molecularAnalyzer.makeMolecule(imageFile);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("*****************************************   "+imageFile.getAbsolutePath()+"");
					throw(e);
//					continue;
				}
				PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
				String fileName = imageFile.getName();
				LOG.trace("-----"+dir+"/"+fileName+"----- islandList "+pixelIslandList.toString());
				SVGG g = new SVGG();
				SVGG mol = pixelIslandList.getOrCreateSVGG();
				mol.setStroke(CYAN);
				mol.setFill(CYAN);
				g.appendChild(mol);
				int ncolor = 0;
				for (PixelIsland island : pixelIslandList) {
					SVGG islandSVG = island.getOrCreateSVGG();
					islandSVG.setFill(YELLOW);
					islandSVG.setStroke(YELLOW);
					g.appendChild(islandSVG);
					Int2Range ibox = island.getIntBoundingBox();
					if ((ibox.getXRange().getRange() > maxTextSize.getX() || ibox.getYRange().getRange() > maxTextSize.getY()) || true) {
						ncolor++;
						String stroke = STROKES[ncolor % STROKES.length];
						PixelGraph graph = new PixelGraph(island);
						LOG.trace("==========================");
						LOG.trace("island: "+island+"; graph: "+graph.toString());
						PixelEdgeList edgeList = graph.getEdgeList();
						for (PixelEdge edge : edgeList) {
							SVGG edgeSVG = edge.getOrCreateSVG();
							edgeSVG.setFill(RED);
							edgeSVG.setStroke(RED);
							g.appendChild(edgeSVG);
							for (PixelSegment segment : edge.getOrCreateSegmentList(2.0)) {
								SVGLine line = segment.getSVGLine();
								line.setStroke(stroke);
								line.setStrokeWidth(2.);
								g.appendChild(line);
							}
						}
					}
				}
				SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/"+dir+"/"+fileName+DOT_SVG));
			}
		}
	}

	@Test
	/**
	 * analyse bad images of molecules and fragments from TREC.
	 * 
	 * the output is shown in graphic form in target/molecules/file-name
	 * 
	 */
	public void testBadTREC() {
		File imageFile = new File(DiagramsFixtures.MOLECULE_DIR, "TREC2011testset/bad/US20070099895A1-20070503-C00104.png");
		MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
		molecularAnalyzer.makeMolecule(imageFile);
		PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
		SVGG g = outputMoleculeAndExtractSegments(pixelIslandList);
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/"+imageFile.getName()+DOT_SVG));
	}

	private SVGG outputMoleculeAndExtractSegments(PixelIslandList pixelIslandList) {
		SVGG g = new SVGG();
		SVGG mol = pixelIslandList.getOrCreateSVGG();
		mol.setStroke(CYAN);
		mol.setFill(CYAN);
		g.appendChild(mol);
		int ncolor = 0;
		for (PixelIsland island : pixelIslandList) {
			SVGG islandSVG = island.getOrCreateSVGG();
			islandSVG.setFill(YELLOW);
			islandSVG.setStroke(YELLOW);
			g.appendChild(islandSVG);
			ncolor++;
			String stroke = STROKES[ncolor % STROKES.length];
			PixelGraph graph = new PixelGraph(island);
			LOG.trace("island: "+island+"; graph: "+graph.toString());
			PixelEdgeList edgeList = graph.getEdgeList();
			for (PixelEdge edge : edgeList) {
				SVGG edgeSVG = edge.getOrCreateSVG();
				edgeSVG.setFill(RED);
				edgeSVG.setStroke(RED);
				g.appendChild(edgeSVG);
				for (PixelSegment segment : edge.getOrCreateSegmentList(2.0)) {
					SVGLine line = segment.getSVGLine();
					line.setStroke(stroke);
					line.setStrokeWidth(2.);
					g.appendChild(line);
				}
			}
		}
		return g;
	}

	@Test
	/**
	 * 2 3-nodes , 4 1-nodes an O and H
	 *  
	 * has bug 
	 * 
	 */
	public void testFailingMolecularGraphs() {
		String fileName = "US06358960-20020319-C00440.png";
		File imageFile = new File(TREC_MINI, fileName);
		MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
		molecularAnalyzer.makeMolecule(imageFile);
		PixelIslandList pixelIslandList = molecularAnalyzer.getOrCreatePixelIslandList();
		LOG.trace("-----"+fileName+"----- islandList "+pixelIslandList.toString());
		SVGG g = new SVGG();
		SVGG mol = pixelIslandList.getOrCreateSVGG();
		mol.setStroke(CYAN);
		mol.setFill(CYAN);
		g.appendChild(mol);
		int ncolor = 0;
		for (PixelIsland island : pixelIslandList) {
			SVGG islandSVG = island.getOrCreateSVGG();
			islandSVG.setFill(YELLOW);
			islandSVG.setStroke(YELLOW);
			g.appendChild(islandSVG);
			ncolor++;
			String stroke = STROKES[ncolor % STROKES.length];
			PixelGraph graph = new PixelGraph(island);
			g.appendChild(graph.getNodeList().getOrCreateSVG());
			LOG.trace("==========================");
			LOG.trace("island: "+island+"; graph: "+graph.toString());
			PixelEdgeList edgeList = graph.getEdgeList();
			for (PixelEdge edge : edgeList) {
				SVGG edgeSVG = edge.getOrCreateSVG();
				edgeSVG.setFill(RED);
				edgeSVG.setStroke(RED);
				g.appendChild(edgeSVG);
				for (PixelSegment segment : edge.getOrCreateSegmentList(2.0)) {
					SVGLine line = segment.getSVGLine();
					line.setStroke(stroke);
					line.setStrokeWidth(2.);
					g.appendChild(line);
				}
			}
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/molecules/"+fileName+DOT_SVG));
	}
	
	/** 
	 * 
	 * Looks for solid islands in line structure (wedges/acute-triangles in chemistry).
	 * 
	 * There are 7-8 islands including several characters
	 */
	@Test
	public void testSolidIslands() throws IOException {
		File rawfile = new File(TREC, "06/US06376484-20020423-C00698.png");
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = MoleculeTest.DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		LOG.trace("size "+islandList.size());
		islandList.sortBySizeDescending();
		for (int i = 0; i < islandList.size(); i++) {
			PixelIsland island = new PixelIsland(islandList.get(i));
			LOG.trace("..size "+island.size());
			
			SVGG g = new SVGG();
			PixelRingList pixelRingList = island.getOrCreateInternalPixelRings();
			for (int j = 0; j < pixelRingList.size(); j++) {
				int col = j % DiagramsFixtures.FILL.length;
				pixelRingList.get(j).plotPixels(g, DiagramsFixtures.FILL[col]);
			}
			SVGSVG.wrapAndWriteAsSVG(g, new File(TREC_OUT+"/06a/US06376484-20020423-C00698_"+i+DOT_SVG));
			int ring = 1;
			if (pixelRingList.size() > ring) {
				PixelList outline = pixelRingList.get(ring).getPixelsTouching(pixelRingList.get(0));
				SVGG gg = new SVGG();
				outline.plotPixels(g, BLACK);
				outline.plotPixels(gg, BLACK);
				PixelGraph graph = new PixelGraph(outline, island);
				PixelEdgeList edgeList = graph.getEdgeList();
				for (PixelEdge edge : edgeList) {
					LOG.trace("**************************"+edge.getOrCreateSegmentList(2.0)+"; "+edge.getPixelList().size());
				}
//				SVGG gg = edgeList.get(0).createLineSVG();
				// this is the outline of the symbol
				SVGSVG.wrapAndWriteAsSVG(gg, new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_"+i+DOT_SVG));
			}
		}
	}
	
	/**
	 * Analyzes a single wedge (acute triangle) as a hollow polygon.
	 * 
	 * See testSolidIslands for context...
	 * 
	 */
	@Test
	public void testWedgeThin() throws IOException {
		File rawfile = new File(TREC, "06/US06376484-20020423-C00698.png");
		// UNTHINNED
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = MoleculeTest.DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		islandList.sortBySizeDescending();
		PixelIsland island0 = new PixelIsland(islandList.get(0)); // largest
		PixelRingList pixelRingList = island0.getOrCreateInternalPixelRings();
		int ncolor = 0;
		SVGG g = new SVGG();
		for (PixelList ring : pixelRingList) {
			g.appendChild(ring.plotPixels(STROKES[ncolor % STROKES.length]));
			ncolor++;
		}
		SVGSVG.wrapAndWriteAsSVG(g, new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_colours.svg"));
		
		int ringNumber = 2;
		PixelList ringPixels = pixelRingList.get(ringNumber);
		PixelIsland ringIsland = PixelIsland.createSeparateIslandWithClonedPixels(ringPixels, true);
		ringIsland.setDiagonal(true);
		ringIsland.doSuperThinning();
		SVGG gg = new SVGG();
		SVGSVG.wrapAndWriteAsSVG(gg, new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_ring2thin.svg"));
		gg = new SVGG();
		gg.appendChild(ringIsland.getOrCreateSVGG());
		PixelGraph graph = new PixelGraph(ringPixels, ringIsland);
		PixelNodeList nodeList = graph.getNodeList();
		LOG.trace("nodeList "+nodeList+"; "+ringPixels);
		PixelEdgeList edgeList = graph.getEdgeList();
		LOG.trace("edgeList "+edgeList);
		Assert.assertNotNull(edgeList);
		PixelEdge edge = edgeList.get(0);
		LOG.trace("**************************"+edge.getOrCreateSegmentList(2.0)+"; "+edge.getPixelList().size());
		// this is the outline of the symbol
		SVGSVG.wrapAndWriteAsSVG(gg, new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_ring2.svg"));
	}
	
	/**
	 * Analyzes a single wedge (acute triangle) as a hset of touching rings.
	 * 
	 * See testSolidIslands for context...
	 * 
	 */
	@Test
	public void testWedgeRings() throws IOException {
		File rawfile = new File(TREC, "06/US06376484-20020423-C00698.png");
		// UNTHINNED
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = MoleculeTest.DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		islandList.sortBySizeDescending();
		PixelIsland island0 = new PixelIsland(islandList.get(0)); // largest
		PixelRingList pixelRingList = island0.getOrCreateInternalPixelRings();
		SVGG g = new SVGG();
		PixelList[] outerPixels = new PixelList[pixelRingList.size() - 1];
		PixelList[] innerPixels = new PixelList[pixelRingList.size() - 1];
		PixelList[] orthogonalContacts = new PixelList[pixelRingList.size() - 1];
		int outerCount[] =      {1028,327,127,87,42};
		int innerCount[] =      {327, 127,87, 42,9 };
		int orthogonalCount[] = {470, 114,80, 44,18};
		for (int ringNumber = 0; ringNumber < pixelRingList.size() - 1; ringNumber++) {
			outerPixels[ringNumber] = pixelRingList.get(ringNumber);
			innerPixels[ringNumber] = pixelRingList.get(ringNumber + 1);
			orthogonalContacts[ringNumber] = outerPixels[ringNumber].getPixelsWithOrthogonalContactsTo(innerPixels[ringNumber], island0);
			Assert.assertEquals("outer", outerCount[ringNumber], outerPixels[ringNumber].size());
			Assert.assertEquals("inner", innerCount[ringNumber], innerPixels[ringNumber].size());
			Assert.assertEquals("touching", orthogonalCount[ringNumber], orthogonalContacts[ringNumber].size());
			g.appendChild(orthogonalContacts[ringNumber].plotPixels(STROKES[ringNumber]));
		}
		SVGSVG.wrapAndWriteAsSVG(g, 
				new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_rings_Orthogonal.svg"));
	}
	
	
	/**
	 * Analyzes a single wedge (acute triangle) as a set of touching rings.
	 * 
	 * See testSolidIslands for context...
	 * 
	 */
	@Test
	public void testWedgeRingSegments() throws IOException {
		File rawfile = new File(TREC, "06/US06376484-20020423-C00698.png");
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = MoleculeTest.DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		islandList.sortBySizeDescending();
		PixelIsland island0 = new PixelIsland(islandList.get(0)); // largest
		PixelRingList pixelRingList = island0.getOrCreateInternalPixelRings();
		PixelList thinnedRing12 = pixelRingList.get(1).getPixelsWithOrthogonalContactsTo(pixelRingList.get(2), island0);
		PixelIsland thinnedIsland12 = PixelIsland.createSeparateIslandWithClonedPixels(thinnedRing12, true);
		SVGG g = new SVGG();
		g.appendChild(thinnedIsland12.getOrCreateSVGG());
		
		Assert.assertEquals("thinned island", 114, thinnedIsland12.size());
		PixelGraph outerThinnedGraph = PixelGraph.createGraph(thinnedIsland12);
		PixelEdgeList thinnedEdgeList = outerThinnedGraph.getEdgeList();
		Assert.assertEquals("edges", 2, thinnedEdgeList.size()); // because there is a cycle
		int i = 0;
		int[] segmentCount ={2,3};
		for (PixelEdge thinnedEdge : thinnedEdgeList) {
			EdgeSegments segmentList = thinnedEdge.getOrCreateSegmentList(2.0);
//			Assert.assertEquals("segments", segmentCount[i], segmentList.size());
			g.appendChild(segmentList.getSVGG());
			i++;
		}
		SVGSVG.wrapAndWriteAsSVG(g, 
				new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_thinned12_.svg"));
		
		PixelList thinnedRing01 = pixelRingList.get(0).getPixelsWithOrthogonalContactsTo(pixelRingList.get(1), island0);
		PixelIsland thinnedIsland01 = PixelIsland.createSeparateIslandWithClonedPixels(thinnedRing01, true);
		g = new SVGG();
		g.appendChild(thinnedIsland01.getOrCreateSVGG());
		
		Assert.assertEquals("thinned island", 470, thinnedIsland01.size());
		outerThinnedGraph = PixelGraph.createGraph(thinnedIsland01);
		thinnedEdgeList = outerThinnedGraph.getEdgeList();
		Assert.assertEquals("edges", 1, thinnedEdgeList.size());
		for (PixelEdge thinnedEdge : thinnedEdgeList) {
			EdgeSegments segmentList = thinnedEdge.getOrCreateSegmentList(2.0);
//			Assert.assertEquals("segments", 1, segmentList.size());
			g.appendChild(segmentList.getSVGG());
		}
		SVGSVG.wrapAndWriteAsSVG(g, 
				new File(TREC_OUT+"/06a/US06376484-20020423-C00698_OUTLINE_thinned01_.svg"));
		
		
	}
	
	/** as above but dropping minor artefactual islands
	 * 
	 */
	@Test
	public void testWedgeRingSegmentsWithoutMinorIslands() throws IOException {
		File rawfile = new File(TREC, "06/US06376484-20020423-C00698.png");
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		MoleculeTest.DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = MoleculeTest.DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		islandList.sortBySizeDescending();
		PixelIsland island0 = new PixelIsland(islandList.get(0)); // largest
		PixelRingList pixelRingList = island0.getOrCreateInternalPixelRings();
		LOG.trace("pixelRings "+pixelRingList.size());
		pixelRingList.removeMinorIslands(2);
		
		PixelList thinnedRing12 = pixelRingList.get(1).getPixelsWithOrthogonalContactsTo(pixelRingList.get(2), island0);
		PixelIsland thinnedIsland12 = PixelIsland.createSeparateIslandWithClonedPixels(thinnedRing12, true);
		SVGG g = new SVGG();
		g.appendChild(thinnedIsland12.getOrCreateSVGG());
		
		Assert.assertEquals("thinned island", 108, thinnedIsland12.size());
		PixelGraph outerThinnedGraph = PixelGraph.createGraph(thinnedIsland12);
		PixelEdgeList thinnedEdgeList = outerThinnedGraph.getEdgeList();
		Assert.assertEquals("edges", 1, thinnedEdgeList.size()); 
		PixelNodeList thinnedNodeList = outerThinnedGraph.getNodeList();
		LOG.trace("thinned nodes "+thinnedNodeList);
		PixelEdge thinnedEdge = thinnedEdgeList.get(0);
		LOG.trace("thinned edge "+thinnedEdge);
		EdgeSegments segmentList = thinnedEdge.getOrCreateSegmentList(1.0);
//		Assert.assertEquals("segments", 6, segmentList.size()); // was 1
		LOG.trace("segments "+segmentList);
		g.appendChild(segmentList.getSVGG());
		
		SVGSVG.wrapAndWriteAsSVG(g, new File(TREC_OUT+"/06a/C00698_thinned12.svg"));
		
		PixelList thinnedRing01 = pixelRingList.get(0).getPixelsWithOrthogonalContactsTo(pixelRingList.get(1), island0);
		PixelIsland thinnedIsland01 = PixelIsland.createSeparateIslandWithClonedPixels(thinnedRing01, true);
		g = new SVGG();
		g.appendChild(thinnedIsland01.getOrCreateSVGG());
		
		Assert.assertEquals("thinned island", 470, thinnedIsland01.size());
		outerThinnedGraph = PixelGraph.createGraph(thinnedIsland01);
		thinnedEdgeList = outerThinnedGraph.getEdgeList();
		Assert.assertEquals("edges", 1, thinnedEdgeList.size());
		thinnedEdge = thinnedEdgeList.get(0);
		segmentList = thinnedEdge.getOrCreateSegmentList(1.0);
//		Assert.assertEquals("segments", 8, segmentList.size()); // was 1
		g.appendChild(segmentList.getSVGG());
		SVGSVG.wrapAndWriteAsSVG(g, new File(TREC_OUT+"/06a/C00698_thinned01.svg"));
		
		
	}
	

}
