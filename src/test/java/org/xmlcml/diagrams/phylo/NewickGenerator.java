package org.xmlcml.diagrams.phylo;

import nu.xom.Attribute;
import nu.xom.Element;

/** siple Newick generator for testing.
 * 
 * @author pm286
 *
 */
public class NewickGenerator {

	public NewickGenerator() {
		
	}
	
	String makeNewick(Element xml) {
		StringBuilder sb = new StringBuilder();
		makeNewick(xml, sb);
		return sb.toString()+";";
	}
	

	private void makeNewick(Element xml, StringBuilder sb) {
		String label = xml.getAttributeValue("label");
		if (xml.getChildCount() == 0) {
			sb.append(label);
		} else {
			sb.append("(");
			for (int i = 0; i < xml.getChildCount(); i++) {
				makeNewick((Element)xml.getChild(i), sb);
				if (i < xml.getChildCount() - 1) {
					sb.append(",");
				}
			}
			sb.append(")");
		}
	}
	
	/** utility method for creating test tree.
	 * 
	 * @param parent
	 * @param label
	 * @return
	 */
	Element createAndAddNewNode(Element parent, String label) {
		Element child = new Element("node");
		if (label != null) {
			child.addAttribute(new Attribute("label", label));
		}
		parent.appendChild(child);
		return child;
	}



}
