package eu.reply.PM.test;

import java.util.ArrayList;
import java.util.Scanner;

import eu.reply.PM.Token;
import eu.reply.PM.TranslateToken;


public class TestTranslateToken_Domain2 {

	private static Scanner s;

	public static void main(String[] args) {

		
		TranslateToken translater = new TranslateToken();

		float t = 0.8f;

		String[] exSalutare = { "ciao", "buongiorno", "buonasera", "buonanotte", "salve", "i miei ossequi" };

		String[] exRingraziare = { "grazie", "la ringrazio" };

		String[] exConfirm = { "si", "va bene", "accetto", "ok" };

		String[] exCancel = { "no", "lascia stare", "annulla" };

		String[] exCortesia = { "per piacere", "per favore", "cortesemente", "gentilmente" };
		
		String[] exSearch={"avete","vendete","cercavo","avrei bisogno","vorrei comprare","vorrei acquistare","voglio","vorrei"};
		
		String[] exMachine={"macchina","macchinetta","macchinette","macchina da caffè","macchinetta da caffè"};
	
		String[] exNameMachine={"lavazza jolie","jolie","lavazza minù","minù"};
		
		String[] exCapsule={"capsule","capsula"};
		
		String[] exIntensita={"intenso","intensità"};
		
		//String[] exValueIntensità={"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		
		String[] exTostatura={"tostatura","tostato"};
		
		String[] exValueTostatura={"media","scura"};
		
		String[] exComposizione={"contiene","miscela"};
		
		String[] exValueComposizione={"orzo","ginseng","arabica","robusta"};
		
		translater.createSemanticPool("action", t, 100);
		translater.addSemanticElement("action", "search");
		for (String ex : exSearch) {
			translater.addExample("action", "search", ex);
		}
		
		translater.createSemanticPool("domain", t, 90);
		translater.addSemanticElement("domain", "machine");
		for (String ex : exMachine) {
			translater.addExample("domain", "machine", ex);
		}
		
		translater.addSemanticElement("domain", "capsule");
		for (String ex : exCapsule) {
			translater.addExample("domain", "capsule", ex);
		}
		
		translater.createSemanticPool("property", t, 80);
		translater.addSemanticElement("property", "nameMachine");
		for (String ex : exNameMachine) {
			translater.addExample("property", "nameMachine", ex);
		}
		
		translater.addSemanticElement("property", "intensity");
		for (String ex : exIntensita) {
			translater.addExample("property", "intensity", ex);
		}
		
		translater.addSemanticElement("property", "roasting");
		for (String ex : exTostatura) {
			translater.addExample("property", "roasting", ex);
		}
		
		translater.addSemanticElement("property", "composition");
		for (String ex : exComposizione) {
			translater.addExample("property", "composition", ex);
		}
		
		translater.createSemanticPool("value", t, 70);
//		translater.addSemanticElement("value", "intensity");
//		for (String ex : exValueIntensità) {
//			translater.addExample("value", "intensity", ex);
//		}
		
		translater.addSemanticElement("value", "roasting");
		for (String ex : exValueTostatura) {
			translater.addExample("value", "roasting", ex);
		}
		
		translater.addSemanticElement("value", "composition");
		for (String ex : exValueComposizione) {
			translater.addExample("value", "composition", ex);
		}
				
		translater.createSemanticPool("dialog", t, 70);
		translater.addSemanticElement("dialog", "greetings");
		for (String ex : exSalutare) {
			translater.addExample("dialog", "greetings", ex);
		}

		translater.addSemanticElement("dialog", "thanks");
		for (String ex : exRingraziare) {
			translater.addExample("dialog", "thanks", ex);
		}

		translater.addSemanticElement("dialog", "politeness");
		for (String ex : exCortesia) {
			translater.addExample("dialog", "politeness", ex);
		}

		translater.createSemanticPool("question", t, 60);
		translater.addSemanticElement("question", "confirm");
		for (String ex : exConfirm) {
			translater.addExample("question", "confirm", ex);
		}

		translater.addSemanticElement("question", "cancel");
		for (String ex : exCancel) {
			translater.addExample("question", "cancel", ex);
		}
		
		System.out.println(translater);
		String input = "";
		s = new Scanner(System.in);
		while (true) {
			System.out.print(">>");
			input = s.nextLine();

			System.out.println("INPUT: " + input);
			input = input.toLowerCase();

			

			ArrayList<Token> result = translater.parse(input);

			String semanticMessage="";

			for (Token tok : result) {
				System.out.println(tok);
				String frag = input.substring(tok.getStart(), tok.getEnd());
				System.out.println("FRAGMENT: " + frag);
				semanticMessage+=tok.getResult();
			}
			System.out.println("SEMANTIC REPRESENTATION: "+semanticMessage);
		}
	}

}
