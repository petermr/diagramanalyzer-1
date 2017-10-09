package org.xmlcml.diagrams;

import nu.xom.Element;

import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.image.pixel.PixelComparator.ComparatorType;
import org.xmlcml.image.pixel.PixelGraph;
import org.xmlcml.image.pixel.PixelNode;

/** creates simple XMLTree for PixelGraph.
 * 
 * @author pm286
 *
 */
public class DiagramTreeOLD {


	private static final Logger LOG = Logger.getLogger(DiagramTreeOLD.class);
	
	private static final int OFFSET = 20;
	static final String _ROOT = "_root";
	
	private PixelGraph graph;
	ComparatorType outputDistances = null;
	Real2Range boundingBox;
	Double maxDist;
	int decimalPlaces = 2;
	private PixelNode rootPixelNode;
	public XMLNodeOLD rootXMLNode;

	
	public DiagramTreeOLD() {
	}


	public void setDecimalPlaces(int d) {
		this.decimalPlaces  = d;
	}
	

	/** sets orientation for distance comparison.
	 * 
	 * LEFT, RIGHT, HORIZONTAL => HORIZONTAL
	 * TOP, BOTTOM, VERTICAL   => VERTICAL
	 * 
	 * @param type
	 */
	public void setOutputDistances(ComparatorType type) {
		if (type == null) {
			this.outputDistances  = type;
		} else if (ComparatorType.LEFT.equals(type)) {
			this.outputDistances  = ComparatorType.HORIZONTAL;
		} else if (ComparatorType.RIGHT.equals(type)) {
			this.outputDistances  = ComparatorType.HORIZONTAL;
		} else if (ComparatorType.TOP.equals(type)) {
			this.outputDistances  = ComparatorType.VERTICAL;
		} else if (ComparatorType.BOTTOM.equals(type)) {
			this.outputDistances  = ComparatorType.VERTICAL;
		} else {
			this.outputDistances  = type;
		}
	}
	

	public void setGraph(PixelGraph pixelGraph) {
		this.graph = pixelGraph;
	}

	public XMLNodeOLD getRootXMLNode() {
		return rootXMLNode;
	}

	public PixelGraph getGraph() {
		return graph;
	}

	public XMLNodeOLD createXMLNode() {
		return new XMLNodeOLD(this);
	}
	
	public XMLNodeOLD createXMLNode(String label) {
		return new XMLNodeOLD(this, label);
	}
	
	public XMLNodeOLD createXMLNode(String label, Real2 xy2) {
		XMLNodeOLD node = new XMLNodeOLD(this, label);
		node.setXY2(xy2);
		return node;
	}
	
	public Real2Range getBoundingBox() {
		boundingBox = new Real2Range();
		addDescendantBoundingBoxes(rootXMLNode);
		LOG.trace(">bb> "+boundingBox);
		if (ComparatorType.HORIZONTAL.equals(outputDistances)) {
			maxDist = boundingBox.getXRange().getRange();
		} else if (ComparatorType.VERTICAL.equals(outputDistances)) {
			maxDist = boundingBox.getYRange().getRange();
		} else {
			// FIXME check rename worked
			maxDist = boundingBox.getLLURCorners()[0].getDistance(boundingBox.getLLURCorners()[1]);
		}
		return boundingBox;
	}

	private void addDescendantBoundingBoxes(XMLNodeOLD xmlNode) {
		String xy2S = xmlNode.getAttributeValue(XMLNodeOLD.XY2);
		boundingBox.add(Real2.createFromString(xy2S));
		for (int i = 0; i < xmlNode.getChildElements().size(); i++) {
			Element child = xmlNode.getChildElements().get(i);
			if (child instanceof XMLNodeOLD) {
				addDescendantBoundingBoxes((XMLNodeOLD) child);
			}
		}
	}


	private void numberTerminalNodes(PixelGraph pixelGraph) {
		int i = 0;
		for (PixelNode node : pixelGraph.getNodeList()) {
			if (node != null) {
				node.setLabel("n"+i);
			}
			i++;
		}
	}

//	public XMLNode getRootNode() {
//		return rootXMLNode;
//	}

//	public String createNewickFromXML() {
//		if (rootXMLNode == null) {
//			throw new RuntimeException("cannot create Newick from null tree");
//		}
//		StringBuilder sb = new StringBuilder();
//		if (rootXMLNode != null) {
//			if (outputDistances != null) {
//				getBoundingBox();
//			}
//			rootXMLNode.appendNewickBody(sb, null);
//			sb.append(";");
//		}
//		return sb.toString();
//	}
//
	public XMLNodeOLD createAndAddXMLNode(XMLNodeOLD parent) {
		return createAndAddXMLNode(parent, null, null);
	}

	public XMLNodeOLD createAndAddXMLNode(XMLNodeOLD parent, Real2 xy2) {
		return createAndAddXMLNode(parent, null, xy2);
	}

	public XMLNodeOLD createAndAddXMLNode(XMLNodeOLD parent, String label) {
		return createAndAddXMLNode(parent, label, null);
	}

	public XMLNodeOLD createAndAddXMLNode(XMLNodeOLD parent, String label, Real2 xy2) {
		XMLNodeOLD node = null;
		if (parent != null) {
			node = createXMLNode(label, xy2);
			parent.appendChild(node);
		}
		return node;
	}
	
	public SVGG getOrCreateSVG() {
		SVGG g = new SVGG();
		if (rootXMLNode != null) {
			g.appendChild(rootXMLNode.getOrCreateSVG());
		}
		return g;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append((rootXMLNode == null) ? "null root " : rootXMLNode.toXML());
		return sb.toString();
	}

	public void tidyGraph(double d) {
		if (graph != null) {
			graph.tidyNodesAndEdges((int)d);
		}
	}


	public PixelNode getRootPixelNode() {
		this.rootPixelNode = (this.rootPixelNode == null && graph != null) ? graph.getRootPixelNode() : rootPixelNode;
		return rootPixelNode;
	}


	public void setRootPixelNode(PixelNode rootPixelNode) {
		this.rootPixelNode = rootPixelNode;
	}

	
}
