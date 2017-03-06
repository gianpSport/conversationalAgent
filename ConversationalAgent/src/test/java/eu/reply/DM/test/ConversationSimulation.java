package eu.reply.DM.test;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.reply.Config.Config;
import eu.reply.DM.DialogManager;
import eu.reply.SN.Reader;
import eu.reply.SN.SemanticNet;

public class ConversationSimulation {
	
	private static Scanner s;
	
	public static void main(String[] args){
		String url = Config.getPathSemanticNet();
		float threshold = 0.9f;
		SemanticNet net=new SemanticNet(url);
		//net.printModel();
		JSONObject read=Reader.readNLU(net.getModel());
		//System.out.println(read.toString(4));
		DialogManager contex = new DialogManager(net, threshold,read);
		
		String input = "";
		s = new Scanner(System.in);
		
		while (true) {
			System.out.print(">>");
			input = s.nextLine();

//			System.out.println("INPUT: " + input);
			input = input.toLowerCase();
			String out = contex.realMessage(input);
//			System.out.println("------------------DOMAIN------------------");
//			while (out.length() > 0) {
//				JSONObject outputJSON = new JSONObject(out);
//				out = out.replace(outputJSON.toString(), "");
//				if (outputJSON.has("result")) {
//					String result = outputJSON.get("result").toString();
//					if (result.charAt(0) == '[') {
//						System.out.println(new JSONArray(result).toString(4));
//					} else if (result.charAt(0) == '{') {
//						System.out.println(new JSONObject(result).toString(4));
//					} else {
//						System.out.println(result);
//					}
//				} else {
//					System.out.println("No Result for:\n" + outputJSON.get("query"));
//				}
//			}
		System.out.println(out);
		}
	}

}
