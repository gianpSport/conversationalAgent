package org.watson.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.reply.ConversationalAgent.Agent;


@Controller
public class WatsonServlet {
	
	//Modifica mago di Oz
	//private WatsonConversation watsonConversation;
	private Agent watsonConversation;
	
	static final String path_Conversation = "/home/administrator/Documents/ConversationRecord/";

	
	@RequestMapping("/welcome")
	public @ResponseBody String welcomeMessage(HttpSession session) {
		String message = "";
		//try {
			//Modifica per utilizzare il mago di Oz
			//watsonConversation = new WatsonConversation(credential, workspaceID, version);
			watsonConversation=new Agent();
			
			session.setAttribute("watson", watsonConversation);
			String response=watsonConversation.first_message();
			message = "<p><b>" +response+ "</b></p>";
			try {
				watsonConversation.write_Conversation(path_Conversation, response, "WizardOfOz");
			} catch (IOException e) {
				System.err.println("Errore nella scrittura su file di conversazione risposta di watson!! conversation id: "+watsonConversation.conversation_id());
				e.printStackTrace();
			}
//		} catch (JSONException | IOException e) {
//			message = "<p><b>Errore nella creazione di Conversation!!</b></p>";
//			e.printStackTrace();
//		}

		return message;
	}

	@RequestMapping(value = "/response", method = RequestMethod.GET, params = { "input" })
	public @ResponseBody String getResponse(@RequestParam("input") String input_text, HttpSession session) {

		
		String result = "";
		
		//Modifica per mago di Oz
		//watsonConversation = (WatsonConversation) session.getAttribute("watson");
		
		watsonConversation=(Agent)session.getAttribute("watson");
		
		if (watsonConversation != null) {
			System.out.println("input: " + input_text);
			try {
				watsonConversation.write_Conversation(path_Conversation, input_text, "Client");
			} catch (IOException e1) {
				System.err.println("Errore nella scrittura su file di conversazione input utente!! conversation id: "+watsonConversation.conversation_id());
				e1.printStackTrace();
			}
			String response = null;
			//try {
				response = watsonConversation.sendMessage(input_text);
				System.out.println("response : " + response);
				if (response!=null) {
					result = "<p><b>" + response + "</b></p>";
				}
//			} catch (JSONException | IOException e) {
//				result = "<p><b>Errore nelle API guarda server log.</b></p>";
//				e.printStackTrace();
//			}
			if(response!=null){
				try {
					watsonConversation.write_Conversation(path_Conversation, response, "Watson");
				} catch (IOException e) {
					System.err.println("Errore nella scrittura su file di conversazione risposta di watson!! conversation id: "+watsonConversation.conversation_id());
					e.printStackTrace();
				}
			}
		} else {
			result = "<p><b>Errore nella creazione di Conversation.</p>";
		}
		return result;
	}

	@RequestMapping("/chat")
	public ModelAndView chat() {
		return new ModelAndView("chat");
	}

	@RequestMapping("/chatTemplate")
	public ModelAndView chatTemplate() {
		return new ModelAndView("chatTemplate");
	}

	@RequestMapping("/")
	public ModelAndView homePage() {
		return new ModelAndView("chat");
	}

}
