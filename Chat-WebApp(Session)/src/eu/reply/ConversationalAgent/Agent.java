package eu.reply.ConversationalAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import eu.reply.DM.DialogManager;
import eu.reply.SN.Reader;
import eu.reply.SN.SemanticNet;
import ibm.watson.conversation.WatsonConversation;

public class Agent extends WatsonConversation{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8057452002437485537L;
	
	private final String url="lavazza_full.rdf";
	private final float t=0.8f;
	private DialogManager agent;
	private final boolean DEBUG=false;
	
	public Agent(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		String conversation_id = dateFormat.format(cal.getTime());
		if (DEBUG)
			System.out.println("ID CONVERSATION: " + conversation_id);
		super.context = new JSONObject();
		super.context.accumulate("conversation_id", conversation_id);
		SemanticNet net=new SemanticNet(url);
		JSONObject lexical=Reader.readNLU(net.getModel());
		agent=new DialogManager(net, t,lexical);
	}
	
	public String first_message(){
		return "Ciao, sono il tuo assistente virtuale. Posso aiutarti a scoprire i prodotti Lavazza. chiedimi pure informazioni sulle capsule e sulle macchine da caffè...";
		
	}
	
	public String sendMessage(String input){
		String message= agent.realMessage(input.toLowerCase());
		message=message.replaceAll("\n", "<br>");
		message=message.replaceAll(" ", "&nbsp;");
		return message;
	}

}
