package org.xmlcml.diagrams;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Logger;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Util;
import org.xmlcml.graphics.svg.SVGCircle;
import org.xmlcml.graphics.svg.SVGG;
import org.xmlcml.graphics.svg.SVGLine;
import org.xmlcml.image.pixel.PixelComparator.ComparatorType;

public class XMLNodeOLD extends Element {

	private static final String DIST_SEP = ":";

	private final static Logger LOG = Logger.getLogger(XMLNodeOLD.class);
	
	static final String XY2 = "xy2";
	static final String LABEL = "label";
	static final String D2PARENT = "d2parent";
	public final static String TAG = "node";

	public DiagramTreeOLD tree;
	private double nodeRadius = 2.0;
	
	public XMLNodeOLD(DiagramTreeOLD tree) {
		super(TAG);
		this.tree = tree;
	}
	
	public XMLNodeOLD(DiagramTreeOLD tree, String label) {
		this(tree);
		setLabel(label);
	}

	public void setLabel(String label) {
		if (label != null) {
			addAttribute(new Attribute(LABEL, label));
		}
	}
	
	public String getLabel() {
		return getAttributeValue(LABEL);
	}

	public void setXY2(Real2 coord) {
		addAttribute(new Attribute(XY2, String.valueOf(coord)));
	}
	
	public Real2 getXY2() {
		String s = getAttributeValue(XY2);
		Real2 xy2 = (s == null || s.equals("")) ? null : Real2.createFromString(s);
		return xy2;
	}
	
	public void setDistanceToParent(double d) {
		this.addAttribute(new Attribute(D2PARENT, String.valueOf(d)));
	}
	
	public Double getDistanceToParent() {
		String dS = getAttributeValue(D2PARENT);
		return (dS == null || dS.equals("")) ? null : new Double(dS);
	}
	
	public void addNode(XMLNodeOLD node) {
		this.appendChild(node);
		node.setTree(this.tree);
	}

	private void setTree(DiagramTreeOLD tree) {
		this.tree = tree;
	}

	void appendNewickBody(StringBuilder sb, XMLNodeOLD parent) {
		int nchild = getChildElements().size();
		if (nchild == 0) {
			String l = this.getLabel();
			sb.append(l == null ? "no_label" : l);
		} else {
			if (nchild > 1) sb.append("(");
			for (int i = 0; i < nchild; i++) {
				XMLNodeOLD node = (XMLNodeOLD) getChildElements().get(i);
				node.appendNewickBody(sb, this);
				node.addDistance(sb, this);
				if (i < this.getChildElements().size() - 1) sb.append(",");
			}
			if (nchild > 1) sb.append(")");
		}
	}

	private void addDistance(StringBuilder sb, XMLNodeOLD parent) {
		if (tree.outputDistances != null) {
			Double delta = getScaledDelta(parent);
			if (delta != null) {
				sb.append(DIST_SEP);
				sb.append(delta);
			}
		}
	}

	/** distance to parent scaled by BoundingBox.
	 * 
	 * @param node
	 * @return
	 */
	private Double getScaledDelta(XMLNodeOLD node) {
		Real2 thisXY = this.getXY2();
		Real2 nodeXY = node.getXY2();
		Double d = null;
		if (thisXY != null && nodeXY != null) { 
			if (ComparatorType.HORIZONTAL.equals(tree.outputDistances)) {
				d = thisXY.getX() - nodeXY.getX();
			} else if (ComparatorType.VERTICAL.equals(tree.outputDistances)) {
				d = thisXY.getY() - nodeXY.getY();
			} else {
				d = thisXY.getDistance(nodeXY);
			}
			d = Util.format(Math.abs(d) / tree.maxDist, tree.decimalPlaces);
		}
		return d;
	}
	
	public SVGG getOrCreateSVG() {
		SVGG g = new SVGG(); 
		SVGCircle circle = new SVGCircle(this.getXY2(), nodeRadius);
		g.appendChild(circle);
		Elements childElements = this.getChildElements();
		for (int i = 0; i < childElements.size(); i++) {
			XMLNodeOLD child = (XMLNodeOLD) childElements.get(i);
			SVGLine line = new SVGLine(this.getXY2(), child.getXY2());
			g.appendChild(line);
			SVGG gg = child.getOrCreateSVG();
			g.appendChild(gg);
		}
		return g;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.getLabel() != null) sb.append(getLabel());
		if (this.getXY2() != null) sb.append(getXY2());
		return sb.toString();
	}

}
