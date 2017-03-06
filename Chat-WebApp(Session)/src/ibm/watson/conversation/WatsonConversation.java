package ibm.watson.conversation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import time.Instance;
import time.duration.Durata;
import time.duration.DurationFormatException;
import time.hour.Ora;


public class WatsonConversation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1627468397108580458L;

	// Flag per attivate/disattivare modalitÃ  debug
	static final boolean DEBUG = true;

	static final String QUERY_SVEGLIA_ALL = "QUERY SVEGLIA";

	static final String QUERY_SVEGLIA_VALUE = "QUERY SVEGLIA VALUE";

	static final String QUERY_TIMER_ALL = "QUERY TIMER";

	static final String tag_ora = "0R17";

	static final String tag_durata = "D1R17T17";

	protected JSONObject context;

	private URL end_point;

	private JSONObject credential;

	private int last_timer;
	
	public WatsonConversation(){}
	/**
	 * Costruttore Watson Conversation, definisce una particolare istanza legata
	 * a un particolare workspace
	 * 
	 * @param credential
	 *            JSONObject ottenuto da bluemix N.B. aggiungere /v1 al url
	 * @param workspaceID
	 *            ottenuto da bluemix, identifica la particolare conversazione
	 * @param version
	 *            di Conversation da utilizzare
	 * @throws MalformedURLException
	 *             Lanciato a causa di url contenuto in credential non corretto
	 * @throws JSONException
	 *             Lanciato a causa di JSON credential non conforme
	 */
	public WatsonConversation(JSONObject credential, String workspaceID, String version)
			throws MalformedURLException, JSONException {
		end_point = new URL(credential.getString("url") + "/workspaces/" + workspaceID + "/message?version=" + version);
		this.credential = credential;
		Ora.init();
	}

	/**
	 * Metodo che costruisce una connessione autenticata al particolare servizio
	 * di Conversation
	 * 
	 * @return
	 * @throws JSONException
	 *             Lanciato a causa di JSON credential non conforme
	 * @throws IOException
	 *             Lanciato a causa di problemi in creazione/utilizzo della
	 *             connessione
	 */
	private HttpURLConnection init_Connection() throws JSONException, IOException {

		HttpURLConnection connection = (HttpURLConnection) end_point.openConnection();

		connection.setDoOutput(true);

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

		// Autenticazione
		String userPassword = credential.getString("username") + ":" + credential.getString("password");

		if (DEBUG)
			System.out.println(userPassword);

		@SuppressWarnings("restriction")
		String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		// Fine Autenticazione

		return connection;
	}

	/**
	 * Metodo che invia messaggio vuoto per ottenere messaggio contenuto in nodo
	 * di start_conversation
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public String first_message() throws JSONException, IOException {
		String result = "";
		result = sendMessage("");
		return result;
	}

	/**
	 * Metodo per l'invio di un messaggio di testo a watson
	 * 
	 * @param input_text
	 *            messaggio da inviare
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public String sendMessage(String input_text) throws JSONException, IOException {
		String result = null;
		// costruzione JSONObject
		JSONObject input = new JSONObject();
		JSONObject text = new JSONObject();
		text.accumulate("text", input_text);
		input.accumulate("input", text);

		// Instance Ã¨ una classe compsta da un boolean una parte del input
		// (term) e un value se viene trovata un istanza di oranel testo
		// Instance viene popolato da un boolean a true term parte del testo che
		// rappresenta l'ora e value che Ã¨ il valore nominale del ora
		Instance ora = findTimeSveglia(input);

		// se il testo contiene l'ora viene aggiunta l'entitÃ  ora 000_ORA e
		// salvato in una var di contesto il valore del ora della sveglia
		if (ora.contains()) {
			updateValueSveglia(ora.value());
			input = addOraTAG(input_text,tag_ora);
		}

		// Riconsidero l'inputo persente nel JSON eventualmente modificato dal
		// ora
		input_text = input.getJSONObject("input").getString("text");

		// cerco una durata al interno del testo di input, se Ã¨ contenuta
		// verrÃ  salvata in una variabile locale chiamata last_timer
		// aggiorno il contesto e aggiungo l'entitÃ  durata 000_DURATA
		if (findTimeTimer(input_text)) {
			updateValueTimer(last_timer);
			input = addDurataTAG(input_text,tag_durata);
		}

		// aggiungo al input il contesto aggiornato dai due metodi
		// updateValueSveglia e updateValueTimer
		input.accumulate("context", context);

		if (DEBUG)
			System.out.println("INPUT: " + input);

		// inizializzo la connessione con il server
		HttpURLConnection connection = init_Connection();

		String message = input.toString();
		// Invio messaggio su connessione
		OutputStream os = (OutputStream) connection.getOutputStream();
		os.write(message.getBytes());
		os.flush();
		// Fine Invio messaggio

		// Lettura messaggio di risposta da connessione N.B. cambio Charset a
		// UTF-8
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream()), "UTF-8"));
		String serverOutput = br.readLine();
		// Fine lettura messaggio di risposta

		// Chiusura connessione
		connection.disconnect();

		JSONObject response = new JSONObject(serverOutput);

		if (DEBUG)
			System.out.println(response);

		// Conservo il contesto aggiornato
		context = response.getJSONObject("context");

		if (DEBUG){
			System.out.println("ID: "+context.getString("conversation_id"));
			System.out.println("CONTEXT: " + context);
		}
		// Estrazione del Output
		JSONObject output = response.getJSONObject("output");
		JSONArray text_out = output.getJSONArray("text");

		if (text_out.length() > 0) {
			result = text_out.getString(text_out.length() - 1);

			if (result.equals(QUERY_SVEGLIA_ALL)) {
				JSONArray sveglie = context.getJSONArray("sveglie");
				if (sveglie.length() > 0) {
					result = "Le sveglie impostate sono: ";
					for (int i = 0; i < sveglie.length(); i++) {
						result += " " + (i + 1) + ") " + sveglie.get(i);
					}
				} else {
					result = "Non ci sono sveglie impostate.";
				}
			}

			if (result.equals(QUERY_SVEGLIA_VALUE)) {
				String ora_search = context.getString("valueSveglia");
				JSONArray sveglie = context.getJSONArray("sveglie");
				if (sveglie.length() > 0) {
					boolean find = false;
					for (int i = 0; i < sveglie.length(); i++) {
						if (sveglie.getString(i).equals(ora_search)) {
							result = "Hai impostato una sveglia per le " + ora_search;
							find = true;
							break;
						}
					}
					if (!find) {
						result = "Non hai impostato una sveglia per le " + ora_search;
					}
				} else {
					result = "Non Ã¨ impostata nessuna sveglia.";
				}
			}

			if (result.equals(QUERY_TIMER_ALL)) {
				JSONArray timer = context.getJSONArray("timer");
				if (timer.length() > 0) {
					result = "I timer impostati sono: ";
					for (int i = 0; i < timer.length(); i++) {
						result += " " + (i + 1) + ") " + timer.get(i);
					}
				} else {
					result = "Non ci sono timer impostati.";
				}
			}
		}

		if (DEBUG) {
			JSONArray log_out = output.getJSONArray("log_messages");
			for (int i = 0; i < log_out.length(); i++) {
				result += "[log" + (i + 1) + "]" + log_out.get(i);
			}
		}
		return result;
	}

	// Modificare, creare oggetto in context

	private void updateValueSveglia(String value) {
		context.put("valueSveglia", value);
	}

	private void updateValueTimer(int value) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		if (DEBUG)
			System.out.println("Timer parte da:" + dateFormat.format(date));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, value);
		String time = dateFormat.format(cal.getTime());
		if (DEBUG)
			System.out.println("Timer suona a:" + time);
		context.put("valueTimer", time);
	}

	private JSONObject addDurataTAG(String input_text,String tag) {
		JSONObject input = new JSONObject();
		JSONObject text = new JSONObject();
		text.accumulate("text", input_text + " "+tag);
		input.accumulate("input", text);
		return input;
	}

	private JSONObject addOraTAG(String input_text,String tag) {
		JSONObject input = new JSONObject();
		JSONObject text = new JSONObject();
		text.accumulate("text", input_text + " "+tag);
		input.accumulate("input", text);
		return input;
	}

	private Instance findTimeSveglia(JSONObject input) {
		String input_text = input.getJSONObject("input").getString("text");
		input_text = input_text.toLowerCase();
		return Ora.contains(input_text);
	}

	private boolean findTimeTimer(String input_text) {
		boolean result = false;
		// input_text=input_text.toLowerCase();
		try {
			int second = Durata.decode(input_text);
			if (second != -1) {
				result = true;
				last_timer = second;
			} else if (DEBUG)
				System.out.println("risultato -1 nel parsing della durata!!");

		} catch (DurationFormatException e) {
			if (DEBUG)
				System.out.println("Errore nel parsing della durata!!");
		}
		return result;
	}
	
	public void write_Conversation(String path_dir,String message,String speaker) throws IOException{
		String conversation_id=this.context.getString("conversation_id");
		File con_file=new File(path_dir+"/"+conversation_id+".txt");
		if(!con_file.exists()){
			con_file.createNewFile();
			if(DEBUG)
				System.out.println("Creo: "+con_file.getAbsolutePath());
		}
		if(DEBUG)
			System.out.println("file conversation in: "+con_file.getAbsolutePath());
		
		FileWriter fw = new FileWriter(con_file.getAbsolutePath(), true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter writer = new PrintWriter(bw);
		//PrintWriter writer = new PrintWriter(con_file, "UTF-8");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String tag_time="["+dateFormat.format(date)+"]";
		if(DEBUG)
			System.out.println("Tag time in file: "+tag_time);
		writer.write(tag_time+" ["+speaker+"] "+message+" \n");
		writer.close();
	}
	
	public String conversation_id(){
		return this.context.getString("conversation_id");
	}

}
