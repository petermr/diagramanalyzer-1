package org.xmlcml.diagrams;

import java.io.File;

public class DiagramsFixtures {

	public final static String[] FILL = {
		"red",
		"green",
		"blue",
		"yellow",
		"purple",
		"cyan",
		"magenta"
	};
	
	public final static File TOP_DIR = new File(".");
	public final static File RESOURCES_DIR = new File("src/test/resources/");
	public final static File DIAGRAMS_DIR = new File(DiagramsFixtures.RESOURCES_DIR, "org/xmlcml/diagrams/");
	public final static File COMPOUND_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "compound");
	public final static File FUNNEL_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "funnel");
	public final static File ROSS_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "ross");
	public static final File MOLECULE_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "molecules");
	public static final File MISC_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "misc");
	public static final File PROBLEM_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "problemTrees");
	public final static File PROCESSING_DIR = new File(DiagramsFixtures.DIAGRAMS_DIR, "processing");
	public static final File IJSEM_OPEN_DIR = new File(DiagramsFixtures.TOP_DIR, "./ijsemopen/");
	
}
