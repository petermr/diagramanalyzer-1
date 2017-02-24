package org.xmlcml.diagrams;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.diagrams.phylo.PhyloTreePixelAnalyzer;
import org.xmlcml.euclid.Int2;
import org.xmlcml.euclid.Int2Range;
import org.xmlcml.euclid.IntRange;
import org.xmlcml.euclid.Real2;
import org.xmlcml.graphics.svg.SVGSVG;
import org.xmlcml.image.ImageProcessor;
import org.xmlcml.image.pixel.PixelComparator.ComparatorType;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelIsland;
import org.xmlcml.image.pixel.PixelIslandList;

public class XMLTreeTest {

	private final static Logger LOG = Logger.getLogger(XMLTreeTest.class);

	public ImageProcessor DEFAULT_PROCESSOR;

	@Before
	public void setup() {
		DEFAULT_PROCESSOR = ImageProcessor.createDefaultProcessor();
	}
	
	@Test
	public void testBoxMidPoints() {
		IntRange xRange = new IntRange(3,13);
		IntRange yRange = new IntRange(7,17);
		Int2Range box = new Int2Range(xRange, yRange);
		TreeBuilder treeBuilder = new TreeBuilder(null, null);
		treeBuilder.setComparatorType(ComparatorType.LEFT);
		Int2 leftMidPoint = treeBuilder.getExtremeMidPoint(box);
		Assert.assertEquals("left", new Int2(3, 12), leftMidPoint);
		treeBuilder.setComparatorType(ComparatorType.RIGHT);
		Int2 rightMidPoint = treeBuilder.getExtremeMidPoint(box);
		Assert.assertEquals("right", new Int2(13, 12), rightMidPoint);
		treeBuilder.setComparatorType(ComparatorType.TOP);
		Int2 topMidPoint = treeBuilder.getExtremeMidPoint(box);
		Assert.assertEquals("top", new Int2(8, 7), topMidPoint);
		treeBuilder.setComparatorType(ComparatorType.BOTTOM);
		Int2 bottomMidPoint = treeBuilder.getExtremeMidPoint(box);
		Assert.assertEquals("bottom", new Int2(8, 17), bottomMidPoint);
	}



//	/** simple bifurcation.
//	 * 
//	 */
//	@Test
//	public void testNewick1() {
//		TreeBuilder treeBuilder = new TreeBuilder(null, null);
//		DiagramTree tree = treeBuilder.createTreeWithUnbranchedRoot();
//		XMLNode root = tree.getRootNode();
//		XMLNode n1 = tree.createXMLNode("l1");
//		root.appendChild(n1);
//		XMLNode n2 = tree.createXMLNode("l2");
//		root.appendChild(n2);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "(l1,l2);", newick);
//	}
//
//	/** trifurcated root.
//	 * 
//	 * third child anon
//	 */
//	@Test
//	public void testNewick4() {
//		TreeBuilder treeBuilder = new TreeBuilder(null, null);
//		DiagramTree tree = treeBuilder.createTreeWithUnbranchedRoot();
//		XMLNode root = tree.getRootNode();
//		XMLNode n1 = tree.createXMLNode("l1");
//		root.appendChild(n1);
//		XMLNode n2 = tree.createXMLNode("l2");
//		root.appendChild(n2);
//		XMLNode anon = tree.createXMLNode();
//		root.appendChild(anon);
//		XMLNode n3 = tree.createXMLNode("l3");
//		anon.appendChild(n3);
//		XMLNode n4 = tree.createXMLNode("l4");
//		anon.appendChild(n4);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "(l1,l2,(l3,l4));", newick);
//	}
//	
//	/** repeated bifurcation.
//	 * 
//	 * 
//	 */
//	@Test
//	public void testNewick22() {
//		TreeBuilder treeBuilder = new TreeBuilder(null, null);
//		DiagramTree tree = treeBuilder.createTreeWithUnbranchedRoot();
//		XMLNode root = tree.getRootNode();
//		XMLNode n1 = tree.createXMLNode("l1");
//		root.appendChild(n1);
//		XMLNode n2 = tree.createXMLNode("l2");
//		root.appendChild(n2);
//		XMLNode anon = tree.createXMLNode();
//		n1.appendChild(anon);
//		XMLNode n3 = tree.createXMLNode("l11");
//		anon.appendChild(n3);
//		XMLNode n4 = tree.createXMLNode("l12");
//		anon.appendChild(n4);
//		anon = tree.createXMLNode();
//		n2.appendChild(anon);
//		XMLNode n5 = tree.createXMLNode("l21");
//		anon.appendChild(n5);
//		XMLNode n6 = tree.createXMLNode("l22");
//		anon.appendChild(n6);
//
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "((l11,l12),(l21,l22));", newick);
//	}
//	
//	/** single bifurcation.
//	 * 
//	 * 
//	 */
//	@Test
//	public void testNewick2Dist() {
//		TreeBuilder treeBuilder = new TreeBuilder(null, null);
//		DiagramTree tree = treeBuilder.createTreeWithUnbranchedRoot();
//		XMLNode root = tree.getRootNode();
//		root.setXY2(new Real2(0., 200.));
//		XMLNode n1 = tree.createXMLNode("l1");
//		n1.setXY2(new Real2(100., 100.));
//		root.appendChild(n1);
//		XMLNode n2 = tree.createXMLNode("l2");
//		n2.setXY2(new Real2(150., 300.));
//		root.appendChild(n2);
//
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "(l1:0.67,l2:1.0);", newick);
//	}
//	
//	/** repeated bifurcation.
//	 * 
//	 * 
//	 */
//	@Test
//	public void testNewick22Dist() {
//		TreeBuilder treeBuilder = new TreeBuilder(null, null);
//		DiagramTree tree = treeBuilder.createTreeWithUnbranchedRoot();
//		XMLNode root = tree.getRootNode();
//		root.setXY2(new Real2(0., 200.));
//		XMLNode n1 = tree.createAndAddXMLNode(root, "l1", new Real2(100., 100.));
//		XMLNode n11 = tree.createAndAddXMLNode(n1, "l11", new Real2(150., 50.));
//		XMLNode n12 = tree.createAndAddXMLNode(n1, "l12", new Real2(400., 150.));
//		XMLNode n2 = tree.createAndAddXMLNode(root, "l2", new Real2(150., 300.));
//		XMLNode n21 = tree.createAndAddXMLNode(n2, "l21", new Real2(350., 250.));
//		XMLNode n22 = tree.createAndAddXMLNode(n2, "l22", new Real2(500., 350.));
//
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "((l11:0.1,l12:0.6):0.2,(l21:0.4,l22:0.7):0.3);", newick);
//	}
//	
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void test2TaxaTree() {
//		
//		BufferedImage image = DEFAULT_PROCESSOR.processImageFile(new File(Fixtures.ROSS_DIR, "ross5trees/root2taxa.png"));
//		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
//		PixelIsland treeIsland = pixelIslandList.get(0);
//		SVGSVG.wrapAndWriteAsSVG(treeIsland.getOrCreateSVGG(), new File("target/ross5trees/root2taxa.svg"));
//		PixelGraph graph = treeIsland.getOrCreateGraph();
//		graph.numberAllNodes();
//		DiagramTree tree = new TreeBuilder(graph, ComparatorType.LEFT).createFromGraph();
//		Assert.assertNotNull("tree", tree);
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		LOG.debug("xml "+tree.toString());
//		Assert.assertEquals("newick", 
//				"(n1:0.82,n2:0.51):0.18;", newick.toString()); 
//	}
//	
//	/**
//	 *node label="n0" xy2="(200.0,131.0)">
//	 *  <node label="n3" xy2="(226.0,131.0)">
//	 *    <node label="n1" xy2="(348.0,96.0)" />
//	 *    <node label="n2" xy2="(302.0,165.0)" />
//	 *  </node>
//	 *</node>
//
//	 */
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void test3TaxaTree() {
//		BufferedImage image = DEFAULT_PROCESSOR.processImageFile(new File(Fixtures.ROSS_DIR, "ross5trees/root3taxa.png"));
//		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
//		PixelIsland treeIsland = pixelIslandList.get(0);
//		PixelGraph graph = treeIsland.getOrCreateGraph();
//		graph.numberAllNodes();
//		DiagramTree tree = new TreeBuilder(graph, ComparatorType.LEFT).createFromGraph();
//		Assert.assertNotNull("tree", tree);
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", 
//				"(n1:0.7,(n3:0.35,n2:0.35):0.34):0.3;", newick.toString());
//	}
//	
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void test14TaxaTree() {
//		BufferedImage image = DEFAULT_PROCESSOR.processImageFile(new File(Fixtures.ROSS_DIR, "ross5trees/root14taxa.png"));
//		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
//		PixelIsland treeIsland = pixelIslandList.get(0);
//		PixelGraph graph = treeIsland.getOrCreateGraph();
//		graph.numberAllNodes();
//		DiagramTree tree = new TreeBuilder(graph, ComparatorType.LEFT).createFromGraph();
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", 
//			"((((n5:0.1,n6:0.1):0.6,((n8:0.1,n7:0.1):0.5,"
//			+ "(n9:0.5,((n11:0.3,(n12:0.2,(n13:0.1,n14:0.1):0.1):0.1):0.1,"
//			+ "n10:0.4):0.1):0.1):0.1):0.1,"
//			+ "((n3:0.1,n4:0.1):0.1,n2:0.2):0.6):0.1,n1:0.9):0.1;", 
//			newick.toString());
//	}
//	
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void testIJSEM18333() {
//		BufferedImage image = DEFAULT_PROCESSOR.processImageFile(new File(Fixtures.ROSS_DIR, "ijs.0.018333-0-002.pbm.png"));
//		PixelIslandList pixelIslandList = ImageProcessor.createDefaultProcessorAndProcess(image).getOrCreatePixelIslandList();
//		PixelIsland treeIsland = pixelIslandList.get(0);
//		PixelGraph graph = treeIsland.getOrCreateGraph();
//		graph.numberAllNodes();
//		DiagramTree tree = new TreeBuilder(graph, ComparatorType.LEFT).createFromGraph();
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", "(n1:0.7,(((((n5:0.02,n6:0.02):0.17,n9:0.25):0.27,"
//			+ "(n7:0.16,(n10:0.03,n8:0.01):0.13):0.34):0.06,(((n12:0.38,n13:0.48):0.04,"
//			+ "(n15:0.0,n14:0.0):0.63):0.04,n11:0.47):0.17):0.01,((n3:0.09,n2:0.04):0.53,n4:0.68):0.07):0.09):0.06;", 
//			newick.toString());
//	}
//	
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void testRootedTrees() {
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.014811-0-001.pbm.png"), 
//				"(307_430:0.52,((((363_187:0.4,376_153:0.43):0.09,(323_118:0.25,(331_84:0.25,(326_15:0.22,384_50:0.33):0.02):0.02):0.17):0.06,(535_256:0.68,349_221:0.33):0.2):0.05,((206_325:0.21,220_290:0.24):0.04,(244_396:0.14,252_359:0.16):0.18):0.07):0.01):0.05;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.018333-0-002.pbm.png"), 
//				"(468_13:0.7,(((((420_541:0.02,421_503:0.02):0.17,455_466:0.25):0.27,(443_428:0.16,(444_390:0.03,435_352:0.01):0.13):0.34):0.06,(((486_202:0.38,544_164:0.48):0.04,(617_240:0.0,615_277:0.0):0.63):0.04,495_315:0.47):0.17):0.01,((516_89:0.09,487_51:0.04):0.53,556_126:0.68):0.07):0.09):0.06;");
//	}
//	
//	@Test 
//	@Ignore // FIXME nodes not numbered properly
//	public void testImplicitlyRootedTrees() {
//		extractAndTestGraph(new File(Fixtures.ROSS_DIR, "implicitRoot.png"), 
//				"((385_1199:0.12,385_993:0.12):0.83,385_1309:0.95):0.05;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.014126-0-000.pbm.png"), 
//				"(((585_569:0.58,(562_535:0.5,((((565_188:0.29,(464_223:0.12,460_257:0.11):0.02):0.03,((524_154:0.15,458_119:0.05):0.05,((611_50:0.19,548_15:0.09):0.06,510_84:0.09):0.09):0.06):0.01,((506_362:0.13,551_327:0.19):0.02,488_292:0.12):0.09):0.04,((525_431:0.24,(515_500:0.18,638_466:0.37):0.05):0.04,485_397:0.21):0.03):0.14):0.04):0.06,386_605:0.33):0.25,639_659:0.97):0.03;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.014811-0-002.pbm.png"), 
//				"((564_218:0.27,(648_184:0.34,(672_149:0.32,(616_115:0.18,((651_12:0.07,788_47:0.38):0.12,722_81:0.35):0.07):0.02):0.07):0.12):0.23,(561_252:0.46,516_287:0.36):0.04):0.0;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.015511-0-001.pbm.png"), 
//				"((((447_517:0.08,419_559:0.03):0.38,((398_396:0.21,(412_477:0.1,404_435:0.08):0.14):0.13,(403_354:0.3,((400_190:0.05,(457_28:0.15,((426_151:0.01,428_109:0.01):0.01,440_69:0.05:0.0:0.0):0.07):0.01):0.08,(438_235:0.0,440_314:0.0):0.21):0.17):0.05):0.02):0.19,(398_640:0.05,401_599:0.06):0.5):0.28:0.0:0.0:0.0:0.0:0.0:0.0,132_682:0.28:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0:0.0):0.04;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.017715-0-001.pbm.png"), 
//				"((455_633:0.76,(((482_284:0.23,517_243:0.33):0.55,((((418_13:0.0,420_50:0.01):0.27,430_89:0.3):0.08,454_128:0.46):0.11,(443_167:0.48,403_206:0.35):0.06):0.11):0.04,((415_400:0.48,(391_361:0.38,362_322:0.29):0.02):0.09,(413_439:0.55,((410_556:0.32,443_594:0.42):0.1,(478_516:0.59,401_478:0.35):0.05):0.12):0.01):0.04):0.03):0.05,(423_711:0.63,478_673:0.8):0.08):0.0;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.018846-0-000.pbm.png"), 
//				"((375_799:0.88,254_761:0.58):0.06,(((274_724:0.02,275_686:0.03):0.18,297_649:0.26):0.2,(280_612:0.27,(325_575:0.36,((342_388:0.33,(311_426:0.21,((376_537:0.31,324_500:0.18):0.05,339_463:0.27):0.01):0.04):0.02,((((340_277:0.09,342_314:0.1):0.05,347_239:0.16):0.06,((329_202:0.02,346_165:0.06):0.12,((342_90:0.12,(339_53:0.07,337_16:0.07):0.04):0.04,341_128:0.16):0.01):0.04):0.08,335_351:0.27):0.05):0.06):0.03):0.16):0.28):0.05;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019125-0-000.pbm.png"), 
//				"(771_1215:0.97,(613_1168:0.21,((((609_938:0.0,609_984:0.0):0.14,581_891:0.1):0.02,(((554_383:0.04,(((576_198:0.02,(591_152:0.01,(590_106:0.0,590_61:0.0):0.01):0.02):0.01,567_244:0.02):0.01,(772_337:0.26,610_291:0.06):0.02):0.03):0.01,(627_429:0.13,(((574_568:0.04,565_521:0.03):0.01,(525_521:0.0,530_525:0.0):0.01):0.0,549_475:0.02):0.01):0.02,553_614:0.05):0.01,((547_660:0.03,564_706:0.05):0.01,((569_798:0.01,601_834:0.05):0.02,581_752:0.05):0.04):0.01):0.03):0.03,(559_1020:0.09,(618_1122:0.0,618_1077:0.0):0.17):0.04):0.01):0.56):0.03;");
//		 //large (and has a pinhole break!)
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019299-0-000.pbm.png"), 
//				"((828_1669:0.14,853_1700:0.16):0.56,((((802_1455:0.24,797_1485:0.24):0.06,802_1516:0.3):0.15,((699_1424:0.25,((802_1394:0.24,712_1362:0.16):0.06,((699_1270:0.02,699_1301:0.02):0.03,711_1332:0.06):0.16):0.03):0.04,(((1010_352:0.49,(719_294:0.0,((((1023_106:0.08,1047_138:0.1):0.06,1158_169:0.25):0.05,(1016_76:0.04,(1035_15:0.02,1041_46:0.02):0.04):0.14):0.06,(978_229:0.19,963_199:0.18):0.02):0.01):0.24):0.01,(((829_596:0.08,815_566:0.07):0.13,(809_504:0.13,797_536:0.12):0.07):0.08,((868_474:0.14,868_443:0.14):0.16,(777_413:0.14,797_382:0.16):0.09):0.02):0.05):0.03,((((835_994:0.3,(((770_842:0.09,771_872:0.09):0.02,751_903:0.09):0.12,(763_934:0.2,834_965:0.26):0.02):0.02):0.02,809_1025:0.3):0.01,((737_812:0.21,(711_781:0.09,719_750:0.09):0.1):0.03,((899_689:0.29,(783_659:0.04,771_627:0.03):0.15):0.05,750_720:0.21):0.04):0.01):0.02,((((860_1210:0.07,893_1179:0.1):0.17,880_1240:0.25):0.12,(737_1118:0.22,906_1148:0.36):0.03):0.01,(802_1087:0.06,790_1057:0.05):0.26):0.01):0.03):0.02):0.07):0.08,(821_1639:0.43,(821_1608:0.41,(783_1547:0.21,809_1577:0.23):0.17):0.02):0.1):0.16):0.02;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019299-0-001.pbm.png"), 
//				"((((458_1032:0.15,407_999:0.09):0.02,379_1065:0.08):0.18,((415_931:0.23,((613_898:0.25,((540_828:0.0,540_796:0.0):0.06,504_863:0.01):0.11):0.05,(((598_627:0.0,599_594:0.0):0.1,(605_558:0.07,(625_524:0.03,599_490:0.0):0.01:0.05):0.04):0.1,(824_661:0.0,824_761:0.0):0.47):0.09):0.16):0.01,446_964:0.28):0.05):0.19,598_1099:0.71):0.02;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019513-0-000.pbm.png"), 
//				"(((((571_251:0.28,(((728_49:0.19,759_15:0.23):0.19,((571_150:0.14,636_116:0.23):0.01,680_83:0.3):0.02):0.09,(615_217:0.25,617_184:0.25):0.08):0.01):0.04,((580_385:0.22,(671_419:0.14,618_454:0.07):0.2):0.11,((646_313:0.22,674_280:0.25):0.11,575_350:0.23):0.09):0.0):0.05,((607_588:0.32,((585_688:0.17,587_723:0.17):0.03,528_655:0.13):0.08):0.08,(570_554:0.22,(560_520:0.09,559_487:0.09):0.11):0.13):0.03):0.02,(698_857:0.49,662_756:0.44):0.08):0.13,406_891:0.31):0.23;");
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019638-0-001.pbm.png"), 
//				"((717_2335:0.73,((((795_2297:0.34,764_2263:0.31):0.27,744_2221:0.56):0.14,875_2184:0.83):0.04,(728_2147:0.66,936_2110:0.88):0.05):0.02):0.02,(((((875_419:0.6,806_380:0.53):0.17,(((780_192:0.47,(776_155:0.25,(769_120:0.18,(801_77:0.15,758_43:0.1):0.07):0.06):0.21):0.09,798_229:0.57):0.02,((745_304:0.08,747_342:0.08):0.36,803_270:0.5):0.1):0.11):0.01,((((826_493:0.13,861_456:0.17):0.24,833_531:0.38):0.24,889_571:0.68):0.03,(((858_720:0.19,827_681:0.16):0.15,838_645:0.32):0.09,898_607:0.47):0.24):0.1):0.1,726_758:0.73):0.01,(((((913_946:0.59,(813_982:0.02,833_1018:0.04):0.47):0.11,(849_1093:0.37,799_1058:0.31):0.27):0.09,939_909:0.82):0.11,(698_871:0.17,(741_795:0.12,703_830:0.08):0.1):0.5):0.01,(((((783_1508:0.46,875_1470:0.56):0.16,((775_1581:0.58,850_1547:0.65):0.01,(880_1621:0.67,908_1659:0.7):0.02):0.03):0.05,(877_1394:0.72,917_1434:0.77):0.04):0.05,(((785_1283:0.45,(767_1319:0.25,807_1358:0.29):0.18):0.09,827_1246:0.59):0.12,(923_1209:0.76,(757_1132:0.03,774_1167:0.05):0.55):0.05):0.06):0.04,(730_2070:0.68,(((693_1697:0.48,784_1735:0.58):0.02,765_1772:0.58):0.11,(((686_1960:0.24,(665_1998:0.07,678_2033:0.09):0.14):0.08,(633_1920:0.05,(649_1849:0.04,644_1883:0.03):0.03):0.21):0.13,714_1810:0.48):0.17):0.02):0.02):0.01):0.03):0.01):0.02;");
//	}
	
	@Test 
	public void testProblemTrees() {
//		// has filled circular nodes
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.017582-0-000.pbm.png"), 
//				"(818_2440:0.98,(685_2399:0.77,(455_2359:0.45,(((((556_1631:0.37,510_1672:0.31):0.01,((((619_1307:0.17,619_1267:0.17):0.13,494_1348:0.15):0.04,((((((598_620:0.09,574_579:0.06):0.12,618_660:0.24):0.06,(((515_418:0.09,553_458:0.13):0.03,588_499:0.2):0.02,549_539:0.18):0.03):0.0,(592_378:0.24,(520_336:0.13,((690_135:0.28,(581_94:0.11,(583_54:0.09,636_14:0.15):0.03):0.03):0.03,(((613_175:0.06,688_215:0.15):0.03,592_256:0.07):0.08,593_296:0.14):0.05):0.02):0.03):0.03):0.0,((((501_742:0.06,493_701:0.05):0.04,(543_822:0.13,526_782:0.11):0.02):0.02,(517_903:0.1,542_863:0.13):0.04):0.03,546_943:0.2):0.01):0.02,(((591_1186:0.12,583_1146:0.11):0.15,656_1226:0.34):0.01,((517_1024:0.13,516_984:0.13):0.03,(501_1105:0.11,501_1065:0.11):0.03):0.03):0.01):0.02):0.02,(615_1591:0.33,(((575_1510:0.16,552_1470:0.13):0.04,(634_1430:0.23,616_1389:0.21):0.04):0.02,634_1550:0.29):0.06):0.03):0.09):0.02,(608_1753:0.14,577_1711:0.1):0.32):0.03,595_1793:0.47):0.05,((((605_1834:0.24,499_1874:0.12):0.15:0.02,(555_1995:0.27,678_2035:0.42):0.08):0.06,(490_2157:0.17,(503_2076:0.14,487_2117:0.12):0.05):0.17):0.03,((654_2278:0.4,694_2318:0.45):0.14,(555_2238:0.17,663_2197:0.3):0.25):0.03):0.03):0.1):0.04):0.05):0.0:0.02;");
//		// small breaks
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.018689-0-000.pbm.png"), 
//				"((385_1199:0.12,385_993:0.12):0.83,385_1309:0.95):0.05;");
//		// has filled circular nodes
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.018788-0-000.pbm.png"), 
//				"(((((546_650:0.31,((663_535:0.29,554_477:0.18):0.06,477_593:0.16):0.08):0.07,(510_708:0.08,(498_766:0.05,(501_825:0.04,(515_940:0.04,502_883:0.03):0.01):0.02):0.01):0.27):0.02,(500_419:0.28,545_360:0.33):0.08):0.06,(423_302:0.25,((948_245:0.7,502_187:0.23):0.05,(803_71:0.43,459_129:0.06):0.17):0.06):0.08):0.05,519_12:0.49):0.06;");
//		// has filled triangular nodes
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019018-0-002.pbm.png"), 
//				"(635_13:0.97,(((((353_300:0.0,353_254:0.0):0.19,((362_637:0.0,(386_685:0.01,433_735:0.08):0.03):0.03,(427_349:0.09,(((414_541:0.03,412_589:0.03):0.02,(406_445:0.02,395_493:0.01):0.01):0.01,381_397:0.01):0.01):0.04):0.18):0.14,((479_916:0.37,455_996:0.07:0.26):0.06,(382_884:0.18,((430_794:0.0,430_780:0.0):0.18,421_837:0.17):0.07):0.1):0.09):0.08,193_205:0.17):0.07,(213_61:0.22,(252_109:0.06,281_157:0.1):0.22):0.05):0.05):0.03;");
//		// 4 trees, takes largest
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019018-0-003.pbm.png"), 
//				"((((((1164_337:0.0,1161_497:0.0):0.0,1161_123:0.0):0.02,(1188_551:0.02,1179_605:0.0):0.05):0.05,(1153_711:0.0,1153_660:0.0):0.05):0.13,(1233_1195:0.17,((1364_1141:0.19,1470_1087:0.41):0.12,((1332_766:0.0,1332_926:0.0):0.19,(1321_1034:0.0,1330_980:0.02):0.17):0.06):0.12):0.17):0.15,(((1467_1409:0.12,1462_1356:0.11):0.1,1430_1302:0.15):0.66,1177_1248:0.31):0.06):0.04;");
//		// 4 trees
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019018-0-004.pbm.png"), 
//				"(((1257_1157:0.77,(1295_1112:0.0,1296_1068:0.0):0.86):0.08,((1238_1023:0.0,1246_889:0.02):0.65,(((1248_755:0.02,(1257_621:0.02,1249_577:0.0):0.02):0.02,(1266_800:0.04,1257_844:0.02):0.05):0.49,((1249_487:0.12,((1276_297:0.02,(((1295_265:0.0,1295_308:0.0):0.02,1294_398:0.02:0.0:0.02:0.01:0.0):0.0,1287_41:0.0):0.04):0.0:0.0:0.0,1270_442:0.0):0.17):0.25,1261_532:0.4):0.17):0.14):0.15):0.05;");
//		// small breaks
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019596-0-000.pbm.png"), 
//				"(490_563:0.39,((((506_403:0.04,528_443:0.07):0.1,477_483:0.11):0.14,580_523:0.37):0.06,(((649_165:0.39,887_205:0.66):0.05,((640_46:0.35,571_85:0.27):0.06,597_125:0.36):0.02):0.02,((632_284:0.25,(573_324:0.14,557_364:0.13):0.04):0.05,516_245:0.16):0.15):0.06):0.06):0.16;");
//		// small breaks
//		extractAndTestGraph(new File(Fixtures.IJSEM_OPEN_DIR, "ijs.0.019638-0-000.pbm.png"), 
//				"(((533_488:0.52,641_451:0.69):0.15:0.07,((289_188:0.03,349_152:0.13):0.26,293_245:0.3):0.06):0.0,(342_109:0.35,380_68:0.41):0.08):0.09;");
	}
	

//	private void extractAndTestGraph(File file, String expectedNewick) {
//		PhyloTreePixelAnalyzer analyzer = new PhyloTreePixelAnalyzer();
//		DiagramTree tree = analyzer.createTreeFromImageFile(file);
//		tree.setOutputDistances(ComparatorType.LEFT);
//		String newick = tree.createNewickFromXML();
//		Assert.assertEquals("newick", expectedNewick, newick);
//
//	}
	
}
