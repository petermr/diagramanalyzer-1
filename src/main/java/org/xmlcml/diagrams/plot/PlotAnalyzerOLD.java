package org.xmlcml.diagrams.plot;

import java.io.File;

import org.apache.log4j.Logger;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;
import org.xmlcml.image.ArgIterator;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelIslandList;


@Deprecated // moved to imageanalysis
/** very little useful in this now */
public class PlotAnalyzerOLD extends DiagramAnalyzerOLD {
	private final static Logger LOG = Logger.getLogger(PlotAnalyzerOLD.class);

	public final static String CSV = "-c";
	public final static String CSV1 = "--csv";

	private File csvFile;

	public PlotAnalyzerOLD() {
		setDefaults();
	}

	protected void usage() {
		System.err.println("Usage: plot [args]");
		super.usage();
		System.err.println("       " + CSV + " " + CSV1
				+ "      csvfile (outputs CSV file; optional)");
	}

	public static void main(String[] args) /*throws Exception*/ {
		PlotAnalyzerOLD plotAnalyzer = new PlotAnalyzerOLD();
		plotAnalyzer.parseArgs(args);
	}

	protected boolean parseArgAndAdvance(ArgIterator argIterator) {
		boolean found = true;
		ensureImageProcessor();
		String arg = argIterator.getCurrent();
		if (debug) {
			LOG.debug(arg);
		}
		if (false) {
			
		} else if (arg.equals(ImageProcessor.DEBUG) || arg.equals(ImageProcessor.DEBUG1)) {
			debug = true;
			argIterator.setDebug(true);
			argIterator.next();
		} else if (arg.equals(PlotAnalyzerOLD.CSV) || arg.equals(PlotAnalyzerOLD.CSV1)) {
			String value = argIterator.getSingleValue();
			if (value != null) {
				setCSVFile(new File(value));
			}
		} else {
			found = imageProcessor.parseArgAndAdvance(argIterator);
			if (!found) {
				LOG.debug("skipped unknown token: "+argIterator.getCurrent());
				argIterator.next();
			}
		}
		return found;
	}

	public void debug() {
		if (csvFile != null) {
			System.err.println("CSV output: " + csvFile);
		}
	}

	public boolean runCommands() {
		super.processImageFile();
		this.getOrCreatePixelGraphList();
		int island = ensurePixelProcessor().getSelectedIslandIndex() ;
		if (csvFile != null && island >= 0) {
//			PixelGraph graph = pixelGraphList.get(island);
//			newickString = createNewick(graph);
//			this.writeNewickQuietly();
		}
		return true;
	}
	
	public void getOrCreatePixelGraphList() {
		PixelIslandList pixelIslandList = imageProcessor.getOrCreatePixelIslandList();
//			pixelGraphList = pixelIslandList.analyzeEdgesAndPlot();
		pixelGraphList = pixelIslandList.getOrCreateGraphList();
	}

	public void setCSVFile(File file) {
		if (file == null) {
			throw new RuntimeException("CSV file is null");
		}
		this.csvFile = file;
		this.csvFile.getParentFile().mkdirs();
	}


}
