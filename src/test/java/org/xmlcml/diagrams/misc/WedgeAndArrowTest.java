package org.xmlcml.diagrams.misc;

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
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGPolygon;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.ImageUtil;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;
import org.xmlcml.image.pixel.PixelList;
import org.xmlcml.image.pixel.PixelOutliner;
import org.xmlcml.image.pixel.PixelPlotter;
import org.xmlcml.image.pixel.PixelRingList;
import org.xmlcml.image.processing.ZhangSuenThinning;

public class WedgeAndArrowTest {

	private final static Logger LOG = Logger.getLogger(RSCTest.class);
	
	public final static String PLOS = "plosone";
	public final static String FILENAME111303_004 = "journal.pone.0111303.g004.png";
	public final static File PLOS_DIR = new File(PLOS);
	public final static String[] FILENAMES = {
		FILENAME111303_004,
	};
	ImageProcessor DEFAULT_IMAGE_PROCESSOR;
	
	@Before
	public void setup() {
		DEFAULT_IMAGE_PROCESSOR = new ImageProcessor();
	}

	/** analyse complete spectrum
	 * 
	 * @throws IOException
	 */
	@Test
	public void testPLOS111303_004() throws IOException {
		File rawfile = new File(PLOS_DIR, FILENAME111303_004);
		DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		File rawFile = new File("target/"+ PLOS+"/raw.png");
		ImageIOUtil.writeImageQuietly(DEFAULT_IMAGE_PROCESSOR.getImage(), rawFile);
		LOG.trace("wrote: "+rawFile);
		PixelIslandList islandList = DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		
		DEFAULT_IMAGE_PROCESSOR.setThinning(new ZhangSuenThinning());
		DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		ImageIOUtil.writeImageQuietly(DEFAULT_IMAGE_PROCESSOR.getImage(), new File("target/"+ PLOS+"/thinned.png"));
		islandList.sortBySizeDescending();
		PixelPlotter pixelPlotter = new PixelPlotter();
		pixelPlotter.setOutputDirectory(new File("target/" + WedgeAndArrowTest.PLOS + "/"));
		LOG.trace("islands "+islandList.size());
		for (int i = 0; i < islandList.size(); i++) {
			String serial = String.valueOf(i);
			PixelIsland island = islandList.get(i);
			pixelPlotter.plotIsland(island, serial);
		}
	}

	
	@Test
	@Ignore // consumes huge CPU
	public void testMultiwedge() throws IOException {
		File rawfile = new File(Fixtures.MOLECULE_DIR, "multiwedge.png");
		DEFAULT_IMAGE_PROCESSOR.setThinning(null);
		DEFAULT_IMAGE_PROCESSOR.readAndProcessFile(rawfile);
		PixelIslandList islandList = DEFAULT_IMAGE_PROCESSOR.getOrCreatePixelIslandList();
		ImageIOUtil.writeImageQuietly(DEFAULT_IMAGE_PROCESSOR.getImage(), new File("target/"+ PLOS+"/multiwedge.png"));
		islandList.sortBySizeDescending();
		PixelPlotter pixelPlotter = new PixelPlotter();
		pixelPlotter.setOutputDirectory(new File("target/" + WedgeAndArrowTest.PLOS + "/"));
		PixelIsland island = islandList.get(0);
		pixelPlotter.plotIsland(island, "xyz");
		PixelRingList ringList = island.getOrCreateInternalPixelRings();
		Assert.assertEquals("Rings", 7, ringList.size());
		// shows the arrowhead is at 3
		int[] sizes = new int[]{6931, 2667, 1421, 1006, 600, 227, 68};
		for (int i = 0; i < ringList.size(); i++) {
			PixelList ring = ringList.get(i);
			PixelIsland island1 = PixelIsland.createSeparateIslandWithClonedPixels(ring, true);
			Assert.assertEquals("ring"+i, sizes[i], island1.size());
			SVGSVG.wrapAndWriteAsSVG(ring.getOrCreateSVG(), new File("target/"+PLOS+"/multiwedge00_"+i+".svg"));
		}
		SVGG g0 = ringList.get(2).plotPixels("blue");
		SVGSVG.wrapAndWriteAsSVG(g0, new File("target/"+PLOS+"/multiwedge00.svg"));
		
		// ring2 is the wedges
		PixelOutliner outliner = new PixelOutliner(ringList.get(2));
		outliner.setMaxIter(400);
		outliner.setMinPolySize(50);
		outliner.createOutline();
		List<SVGPolygon> polygonList = outliner.getPolygonList();
		SVGG g = new SVGG();
		for (SVGPolygon polygon : polygonList) {
			g.appendChild(polygon);
		}
		File f = new File("target/"+PLOS+"/wedgePolygonList.svg");
		SVGSVG.wrapAndWriteAsSVG(g, f);
		LOG.debug("wrote "+f);

		
	}

}
