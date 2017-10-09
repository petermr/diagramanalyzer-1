package org.xmlcml.diagrams;

import java.io.File;

import org.xmlcml.diagrams.phylo.LogMessage;
import org.xmlcml.xml.XMLUtil;

import nu.xom.Element;

public class DiagramLogOLD {

	private File file;
	private Element element;
	
	public DiagramLogOLD(String file) {
		this.element = new Element("log");
		setOutput(new File(file));
	}
	
	public void add(Element msg) {
		if (element != null) {
			element.appendChild(msg);
		}
	}

	public void setOutput(File output) {
		this.file = output;
	}

	public void write() {
		if (file != null) {
			XMLUtil.outputQuietly(element, file, 1);
		}
	}

	public void appendChild(LogMessage logMessage) {
		element.appendChild(logMessage);
	}

	public boolean hasMessage() {
		return element.getChildElements().size() > 0;
	}
}
