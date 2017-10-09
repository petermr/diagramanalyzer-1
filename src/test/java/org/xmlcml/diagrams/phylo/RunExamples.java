package org.xmlcml.diagrams.phylo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.image.pixel.PixelGraph;

/** class to manage examples which are too large for tests.
 *
 * This will not be run as a JUnit test
 * 
 * edit in or comment out modules for your own purposes.
 * 
 * @author pm286
 *
 */
public class RunExamples {
	private final static Logger LOG = Logger.getLogger(RunExamples.class);

	/** get newick for tree and add distances.
	 * 
	 * this has a left root
	 * 
	 */
	public static void main(String[] args) throws IOException {
//		runIJSEMFilesSeparately();
		runIJSEMFilesIteratively();
		runIJSEMOpen();
		runRoss5();
		runRoss5light();
		runRoss5darkback();
		runRoss3taxa();
      runRoss14taxa();
		simpleCommands();
	}

	private static void simpleCommands() {
		String[] argsx = {
		"--debug", "--input", "./src/test/resources/org/xmlcml/diagrams/ross/ijs.0.014126-0-000.pbm.png", "--island", "0", "--newick", "target/ijs.0.014126-0-000.nwk" 
		};
	DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
	phyloTree.parseArgsAndRun(argsx);
	}

	// --debug --input ./ijsemopen/ijs.0.014126-0-000.pbm.png --island 0 --newick target/ijs.0.014126-0-000.nwk
	
	private static void runIJSEMFilesIteratively() {
		
		String[] argsx = {
//			"--debug",
			"--input",  "./ijsem/",  // source directory (happens to be in diagramanalyzer but could be anywhere)
			"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
			"--logfile", "target/ijsem/all.log", 
			"--extensions", "png", "jpg",
			"--lengths",
			"--tolerance", "5.0",

			//			"--svg", "target/ijsem/svg/${root}.svg",
			"--skip", "target/ijsem/${root}.nwk",
			"--newick", "target/ijsem/${root}.nwk" // calculate newick and output
			};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(argsx);
	}
	
	/** draw as SVG.
	 * 
	 */
	public static void runIJSEMOpen() {
		String[] args = {
				"--debug",
				"--input",  "./ijsemopen/",  // source image
				"--logfile", "target/ijsemopen/all.log", 
				"--extensions", "png", "jpg",
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ijsemopen/${root}.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--tolerance", "3.0",
				"--skip", "target/ijsemopen/${root}.nwk", // skip existing output
				"--newick", "target/ijsemopen/${root}.nwk" // calculate newick and output
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** draw as SVG.
	 * 
	 */
	public static void runRoss5() {
		String[] args = {
				"--debug",
				"--input",  new File(DiagramsFixtures.ROSS_DIR, "/ross5trees/").toString(),  // source image
				"--logfile", "target/ross5/all.log", 
				"--extensions", "png",
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ross5/${root}.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--tolerance", "3.0",
				"--newick", "target/ross5/${root}.nwk" // calculate newick and output
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** dark background
	 * 
	 * or maybe alpha problem
	 * 
	 */
	public static void runRoss5darkback() {
		String[] args = {
				"--debug",
				"--input",  new File(DiagramsFixtures.ROSS_DIR, "/ross5trees/tree1alt.png").toString(),  // source image
				"--logfile", "target/ross5/all.log", 
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ross5/tree1alt.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--threshold", "5", // background
				"--newick", "target/ross5/tree1alt.nwk" // calculate newick and output
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** dark background
	 * 
	 */
	public static void runRoss5light() {
		String[] args = {
				"--debug",
				"--input",  new File(DiagramsFixtures.ROSS_DIR, "/ross5trees/S16117.png").toString(),  // source image
				"--logfile", "target/ross5/all.log", 
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ross5/S16117.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--threshold", "230", // very light lines
				"--newick", "target/ross5/S16117.nwk" // calculate newick and output
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
	}

	/** dark background
	 * 
	 */
	public static void runRoss3taxa() {
		String[] args = {
				"--debug",
				"--input",  new File(DiagramsFixtures.ROSS_DIR, "/ross5trees/root3taxa.png").toString(),  // source image
				"--logfile", "target/ross5/all.log", 
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ross5/3taxatree.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--newick", "target/ross5/3taxatree.nwk" // calculate newick and output
				};
		PhyloTreePixelAnalyzer phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
		PixelGraph selectedGraph = phyloTree.getOrCreateSelectedGraph();
		Assert.assertNotNull(selectedGraph);
//		Element element = phyloTree.createXML(pixelGraph);
//		phyloTree.createNewick(pixelGraph);
	}

	/** dark background
	 * 
	 */
	public static void runRoss14taxa() {
		String[] args = {
				"--debug",
				"--input",  new File(DiagramsFixtures.ROSS_DIR, "/ross5trees/root14taxatree.png").toString(),  // source image
				"--logfile", "target/ross5/all.log", 
				"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
				"--root", "left",
				"--svgfile", "target/ross5/14taxatree.svg", 
				"--svgpixels", "fill:black",
				"--svgnodes", "fill:blue", 
				"--svgedges", "stroke:green",
				"--lengths",
				"--newick", "target/ross5/14taxatree.nwk" // calculate newick and output
				};
		DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
		phyloTree.parseArgsAndRun(args);
//		Element element = phyloTree.createXML(pixelGraph);
//		phyloTree.createNewick(pixelGraph);
	}



	private static void runIJSEMFilesSeparately() {
		List<File> files = new ArrayList<File>(FileUtils.listFiles(new File("./ijsem/"), new String[]{"png", "jpg"}, false));
		for (File file : files) {
			String root = FilenameUtils.getBaseName(file.toString());
			LOG.debug("root "+root);
			String[] argsx = {
//					"--debug",
					"--input",  file.toString(),  // source image
					"--island", "0", // take the largest island (no magic, if not the first you have to work out which)
					"--logfile", "target/ijsem/"+root+".log", 
//					"--root", "left", // default
					"--lengths",
					"--tolerance", "5.0",
					"--newick", "target/ijsem/"+root+"_test.nwk" // calculate newick and output
					};
			DiagramAnalyzerOLD phyloTree = new PhyloTreePixelAnalyzer();
			phyloTree.parseArgsAndRun(argsx);
		}
	}
	

}
