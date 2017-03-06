package eu.reply.wizardOfOz;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.json.JSONObject;

import ibm.watson.conversation.WatsonConversation;

public class WizardOfOz extends WatsonConversation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8289551845861081932L;

	private final boolean DEBUG = true;
	Scanner s = new Scanner(System.in);

	public WizardOfOz() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		String conversation_id = dateFormat.format(cal.getTime());
		if (DEBUG)
			System.out.println("ID CONVERSATION: " + conversation_id);
		super.context = new JSONObject();
		super.context.accumulate("conversation_id", conversation_id);
	}

	public String first_message() {
		return "Salve, benvenuto nel lavazza store sono qui per aiutarti nella scelta di capsule lavazza a modo mio?";
	}

	public String sendMessage(String input_text) {
		String result = null;
		try {
			System.out.println("Stream input: " + System.in.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("INPUT: " + input_text);
			System.out.print("OUTPUT: ");
			result = s.nextLine();
			if (result.equalsIgnoreCase("SKIP")) {
				result = null;
			}
		} catch (java.util.NoSuchElementException ex) {
			System.out.println("[Wizard Of Oz]Salto elemento messaggio consecutivo.");
		}
		return result;
	}

}
