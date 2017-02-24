package org.xmlcml.diagrams.neuro;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.diagrams.Fixtures;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageUtil;

public class NeuroAnalyzerTest {
	
	public static final Logger LOG = Logger.getLogger(NeuroAnalyzerTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testJNeuro1Png() throws Exception{
		NeuroAnalyzer neuroAnalyzer = new NeuroAnalyzer();
		File trace = new File(Fixtures.DIAGRAMS_DIR, "neuro/1042/raw.png");
		neuroAnalyzer.setTraceImage(trace);
		int nTraces = 2;
		neuroAnalyzer.createSliceList(nTraces);
		neuroAnalyzer.analyzeSlices();
		neuroAnalyzer.analyzePoints();
		FileUtils.write(new File("target/neuro/1042/traces.csv"),neuroAnalyzer.getCSV());
		ImageIOUtil.writeImageQuietly(neuroAnalyzer.getImage(), new File("target/neuro/1042/thinned.png"));
		SVGSVG.wrapAndWriteAsSVG(neuroAnalyzer.getSVG(), new File("target/neuro/1042/lines.svg"));
		Assert.assertEquals("pixels", 7, neuroAnalyzer.getPixelIslandList().size());

	}

	@Test
	public void testJNeuro1621Png() throws Exception{
		NeuroAnalyzer neuroAnalyzer = new NeuroAnalyzer();
		File trace = new File(Fixtures.DIAGRAMS_DIR, "neuro/1621/raw.png");
		neuroAnalyzer.setTraceImage(trace);
		int nTraces = 2;
		neuroAnalyzer.createSliceList(nTraces);
		neuroAnalyzer.analyzeSlices();
		neuroAnalyzer.analyzePoints();
		FileUtils.write(new File("target/neuro/1621/traces.csv"),neuroAnalyzer.getCSV());
		ImageIOUtil.writeImageQuietly(neuroAnalyzer.getImage(), new File("target/neuro/1621/thinned.png"));
		SVGSVG.wrapAndWriteAsSVG(neuroAnalyzer.getSVG(), new File("target/neuro/1621/lines.svg"));
		Assert.assertEquals("pixels", 6, neuroAnalyzer.getPixelIslandList().size());
	}

	@Test
	public void testChen2005Png() throws Exception{
		int nTraces = 1;
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/chen2005/2Aa/raw.png"), new File("target/neuro/chen2005/2Aa/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/chen2005/2Ab/raw.png"), new File("target/neuro/chen2005/2Ab/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/chen2005/2Ac/raw.png"), new File("target/neuro/chen2005/2Ac/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/chen2005/2Ad/raw.png"), new File("target/neuro/chen2005/2Ad/"));
	}

	@Test
	public void test9704Png() throws Exception{
		int nTraces = 1;
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/9704/8Aa/raw.png"), new File("target/neuro/9704/8Aa/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/9704/8Ab/raw.png"), new File("target/neuro/9704/8Ab/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/9704/8Ba/raw.png"), new File("target/neuro/9704/8Ba/"));
		runExtraction(nTraces, new File(Fixtures.DIAGRAMS_DIR, "neuro/9704/8Bb/raw.png"), new File("target/neuro/9704/8Bb/"));
	}


	private void runExtraction(File trace, File outputDir) throws IOException {
		runExtraction(2, trace, outputDir);
	}

	private void runExtraction(int nTraces, File trace, File outputDir) throws IOException {
		NeuroAnalyzer neuroAnalyzer = new NeuroAnalyzer();
		neuroAnalyzer.setTraceImage(trace);
		neuroAnalyzer.createSliceList(nTraces);
		neuroAnalyzer.analyzeSlices();
		neuroAnalyzer.analyzePoints();
		FileUtils.write(new File(outputDir, "traces.csv"),neuroAnalyzer.getCSV());
		ImageIOUtil.writeImageQuietly(neuroAnalyzer.getImage(), new File(outputDir, "thinned.png"));
		SVGSVG.wrapAndWriteAsSVG(neuroAnalyzer.getSVG(), new File(outputDir, "lines.svg"));
	}

	
}
