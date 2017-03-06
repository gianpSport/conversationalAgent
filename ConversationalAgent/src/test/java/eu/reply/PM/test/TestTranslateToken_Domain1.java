package eu.reply.PM.test;

import java.util.ArrayList;
import java.util.Scanner;

import eu.reply.PM.Token;
import eu.reply.PM.TranslateToken;


public class TestTranslateToken_Domain1 {

	private static Scanner s;

	public static void main(String[] args) {

		String[] exCreate = { "mi puoi creare", "mi creeresti", "creami", "crea", "imposta", "impostami", "metti",
				"mettimi", "mi metteresti", "mi imposteresti", "fissa", "mi puoi impostare", "mi puoi mettere" };

		String[] exCreateAllarm = { "svegliami", "mi puoi svegliare", "sveglieresti" };

		String[] exCreateTimer = { "avvisami", "mi puoi avvisare" };

		String[] exCreateReminder = { "ricordami", "potresti ricordarmi", "mi ricorderesti", "mi puoi ricordare",
				"mi ricordi" };;

		String[] exCheck = { "ho", "ho messo", "ho impostato", "ho creato" };

		String[] exDelete = { "cancella", "cancellami", "mi cancelleresti", "togli", "toglimi", "mi toglieresti" };

		String[] exAllarm = { "sveglia", "una sveglia", "sveglie" };

		String[] exTimer = { "timer", "un timer" };

		String[] exReminder = { "promemoria", "evento", "un promemoria", "un evento" };

		String[] exMeeting = { "appuntamento" };

		String[] exStartTime = { "alle 12:00", "alle 12:30", "alle 13:00", "12:00", "12:30", "13:00" };

		String[] exDuration = { "1 ora", "x ore", "x ore e x minuti", "1 ora x minuti x secondi", "x minuti",
				"1 minuto", "x secondi", "1 secondo", "10 minuti" };

		String[] exDay = { "domani", "dopodomani", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato",
				"domenica" };

		String[] exSalutare = { "ciao", "buongiorno", "buonasera", "buonanotte", "salve", "i miei ossequi" };

		String[] exRingraziare = { "grazie", "la ringrazio" };

		String[] exConfirm = { "si", "va bene", "accetto", "ok" };

		String[] exCancel = { "no", "lascia stare", "annulla" };

		String[] exCortesia = { "per piacere", "per favore", "cortesemente", "gentilmente" };

		TranslateToken translater = new TranslateToken();

		float t = 0.8f;

		translater.createSemanticPool("action", t, 100);
		translater.addSemanticElement("action", "create");
		for (String ex : exCreate) {
			translater.addExample("action", "create", ex);
		}

		translater.addSemanticElement("action", "create+allarm");
		for (String ex : exCreateAllarm) {
			translater.addExample("action", "create+allarm", ex);
		}

		translater.addSemanticElement("action", "create+timer");
		for (String ex : exCreateTimer) {
			translater.addExample("action", "create+timer", ex);
		}

		translater.addSemanticElement("action", "create+reminder");
		for (String ex : exCreateReminder) {
			translater.addExample("action", "create+reminder", ex);
		}

		translater.addSemanticElement("action", "check");
		for (String ex : exCheck) {
			translater.addExample("action", "check", ex);
		}

		translater.addSemanticElement("action", "delete");
		for (String ex : exDelete) {
			translater.addExample("action", "delete", ex);
		}

		translater.createSemanticPool("domain", t, 90);
		translater.addSemanticElement("domain", "allarm");
		for (String ex : exAllarm) {
			translater.addExample("domain", "allarm", ex);
		}

		translater.addSemanticElement("domain", "timer");
		for (String ex : exTimer) {
			translater.addExample("domain", "timer", ex);
		}

		translater.addSemanticElement("domain", "reminder");
		for (String ex : exReminder) {
			translater.addExample("domain", "reminder", ex);
		}

		translater.addSemanticElement("domain", "meeting");
		for (String ex : exMeeting) {
			translater.addExample("domain", "meeting", ex);
		}

		translater.createSemanticPool("property", t, 80);
		translater.addSemanticElement("property", "time-start");
		for (String ex : exStartTime) {
			translater.addExample("property", "time-start", ex);
		}

		translater.addSemanticElement("property", "duration");
		for (String ex : exDuration) {
			translater.addExample("property", "duration", ex);
		}

		translater.addSemanticElement("property", "day");
		for (String ex : exDay) {
			translater.addExample("property", "day", ex);
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
//				System.out.println(tok);
//				String frag = input.substring(tok.getIndex(), tok.getEnd());
//				System.out.println("FRAGMENT: " + frag);
				semanticMessage+=tok.getResult();
			}
			System.out.println("SEMANTIC REPRESENTATION: "+semanticMessage);

		}
	}

}
