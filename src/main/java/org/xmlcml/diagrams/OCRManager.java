package org.xmlcml.diagrams;

import java.awt.image.BufferedImage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGText;

/** this relies on javaocrnew and I think we should abandon it.
 * 
 * @author pm286
 *
 */
public class OCRManager {
	private static final Logger LOG = Logger.getLogger(OCRManager.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

//	
//	static OCRScanner scanner = makeOCRScanner();
//	private static int defaultFontSize = 20;
//	private static FontRenderContext defaultFRC = new FontRenderContext(new AffineTransform(), false, true);
//
//	List<SVGText> ambiguousTexts;
//	HashMap<SVGText, Real2Range> imagesForAmbiguousTexts;
//	HashMap<SVGText, Double> scaleFactorsForAmbiguousTexts;
//	ArrayList<SVGText> unambiguousTexts;
//	Real2Range range = new Real2Range();
//	
//	public OCRManager() {
//		ambiguousTexts = new ArrayList<SVGText>();
//		imagesForAmbiguousTexts = new HashMap<SVGText, Real2Range>();
//		scaleFactorsForAmbiguousTexts = new HashMap<SVGText, Double>();
//		unambiguousTexts = new ArrayList<SVGText>();
//	}
//	
	public SVGText scan(BufferedImage bufferedImage, Real2Range image, double blackThreshold, double maximumOCRError) {
		LOG.error("scan(); skipped");
//		String s = "";
//		final double[] accuracy = new double[]{Double.MAX_VALUE};
//		synchronized(scanner) {
//			scanner.acceptAccuracyListener(new AccuracyListenerInterface() {
//				public void processCharOrSpace(OCRIdentification identAccuracy) {
//					if (identAccuracy.getNumChars() != 0 && identAccuracy.getCharIdx(0) != '\n') {
//						accuracy[0] = identAccuracy.getIdentErrorIdx(0);
//					}
//				}
//			});
//			s = scanner.scanSingleLine(bufferedImage, 0, 0, 0, 0, null);
//		}
//		
//		if (s.length() >= 1 && accuracy[0] < maximumOCRError) {
//			s = s.substring(0, 1);
//			if (s.equals(" ")) {
//				return null;
//			}
//			int whiteRowsAtTop = countWhiteRowsAtTop(bufferedImage, blackThreshold);
//			int whiteRowsAtBottom = countWhiteRowsAtBottom(bufferedImage, blackThreshold);
//	
//			Font defaultFont = new Font(scanner.getLastFoundFontName(), 0, defaultFontSize);
//			GlyphVector glyphVector = defaultFont.createGlyphVector(defaultFRC, s);
//			Rectangle2D bounds = glyphVector.getVisualBounds();
//			double scaleFactorByHeight = Math.abs(calculateHeightBasedScaleFactor(image, bufferedImage, whiteRowsAtTop, whiteRowsAtBottom, bounds));
//			double fontSize = scaleFactorByHeight * defaultFontSize;
//			double xCoord = -bounds.getMinX() * scaleFactorByHeight + image.getXMin();
//			double yCoord = -bounds.getMinY() * scaleFactorByHeight + image.getYMin();// + image.getYRange().getRange() + 1;
//			
//			SVGText text = new SVGText(new Real2(0, 0), s);
//			text.setFontSize(fontSize);
//			text.setFontFamily(scanner.getLastFoundFontName().replace(" Italic",""));
//			if (scanner.getLastFoundFontName().contains("Italic")) {
//				text.setFontStyle("italic");
//			}
//			text.setSVGXFontWidth((1000 * scaleFactorByHeight * glyphVector.getLogicalBounds().getWidth()) / fontSize);//defaultFont.getLineMetrics(s, defaultFRC).getHeight()));
//			text.setXY(new Real2(xCoord, yCoord));
//			try {
//				text.removeAttribute(text.getAttribute("style"));
//			} catch (NullPointerException e) {
//	
//			}
//			
//			range.add(text.getXY());
//			if (s.matches("[cosuvwxzCOSUVWXZ]")) {
//				ambiguousTexts.add(text);
//				imagesForAmbiguousTexts.put(text, image);
//				scaleFactorsForAmbiguousTexts.put(text, scaleFactorByHeight);
//			} else {
//				unambiguousTexts.add(text);
//			}
//			return text;
//		}
		return null;
	}
//
	public void cancel(SVGText text) {
		LOG.error("omitted cancel()");
//		ambiguousTexts.remove(text);
//		imagesForAmbiguousTexts.remove(text);
//		scaleFactorsForAmbiguousTexts.remove(text);
//		unambiguousTexts.remove(text);
	}
//	
//	private static OCRScanner makeOCRScanner() {
//		TrainingImageLoader l = new TrainingImageLoader();
//		HashMap<Character, ArrayList<TrainingImage>> dest = new HashMap<Character, ArrayList<TrainingImage>>();
//		try {
//			//l.loadASCIIFont("Helvetica", dest);
//			//l.loadASCIIFont("Helvetica Italic", dest);
//			l.loadASCIIFont("Times New Roman", dest);
//			l.loadASCIIFont("Times New Roman Italic", dest);
//			l.loadASCIIFont("Sans", dest);
//			l.loadASCIIFont("Sans Italic", dest);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		OCRScanner scanner = new OCRScanner();
//		scanner.addTrainingImages(dest);
//		return scanner;
//	}
//
//	private double calculateHeightBasedScaleFactor(Real2Range image, BufferedImage bufferedImage, int whiteRowsAtTop, int whiteRowsAtBottom, Rectangle2D bounds) {
//		double scaleFactorByHeight = ((image.getYRange().getRange() / bufferedImage.getHeight()) * (bufferedImage.getHeight() - whiteRowsAtBottom - whiteRowsAtTop)) / bounds.getHeight();
//		return scaleFactorByHeight;
//	}
//
//	private int countWhiteRowsAtBottom(BufferedImage bufferedImage, double blackThreshold) {
//		int numChannels = bufferedImage.getSampleModel().getNumBands();
//		int[] pixels = bufferedImage.getRaster().getPixels(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), (int[]) null);
//		int whiteRowsAtBottom = 0;
//		outer: for (int i = pixels.length - bufferedImage.getWidth() * numChannels ; i >= 0; i -= bufferedImage.getWidth() * numChannels) {
//			for (int j = i; j < i + bufferedImage.getWidth() * numChannels; j += numChannels) {
//				if (pixels[j] < blackThreshold && (numChannels == 4 ? pixels[j + 3] != -1 && pixels[j + 3] != 0 : true)) {
//					break outer;
//				}
//			}
//			whiteRowsAtBottom++;
//		}
//		return whiteRowsAtBottom;
//	}
//
//	private int countWhiteRowsAtTop(BufferedImage bufferedImage, double blackThreshold) {
//		int numChannels = bufferedImage.getSampleModel().getNumBands();
//		int[] pixels = bufferedImage.getRaster().getPixels(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), (int[]) null);
//		int whiteRowsAtTop = 0;
//		outer: for (int i = 0; i < pixels.length; i += bufferedImage.getWidth() * numChannels) {
//			for (int j = i; j < i + bufferedImage.getWidth() * numChannels; j += numChannels) {
//				if (pixels[j] < blackThreshold && (numChannels == 4 ? pixels[j + 3] != -1 && pixels[j + 3] != 0 : true)) {
//					break outer;
//				}
//			}
//			whiteRowsAtTop++;
//		}
//		return whiteRowsAtTop;
//	}
//	
	public void handleAmbiguousTexts(double textJoiningPositionToleranceRelativeToFontSize, double relativeFontSizeTolerance) {
	LOG.error("handleAmbiguousTexts() skipped");
//		//defaultFont.getLineMetrics("", defaultFRC).getHeight()
//		//HashMultiset<Double> multisetOfNonAmbiguousFontSizes = HashMultiset.create(unambiguousFontSizes.values());
//		if (ambiguousTexts.size() == 0 || unambiguousTexts.size() == 0) {
//			return;
//		}
//		Real2[] corners = range.getCorners();
//		double overallSize = corners[0].getDistance(corners[1]);
//		for (SVGText svgText : ambiguousTexts) {
//			String text = svgText.getText();
//			double currentFontSize = svgText.getFontSize();
//			Font defaultFont = new Font(svgText.getFontFamily(), 0, defaultFontSize);
//			double potentialFontSize = getOtherPossibleFontSize(defaultFont, defaultFRC, text, currentFontSize);
//			double currentFontSizeMatches = 0;
//			double potentialFontSizeMatches = 0;
//			for (SVGText unambiguous : unambiguousTexts) {
//				double difference = Math.abs(svgText.getXY().getY() - unambiguous.getXY().getY());
//				double averageFontSize1 = (currentFontSize + unambiguous.getFontSize()) / 2;
//				double averageFontSize2 = (potentialFontSize + unambiguous.getFontSize()) / 2;
//				if (difference / averageFontSize1 < textJoiningPositionToleranceRelativeToFontSize) {
//					currentFontSizeMatches++;
//				}
//				if (difference / averageFontSize2 < textJoiningPositionToleranceRelativeToFontSize) {
//					potentialFontSizeMatches++;
//				}
//			}
//			if (potentialFontSizeMatches == 0 && currentFontSizeMatches == 0) {
//				for (SVGText unambiguous : unambiguousTexts) {
//					double nearness = 1 - (svgText.getXY().getDistance(unambiguous.getXY()) / overallSize);
//					nearness *= nearness;
//					double minimum = Math.min(unambiguous.getFontSize(), currentFontSize);
//					double maximum = Math.max(unambiguous.getFontSize(), currentFontSize);
//					if (minimum / maximum > relativeFontSizeTolerance) {
//						currentFontSizeMatches += nearness;
//					}
//					minimum = Math.min(unambiguous.getFontSize(), potentialFontSize);
//					maximum = Math.max(unambiguous.getFontSize(), potentialFontSize);
//					if (minimum / maximum > relativeFontSizeTolerance) {
//						potentialFontSizeMatches += nearness;
//					}
//				}
//			}
//			/*for (com.google.common.collect.Multiset.Entry<Double> size : multisetOfNonAmbiguousFontSizes.entrySet()) {
//				if (Math.abs(size.getElement() - currentFontSize) < fontSizeTolerance) {
//					currentFontSizeMatches += size.getCount();
//				}
//				if (Math.abs(size.getElement() - potentialFontSize) < fontSizeTolerance) {
//					potentialFontSizeMatches += size.getCount();
//				}
//			}*/
//			if (potentialFontSizeMatches > currentFontSizeMatches) {
//				svgText.setFontSize(potentialFontSize);
//				String newText = (potentialFontSize > currentFontSize ? text.toLowerCase() : text.toUpperCase());
//				svgText.setText(newText);
//				GlyphVector glyphVector = defaultFont.createGlyphVector(defaultFRC, newText);
//				Rectangle2D bounds = glyphVector.getVisualBounds();
//				double scaleFactorByHeight = scaleFactorsForAmbiguousTexts.get(svgText) * potentialFontSize / currentFontSize;
//				double xCoord = -bounds.getMinX() * scaleFactorByHeight + imagesForAmbiguousTexts.get(svgText).getXMin();
//				double yCoord = -bounds.getMinY() * scaleFactorByHeight + imagesForAmbiguousTexts.get(svgText).getYMin();// + imagesForAmbiguousTexts.get(svgText).getYRange().getRange();
//				svgText.setXY(new Real2(xCoord, yCoord));
//				svgText.setSVGXFontWidth((1000 * scaleFactorByHeight * glyphVector.getLogicalBounds().getWidth()) / potentialFontSize);
//			}
//		}
	}
//
//	private double getOtherPossibleFontSize(Font defaultFont, FontRenderContext defaultFRC, String text, double currentFontSize) {
//		GlyphVector glyphVectorLower = defaultFont.createGlyphVector(defaultFRC, text.toLowerCase());
//		GlyphVector glyphVectorUpper = defaultFont.createGlyphVector(defaultFRC, text.toUpperCase());
//		double heightLower = glyphVectorLower.getVisualBounds().getHeight();
//		double heightUpper = glyphVectorUpper.getVisualBounds().getHeight();
//		double potentialFontSize = (text.toLowerCase().equals(text) ? (heightLower / heightUpper) : (heightUpper / heightLower)) * currentFontSize;
//		return potentialFontSize;
//	}

}