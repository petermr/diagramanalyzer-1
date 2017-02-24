=== Running ===

Very simple instructions to get off the ground. Only for alpha testers at present.

In org.xmlcml.diagrams.PhyloTreeTest:

	@Test
	public void test36933Segments() throws Exception {
		PixelGraph pixelGraph = createAndDrawPolylines("0036933.g001");
		List<PixelNode> rootNodes = pixelGraph.getPossibleRootNodes();
		LOG.debug("root "+rootNodes);
		if (rootNodes.size() == 1) {
			String newick = new PhyloTree().createNewick(pixelGraph, rootNodes.get(0));
			LOG.debug("N "+newick);
		}
	}

Run this and get a Newick String (buggy) printed to the console.

The image files are in:
src/test/resources/org/xmlcml/diagrams/ross and of the form "pone.0018360.g001.png" 
you can change the file  through the string "0018360.g001"


To run, create a new Test with your fileroot and run it (right click, | run as Java Unit test)

You need an explict root node (I will change this later) so try 17932 and 18360

for commandline try:
sh target/appassembler/bin/phylotree --debug --input ./ijsemopen/ijs.0.014126-0-000.pbm.png --island 0 --newick target/ijs.0.014126-0-000.nwk


