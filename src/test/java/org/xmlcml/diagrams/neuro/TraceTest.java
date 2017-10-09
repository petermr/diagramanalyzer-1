package org.xmlcml.diagrams.neuro;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.diagrams.DiagramsFixtures;
import org.xmlcml.euclid.IntArray;
import org.xmlcml.euclid.IntRange;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.image.ImageIOUtil;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.XSliceList;
import org.xmlcml.image.slice.XSlice;

import boofcv.io.image.UtilImageIO;

public class TraceTest {
	
	public static final Logger LOG = Logger.getLogger(TraceTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	@Test
	public void testJNeuro1Png() throws Exception{
		File trace = new File(DiagramsFixtures.DIAGRAMS_DIR, "neuro/jphysiol_80_3_1042.png");
		Assert.assertTrue("trace file exists", trace.exists());
		BufferedImage image = UtilImageIO.loadImage(trace.toString());
		Assert.assertNotNull("image not null", image);
		ImageProcessor imageProcessor = ImageProcessor
				.createDefaultProcessorAndProcess(image);
		new File("target/neuro").mkdirs();
		ImageIOUtil.writeImageQuietly(image, new File("target/neuro/raw.png"));
		BufferedImage defaultBinaryImage = imageProcessor.getImage();
		ImageIOUtil.writeImageQuietly(defaultBinaryImage, new File("target/neuro/binary.png"));
		XSliceList xSliceList = imageProcessor.getOrCreateXSliceList();
		LOG.trace(xSliceList.size());
		int N = 2;
		List<IntArray> points = new ArrayList<IntArray>();
		for (int i = 0; i < xSliceList.size() - 1; i++) {
			XSlice slice0 = xSliceList.get(i);
			XSlice slice1 = xSliceList.get(i+1);
			IntArray curve = new IntArray();
			if (slice0.size() > N) {
				LOG.trace(i+"; "+slice0+"; "+slice0.get(0)+"; "+slice0.get(slice0.size() - 1));
			}
			for (int j = 0; j < N; j++) {
//				IntRange range0 = (j == 0) ? slice0.get(0) : slice0.get(slice0.size() - 1);
//				IntRange range1 = (j == 0) ? slice1.get(0) : slice1.get(slice1.size() - 1);
//				Integer overlap = getOverlap(range0 , range1);
				Integer	overlap = (j == 0) ? slice0.get(0).getMin() : slice0.get(slice0.size() - 1).getMax();
//				if (slice0.size() > N) {
//					LOG.debug("    "+range0+"; "+range1+"; "+overlap);
//				}
				curve.addElement(overlap);
			}
			points.add(curve);
		}
		SVGG g = new SVGG();
		StringBuilder sb = new StringBuilder();
		sb.append("time,current,clamp,\n");
		for (int x = 1; x < points.size(); x++) {
			IntArray lastDots = points.get(x-1);
			IntArray thisDots = points.get(x);
			if (lastDots == null || thisDots == null) {
				LOG.debug("skipped");
				continue;
			}
			sb.append(x+",");
			for (int j = 0; j < N; j++) {
				int y0 = lastDots.elementAt(j);
				int y1 = thisDots.elementAt(j);
				SVGLine line = new SVGLine(new Real2(x-1, y0), new Real2(x, y1));
				sb.append(y1+",");
				g.appendChild(line);
			}
			sb.append("\n");
		}
		FileUtils.write(new File("target/neuro/data.csv"),sb.toString());
		SVGSVG.wrapAndWriteAsSVG(g, new File("target/neuro/lines.svg"));
		Assert.assertEquals("pixels", 7, imageProcessor
				.getOrCreatePixelIslandList().size());
		int pixels = imageProcessor.getOrCreatePixelIslandList().getPixelList()
				.size();
		LOG.debug(pixels);

	}
	
	private Integer getOverlap(IntRange range0, IntRange range1) {
		IntRange overlapRange = range0.intersectionWith(range1);
		Integer overlap = overlapRange != null ? overlapRange.getMidPoint() : null;
		if (overlap == null) {
			overlap = getMidVoid(range0, range1);
//			if (overlap == null) {
//				overlap = getMidVoid(range1, range0);
//			}
		}
		return overlap;
		
	}

	private Integer getMidVoid(IntRange range0, IntRange range1) {
		int range0Min = range0.getMin();
		int range1Max = range1.getMax();
		return (range0Min > range1Max) ? (range0Min + range1Max ) / 2 : null;
	}
}
