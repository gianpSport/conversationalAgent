package eu.reply.PM.test;

import eu.reply.NLP.OpeNER;
import eu.reply.NLP.Tree;

public class TestOpeNER {
	public static void main(String[] args) throws Exception {
		String text = "mi daresti un camion di briciole e una barca di soldi.";
		String output = null;
		int[] pipe = { 0, 1, 3, 4 };
		try {
			output = OpeNER.parsing(text, pipe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(output);
		
		Tree treeOutput=OpeNER.parseInTree(text);
		treeOutput.deepStampTree();
	}	
}
