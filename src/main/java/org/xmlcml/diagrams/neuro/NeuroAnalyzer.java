package org.xmlcml.diagrams.neuro;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.IntArray;
import org.xmlcml.euclid.IntRange;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.svg.SVGElement;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.XSliceList;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.slice.XSlice;

import boofcv.io.image.UtilImageIO;

public class NeuroAnalyzer {

	private static final Logger LOG = Logger.getLogger(NeuroAnalyzer.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private File traceImage;
	private BufferedImage image;
	private ImageProcessor imageProcessor;
	private XSliceList xSliceList;
	private int n;
	private List<IntArray> points;
	private int nTraces;
	private SVGG svg;
	private StringBuilder csvSb;
	
	public NeuroAnalyzer() {
		
	}

	public void setTraceImage(File trace) {
		this.traceImage = trace;
		image = UtilImageIO.loadImage(trace.toString());
		imageProcessor = ImageProcessor
				.createDefaultProcessorAndProcess(image);
	}

	public void createSliceList(int nTraces) {
		this.nTraces = nTraces;
		xSliceList = imageProcessor.getOrCreateXSliceList();
		points = new ArrayList<IntArray>();
	}

	public void analyzeSlices() {
		for (int i = 0; i < xSliceList.size() - 1; i++) {
			XSlice slice0 = xSliceList.get(i);
			XSlice slice1 = xSliceList.get(i+1);
			IntArray curve = new IntArray();
			if (slice0.size() > nTraces) {
				LOG.debug(i+"; "+slice0+"; "+slice0.get(0)+"; "+slice0.get(slice0.size() - 1));
			}
			for (int j = 0; j < nTraces; j++) {
//				IntRange range0 = (j == 0) ? slice0.get(0) : slice0.get(slice0.size() - 1);
//				IntRange range1 = (j == 0) ? slice1.get(0) : slice1.get(slice1.size() - 1);
//				Integer overlap = getOverlap(range0 , range1);
				Integer	overlap = (j == 0) ? slice0.get(0).getMin() : slice0.get(slice0.size() - 1).getMax();
//				if (slice0.size() > nTraces) {
//					LOG.debug("    "+range0+"; "+range1+"; "+overlap);
//				}
				curve.addElement(overlap);
			}
			points.add(curve);
		}
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

	public void analyzePoints() {
		svg = new SVGG();
		csvSb = new StringBuilder();
		csvSb.append("time,current,clamp,\n");
		analyzePoints1();
	}

	private void analyzePoints1() {
		for (int x = 1; x < points.size(); x++) {
			IntArray lastDots = points.get(x-1);
			IntArray thisDots = points.get(x);
			if (lastDots == null || thisDots == null) {
				LOG.debug("skipped");
				continue;
			}
			csvSb.append(x+",");
			for (int j = 0; j < nTraces; j++) {
				int y0 = lastDots.elementAt(j);
				int y1 = thisDots.elementAt(j);
				SVGLine line = new SVGLine(new Real2(x-1, y0), new Real2(x, y1));
				csvSb.append(y1+",");
				svg.appendChild(line);
			}
			csvSb.append("\n");
		}
	}

	public String getCSV() {
		return csvSb == null ? null : csvSb.toString();
	}

	public SVGElement getSVG() {
		return svg;
	}

	public PixelIslandList getPixelIslandList() {
		return imageProcessor == null ? null : imageProcessor.getOrCreatePixelIslandList();
	}

	public BufferedImage getImage() {
		return imageProcessor == null ? null : imageProcessor.getImage();
	}
}
