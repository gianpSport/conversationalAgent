package eu.reply.DM.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.reply.Config.Config;
import eu.reply.DM.DialogManager;
import eu.reply.SN.Reader;
import eu.reply.SN.SemanticNet;

public class CreateJUnitTest_Lavazza {

	private static Scanner s;

	public static void main(String[] args) {
		String url = Config.getPathSemanticNet();
		String url_example = Config.getPathExample();
		float threshold = 0.9f;
		SemanticNet net = new SemanticNet(url);
		JSONObject read = Reader.readNLU(net.getModel());
		DialogManager contex = new DialogManager(net, threshold, read);
		//net.printModel();
		boolean interactive_mode = false;

		if (!interactive_mode) {
			try (BufferedReader br = new BufferedReader(new FileReader(url_example))) {

				String input;
				int line = 1;
				int start =308;
				int stop = 314;
				while ((input = br.readLine()) != null && line <= stop) {
					if (line >= start) {
						System.out.println(input + " num test: " + line++);
						contex = new DialogManager(net, threshold, read);
						input = input.toLowerCase();
						//String out = contex.compose(input);
						//JSONObject obj = new JSONObject(out);
						//System.out.println(obj.toString(4));
						//out = out.replaceAll("\"", "/\"");

						//System.out.println("String result_compose=\"" + out + "\";");
						contex = new DialogManager(net, threshold, read);
						String out = contex.message(input);
						JSONObject obj = new JSONObject(out);
						System.out.println(obj.toString(4));
						out = out.replaceAll("\"", "/\"");
						System.out.println("String result_message=\"" + out + "\";");
						contex = new DialogManager(net, threshold, read);
						out = contex.realMessage(input);
						System.out.println(out);
					} else {
						line++;
						//System.out.println("SKIP");
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			String input = "";
			s = new Scanner(System.in);
			while (true) {
				System.out.print(">>");
				input = s.nextLine();

				System.out.println("INPUT: " + input);
				input = input.toLowerCase();

				// String out = contex.compose(input);
				// System.out.println(out);
				contex = new DialogManager(net, threshold, read);
				String out = contex.message(input);
				System.out.println(out);
				System.out.println("------------------DOMAIN------------------");
				while (out.length() > 0) {
					JSONObject outputJSON = new JSONObject(out);
					out = out.replace(outputJSON.toString(), "");
					if (outputJSON.has("result")) {
						String result = outputJSON.get("result").toString();
						if (result.charAt(0) == '[') {
							System.out.println(new JSONArray(result).toString(4));
						} else if (result.charAt(0) == '{') {
							System.out.println(new JSONObject(result).toString(4));
						} else {
							System.out.println(result);
						}
					} else {
						System.out.println("No Result for:\n" + outputJSON.get("query"));
					}
				}
			}
		}
	}

}
