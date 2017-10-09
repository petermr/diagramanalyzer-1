package org.xmlcml.diagrams.molecules;

import java.io.File;

import org.apache.log4j.Logger;
import org.xmlcml.diagrams.DiagramAnalyzerOLD;

public class MolecularAnalyzer extends DiagramAnalyzerOLD {
	
	private final static Logger LOG = Logger.getLogger(MolecularAnalyzer.class);

	public MolecularAnalyzer() {
	}
	
	public void makeMolecule(File inputFile) {
		getOrCreateGraphList(inputFile);
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
