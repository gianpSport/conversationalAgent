package eu.reply.semanticNetwork.test;

import java.util.List;

import eu.reply.Config.Config;
import eu.reply.SN.SemanticNet;

public class TestSemNet {

	public static void main(String[] args) {
		String url=Config.getPathSemanticNet();
		SemanticNet net=new SemanticNet(url);
		
		String[] classes={"action","domain","property","value","mod","number","unit"};
		
		for(String c:classes){
			List<String> template=net.getTemplate(c);
			System.out.print(c+":"+"< ");
			for(String s:template){
				System.out.print(s+" ");
			}
			System.out.println(">");
		}

	}

}
