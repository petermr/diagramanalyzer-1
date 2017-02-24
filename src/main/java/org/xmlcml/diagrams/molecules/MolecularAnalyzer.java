package org.xmlcml.diagrams.molecules;

import java.io.File;

import org.apache.log4j.Logger;
import org.xmlcml.diagrams.DiagramAnalyzer;

public class MolecularAnalyzer extends DiagramAnalyzer {
	
	private final static Logger LOG = Logger.getLogger(MolecularAnalyzer.class);

	public MolecularAnalyzer() {
		// TODO Auto-generated constructor stub
	}
	
	public void makeMolecule(File inputFile) {
		this.setInputFile(inputFile);
		this.processGraphList();
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			MolecularAnalyzer molecularAnalyzer = new MolecularAnalyzer();
			File imageFile = new File(args[0]);
			LOG.trace("file "+imageFile.exists());
			molecularAnalyzer.makeMolecule(imageFile);
		}
	}

}
