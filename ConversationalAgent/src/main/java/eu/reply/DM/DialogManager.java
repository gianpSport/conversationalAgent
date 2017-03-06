package eu.reply.DM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.reply.Config.Config;
import eu.reply.DB.DB;
import eu.reply.NLG.NLG;
import eu.reply.PM.Token;
import eu.reply.PM.TranslateToken;
import eu.reply.SN.SemanticNet;
import eu.reply.Utils.JSON_utils;

public class DialogManager {

	private static final boolean DEBUG = Config.getDialogManagerDebug();
	private static final boolean DEEP_DEBUG = Config.getDialogManagerDeepDebug();

	SemanticNet net;
	String last_input;
	List<Token> phrase;

	Map<String, Boolean> classCompleteness;
	List<String> hierarchy;

	// String url;
	TranslateToken translater;
	float threshold;

	Map<String, String> workMemory;
	Map<String, List<String>> discourseMemory;
	String garbageToken;
	List<String> resultToken;

	private Scanner s;

	public DialogManager(SemanticNet net, float t, JSONObject read) {
		threshold = t;
		// this.url = url;
		// net = new SemanticNet(url);
		this.net = net;
		translater = new TranslateToken(read, t);
		workMemory = new HashMap<>();
		discourseMemory = new HashMap<>();
		hierarchy = net.getHierarchy();
		if (DEBUG) {
			for (int i = 0; i < hierarchy.size(); i++) {
				System.out.println(i + " " + hierarchy.get(i));
			}
		}
		classCompleteness = net.getClassCompleteness();
		DB.init();
	}

	public String realMessage(String input) {
		input = input.toLowerCase();
		String output = "";
		last_input = input;
		phrase = translater.parse(input);
		String semanticMessage = "";
		String relMessage = "";
		resultToken = new ArrayList<>();
		for (Token token : phrase) {
			String message = token.getResult();
			semanticMessage += message + "\n";
			message = net.enrich(message);
			token.setResult(message);
			relMessage += message + "\n";
		}

		if (DEBUG) {
			System.out.println("---------SEMANTIC REPRESENTATION----------");
			System.out.println(semanticMessage);
		}
		if (DEBUG) {
			System.out.println("-----SEMANTIC REPRESENTATION enrich-----");
			System.out.println(relMessage);
		}
		reasoning(phrase);
		if (DEBUG) {
			System.out.println("-------------MESSAGE RESPONSE--------------");
		}
		NLG nlg = new NLG(net);
		List<JSONObject> query_res = DB.query(resultToken, net);
		if (DEBUG) {
			for (JSONObject res_origin : query_res) {
				JSONObject res = new JSONObject(res_origin.toString());
				net.cleanEnrich(res.getJSONObject("query"));
				System.out.println(res.toString(4));
			}
		}
		output = nlg.getComposedMessage(query_res);
		if (DEEP_DEBUG)
			printDiscourseMemory();
		return output;
	}

	public String message(String input) {
		input = input.toLowerCase();
		String output = "";
		last_input = input;
		phrase = translater.parse(input);
		String semanticMessage = "";
		String relMessage = "";
		resultToken = new ArrayList<>();
		for (Token token : phrase) {
			String message = token.getResult();
			semanticMessage += message + "\n";
			message = net.enrich(message);
			token.setResult(message);
			relMessage += message + "\n";
		}

		if (DEBUG) {
			System.out.println("---------SEMANTIC REPRESENTATION----------");
			System.out.println(semanticMessage);
		}
		if (DEBUG) {
			System.out.println("-----SEMANTIC REPRESENTATION enrich-----");
			System.out.println(relMessage);
		}
		reasoning(phrase);
		if (DEBUG) {
			System.out.println("-------------MESSAGE RESPONSE--------------");
		}
		List<JSONObject> query_res = DB.query(resultToken, net);
		for (JSONObject res : query_res) {
			net.cleanEnrich(res.getJSONObject("query"));
			if (DEBUG) {
				System.out.println(res.toString(4));
			}
			output += res.toString();
		}
		if (DEEP_DEBUG)
			printDiscourseMemory();
		return output;
	}

	public String compose(String input) {
		input = input.toLowerCase();
		String output = "";
		last_input = input;
		phrase = translater.parse(input);
		String semanticMessage = "";
		String relMessage = "";
		resultToken = new ArrayList<>();
		for (Token token : phrase) {
			String message = token.getResult();
			semanticMessage += message + "\n";
			message = net.enrich(message);
			token.setResult(message);
			relMessage += message + "\n";
		}

		if (DEBUG) {
			System.out.println("---------SEMANTIC REPRESENTATION----------");
			System.out.println(semanticMessage);
		}
		if (DEBUG) {
			System.out.println("-----SEMANTIC REPRESENTATION enrich-----");
			System.out.println(relMessage);
		}
		reasoning(phrase);
		if (DEBUG) {
			System.out.println("-------------AFTER REASONING--------------");
		}
		for (String s : resultToken) {
			JSONObject obj = net.cleanEnrich(new JSONObject(s));
			if (DEBUG) {
				System.out.println(obj.toString(4));
			}
			output += obj.toString();
		}
		if (DEEP_DEBUG)
			printDiscourseMemory();
		return output;
	}

	/**
	 * Metodo che si occupa di effetuare il ragionamento sul ultimo messaggio
	 * considerando anche la conversazione
	 * 
	 * @param list
	 * @return
	 */
	private void reasoning(List<Token> list) {
		// Inizializzo la workMemory
		workMemory = new HashMap<>();
		boolean last_in = false;
		boolean last_out = false;

		// per tutti i token del ultimo messaggio
		for (Token token : list) {

			if (DEBUG) {
				System.out.println("TOKEN IN ESAME: " + token);
			}
			// ottengo il messaggio JSON
			String message = token.getResult();

			JSONObject json;
			if (JSON_utils.isJSONArray(message)) {
				// messaggio ambiguo
				JSONArray ambigous_message = JSON_utils.convertJSONArray(message);
				if (DEBUG) {
					System.out.println("Ambiguos Message:");
					System.out.println(ambigous_message.toString(4));
				}
				json = ambigous_message.getJSONObject(0);
			} else {
				// converto in JSON
				json = new JSONObject(message);
			}

			// Estraggo la category
			String category = json.getString("category");

			// Pulisco la categoria -> faccio salire tutti i valori più in basso
			// nella gerarchia
			// schiacciandoli in un unico livello
			clean(category);

			boolean rel_in = net.has_rel_in(json);
			boolean rel_out = net.has_rel_out(json);

			if (!(rel_in || rel_out) && !(last_in || last_out)) {
				if (DEBUG) {
					System.out.println("esporto in result il token poichè non è utile nella memoria");
				}
				resultToken.add(message);
			} else
			// Se nella memoria di lavoro è presente qualcosa
			if (workMemory.containsKey(category)) {

				// Prendo ciò che è contenuto nella memoria di lavoro
				String val_category = workMemory.get(category);

				if (DEBUG) {
					System.out.println("categoria presente workMemory, verifico compatibilità.");
					System.out.println(val_category);
				}

				// se questo è ambiguo provo a risolvere l'ambiguità basandomi
				// solo
				// sulla conversazione
				if (JSON_utils.isJSONArray(val_category)) {
					if (DEBUG) {
						System.out.println("elemento in memoria ambiguo, provo a risolvere l'ambiguità.");
					}
					val_category = resolve_ambiguity(val_category, false);
				}

				// Converto in JSONArray
				JSONArray array = JSON_utils.convertJSONArray(val_category);
				String element = null;

				JSONArray ambiguos_message = JSON_utils.convertJSONArray(message);
				// cerco di risolvere l'ambiguità con il nuovo messaggio
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String amb_name = obj.getString("name");

					for (int j = 0; j < ambiguos_message.length(); j++) {
						JSONObject single_message = ambiguos_message.getJSONObject(j);
						String name = single_message.getString("name");
						// se esiste un elemento nel set di ambiguità con lo
						// stesso
						// nome
						// del messaggio allora esiste una possibilità
						if (amb_name.equals(name) && !net.isTerminal(obj)) {
							// se l'ambiugità è stata inferita allora è
							// possibile
							// risolvere
							// if (!obj.has("token")) {
							element = obj.toString();
							break;
						}
					}
				}

				// se il token in esame risolve l'ambiguità
				if (element != null) {
					if (DEBUG) {
						System.out
								.println("elemento in memoria e quello attuale sono compatibili sostituisco in memoria "
										+ category + ":" + element);
					}
					// inserisco l'elemento disambiguato nella memoria
					workMemory.put(category, element);
				} else {
					if (DEBUG) {
						System.out.println("elemento in memoria non compatibile con l'attuale. elevo " + category);
					}
					// ambiguty non risolta
					elevate_category(category);
					if (DEBUG) {
						System.out.println("inserisco in " + category + " il token " + message);
					}
					// lascia lo spazio per il nuovo messaggio
					workMemory.put(category, message);
				}
			} else {
				// non presente
				if (DEBUG) {
					System.out
							.println("nessun conflitto in memoria, inserisco in " + category + " il token " + message);
				}
				workMemory.put(category, message);

			}
			// aggiungo il token al discorso
			if (!JSON_utils.isJSONArray(message)) {
				addDiscourseMemory(message);
			}

			last_in = rel_in;
			last_out = rel_out;
		}

		// compongo il contenuto della memoria di lavoro
		compose();
	}

	/**
	 * cerca di risolvere l'ambiguità analizzando il livello superiore e
	 * componendo con un nodo interno
	 * 
	 * @param val_category
	 * @return
	 */
	private boolean completeWithSuperior(String val_category) {
		// cercare di dare un ordine alle proprietà che vengono ispezionante
		// mediante la memoria di discorso
		JSONArray array = JSON_utils.convertJSONArray(val_category);
		boolean find = false;
		for (int i = 0; i < array.length() && !find; i++) {
			// estraggo l'oggetto ambiguo
			JSONObject obj = array.getJSONObject(i);
			String category = obj.getString("category");
			String name = obj.getString("name");

			if (net.has_rel_in(obj)) {
				// estraggo le relazioni di input
				JSONArray rel_in = net.rel_in(obj);
				// per ogni relazione di input
				for (int j = 0; j < rel_in.length() && !find; j++) {
					// estraggo la relazione di input
					JSONObject obj_in = rel_in.getJSONObject(j);
					String category_in = obj_in.getString("category");
					String name_in = obj_in.getString("name");
					// se in memoria ho la categoria della relazione di input
					if (workMemory.containsKey(category_in)) {
						// estraggo l'elemento dalla memoria
						String element_in = workMemory.get(category_in);
						if (JSON_utils.isJSONArray(element_in)) {
							element_in = resolve_ambiguity(element_in, false);
							workMemory.put(category_in, element_in);
						}

						if (!JSON_utils.isJSONArray(element_in)) {
							// se l'elemento non è ambiguo
							JSONObject real_in = new JSONObject(element_in);
							// se il nome del oggetto non ambiguo è uguale a
							// quello della relazione
							if (real_in.getString("name").equals(name_in)) {
								// se l'oggetto del livello superiore contiene
								// la categoria
								if (real_in.has(category)) {
									String category_val = real_in.get(category).toString();
									JSONArray category_array = JSON_utils.convertJSONArray(category_val);

									// per ogni elemento della categoria che
									// vogliamo disambiguare contenuta nel
									// oggetto di livello superiore
									for (int k = 0; k < category_array.length() && !find; k++) {
										JSONObject potential = category_array.getJSONObject(k);
										if (potential.getString("name").equals(name)) {
											// se il nome del elemento contenuto
											// è uguale a quello che vogliamo
											// inserire
											// JSONObject merge =
											// compatibile(potential, obj);
											JSONObject merge = compatibile(potential, obj);
											if (merge != null) {
												// category_array.remove(k);
												category_array.put(k, merge);
												find = true;
											}
										}
									}
									if (find) {
										real_in.remove(category);
										for (int k = 0; k < category_array.length(); k++) {
											real_in.accumulate(category, category_array.get(k));
										}
										workMemory.put(category_in, real_in.toString());
									}
								}
							}
						}
					}
				}
			}
		}
		return find;
	}

	/**
	 * Metodo che cerca di liberare una categoria
	 * 
	 * @param category
	 */
	private void elevate_category(String category) {
		// se la categoria è occupata
		if (workMemory.containsKey(category)) {
			if (DEBUG) {
				System.out.println("ho qualcosa in " + category);
			}
			// estrai il valore
			String val = workMemory.get(category);
			// se c'è ambiguità non risolta cerca di risolverla
			// eventualmente chiedendo
			if (JSON_utils.isJSONArray(val)) {
				if (DEBUG) {
					System.out.println("cateogria ambigua, provo a risolvere con livello superiore.");
				}
				if (!completeWithSuperior(val)) {
					if (DEBUG) {
						System.out.println(
								"Ambiguità non risolta. provo a risolvere con discourseMemory se non riesco chiedo al utente.");
					}
					val = resolve_ambiguity(val, true);
				} else {
					if (DEBUG) {
						System.out.println("ambiguità risolta, " + category + " elevata.");
					}
					workMemory.remove(category);
					return;
				}
			}

			// ricostruisco il json
			JSONObject obj = new JSONObject(val);
			// prendo il nome
			String name = obj.getString("name");
			if (DEBUG) {
				System.out.println("esamino relazioni di input.");
			}
			// se l'oggetto ammette relazioni di input
			// esistono istanze che lo prendono in input
			if (net.has_rel_in(obj)) {

				// estraggo le relazioni di input arrichendole
				JSONArray in_array = net.rel_in(obj);
				if (DEBUG) {
					System.out.println("esistono relazioni di input.");
				}
				// Prevede che le relazioni di input siano uguali, tutti con
				// la stessa classe
				String in_category = in_array.getJSONObject(0).getString("category");
				// ASSUNZIONE FORTE

				// flag ritrovamento oggetto che può accettare quello che sto
				// elevando
				boolean find = false;
				// se trovo un oggetto che può prenderlo in input ma risulta
				// incompatibile
				// con l'oggetto che sto elevando, es. contiene già un istanza
				// di uno simile
				boolean hard_break = false;
				// vettore che conterrà il livello successivo, quello in cui
				// stiamo elevando l'ogetto
				JSONArray next_level = new JSONArray();

				// se in memoria di lavoro è prensente la categoria in cui
				// vogliamo elevare l'oggetto
				if (DEBUG) {
					System.out.println("verifico che in workMemory ci sia la categoria di livello superiore.");
				}
				if (workMemory.containsKey(in_category)) {
					if (DEBUG) {
						System.out.println("esiste la categoria.");
					}
					// ottengo il valore della categoria superiore
					String sup_val = workMemory.get(in_category);
					// ricostruisco il vettore del livello superiore
					JSONArray sup_array = JSON_utils.convertJSONArray(sup_val);

					if (DEBUG) {
						System.out.println("analizzo il livello superiore.");
					}
					// verifico tutte le relazioni di input
					for (int j = 0; j < in_array.length() && !hard_break; j++) {

						// ottengo una relazione di input
						JSONObject in_obj = in_array.getJSONObject(j);

						// prendo il nome della relazione in esame
						String in_name = in_obj.getString("name");

						// verifico il livello superiore
						for (int k = 0; k < sup_array.length() && !hard_break; k++) {

							// prendo un elemento del livello superiore
							JSONObject sup_obj = sup_array.getJSONObject(k);

							// ottengo il nome del oggetto del livello superiore
							String sup_name = sup_obj.getString("name");

							// se il nome del oggetto superiore è uguale a
							// quello della
							// relazione di input che sto esaminando
							if (in_name.equals(sup_name)) {
								if (DEBUG) {
									System.out.println(
											"esiste un oggetto del livello superiore compatibile con la categoria che sto elevando. "
													+ sup_name);
								}
								// se contine già la categoria in cui dovremmo
								// mettere
								// l'oggetto che stiamo elevando
								if (DEBUG) {
									System.out.println(
											"analizzo l'oggetto del livello superiore per individuare eventuali incompatibilità.");
								}
								boolean onlyOne = net.onlyOne(sup_obj);
								if (sup_obj.has(category)) {
									if (DEBUG) {
										System.out.println("l'oggetto contiene la cateogria che vogliamo elevare.");
									}
									// flag che indica se contiene già un
									// oggetto uguale
									boolean has = false;
									// prendo il valore della categoria in cui
									// dovremo mettere
									// l'oggetto che stiamo elevando del oggetto
									// del livello superiore
									// in esame
									String val_category = sup_obj.get(category).toString();

									// ricostruisco l'array della categoria
									// contenuta nel oggetto di livello
									// superiore che stiamo considerando
									JSONArray array_category = JSON_utils.convertJSONArray(val_category);

									// verifico su tutti gli elementi del
									// livello superiore
									for (int i = 0; i < array_category.length() && !has; i++) {

										// ottengo un oggetto contenuto nella
										// categoria del oggetto di livello
										// superiore in esame
										JSONObject obj_category = array_category.getJSONObject(i);

										// ottengo il suo nome
										String name_category = obj_category.getString("name");

										boolean terminal = net.isTerminal(obj_category);
										// se il nome del oggetto contenuto nel
										// campo categoria del oggetto di di
										// livello
										// superiore in esame ha stesso nome del
										// oggetto che si sta cercando di
										// elevare
										if (name_category.equals(name) && !terminal) {
											if (DEBUG) {
												System.out.println(
														"l'oggetto di livello superiore contiene un omonimo dell'oggetto della categoria che vogliamo elevare. che non è terminale");
											}
											// lo abbiamo, quindi setto il flag
											// a true
											has = true;
											// provo a verificare la
											// compatibilità
											// il metodo mi restituisce null se
											// non sono compatibili
											// altrimenti l'unione
											if (DEBUG) {
												System.out.println("provo a unire i due oggetti: " + name);
											}
											JSONObject merge = compatibile(obj_category, obj);

											// se sono compatibili
											if (merge != null) {
												if (DEBUG) {
													System.out.println("sono riuscito ad unire i due oggetti.");
												}
												// inserisco l'unione
												array_category.put(i, merge);
											} else {
												if (DEBUG) {
													System.out
															.println("non è possibile unire i due oggetti. HARD BREAK");
												}
												// hard_break a true, devo
												// elevare il livello superiore
												// per fare spazio
												hard_break = true;
											}
											break;
										} else if (terminal) {
											if (DEBUG) {
												System.out.println(
														"l'oggetto che stiamo elevando è terminale quindi bisogna elevare la categoria. HARD BREAK");
											}
											hard_break = true;
											has = true;
											break;
										}
									}

									// se l'oggetto del livello superiore
									// compatibile con l'oggetto che vogliamo
									// elevare
									// non ha nel campo categoria un oggetto con
									// stesso nome di quello che stiamo elevando
									if (!has && !onlyOne) {
										if (DEBUG) {
											System.out.println(
													"a livello superiore è presente la categoria ma non un istanza del oggetto che vogliamo elevare. lo aggiungo alla categoria "
															+ category + " del oggetto " + sup_name);
										}
										// inserisco l'oggetto che stiamo
										// elevando nell'array del oggetto di
										// livello supriore
										// compatibile
										array_category.put(obj);
									} else if (!has && onlyOne) {
										if (DEBUG) {
											System.out.println(
													"a livello superiore è presente la categoria ma non un istanza del oggetto che vogliamo elevare. non posso aggiungerlo alla categoria "
															+ category + " del oggetto " + sup_name
															+ " perchè l'oggetto può contenere una soloa instanza di "
															+ category);
										}

										hard_break = true;
									}

									// se non è scattato un hard_break quindi
									// non bisogna liberare il livello superiore
									if (!hard_break) {
										// posso scrivere il vettore degli
										// oggetti appartenenti alla categoria
										// del oggetto che vogliamo
										// elevare al interno del oggetto di
										// livello superiore compatibile
										if (array_category.length() == 1) {
											// se è un solo oggetto scrivo solo
											// quello
											JSONObject new_obj = array_category.getJSONObject(0);
											sup_obj.remove(category);
											sup_obj.accumulate(category, new_obj);
										} else {
											// altrimenti devo scrivere tutti
											// gli oggetti contenuti
											sup_obj.remove(category);
											for (int i = 0; i < array_category.length(); i++) {
												JSONObject new_obj = array_category.getJSONObject(i);
												sup_obj.accumulate(category, new_obj);
											}
											if (DEBUG) {
												System.out.println("aggiorno l'oggetto di livello superiore.");
											}
										}

										// se invece è scattato un hard_break
									} else {
										// allora devo elevare il livello
										// superiore
										if (DEBUG) {
											System.out.println(
													"elevo la categoria " + in_category + " a causa del HARD BREAK");
										}
										elevate_category(in_category);
										// e ripovare con il livello che ha
										// fatto scattare l'hard_break
										if (DEBUG) {
											System.out.println("riprovo ad elevare la categoria " + category);
										}
										elevate_category(category);
									}

									// se l'oggetto del livello superiore
									// compatibile con l'oggetto che vogliamo
									// elevare
									// non ha un campo category
								} else {

									if (DEBUG) {
										System.out.println("l'oggetto di livello superiore non contiene " + category
												+ " quindi l'aggiungo");
									}
									// l'oggetto che stiamo elevando è il primo
									// e lo possiamo inserire senza problemi
									sup_obj.accumulate(category, obj);
								}
								// inserirsco l'oggetto del livello superiore
								// nel array del prossimo livello
								if (DEBUG) {
									System.out.println("aggiorno il livello superiore.");
								}
								next_level.put(sup_obj);
								// settando a true il flag che indica che
								// abbiamo trovato un oggetto del livello
								// superiore compatibile
								find = true;

								// FINE oggetto superiore compatibile con
								// oggetto che vogliamo elevare
							}
							// FINE ciclo su oggetti del livello superiore
						}
						// FINE ciclo sulle relazioni di input con l'oggetto da
						// elevare
					}

					// FINE sezione workmemory contiene la categoria superiore
					// al oggetto che si vuole elevare
				}

				// inizializzo la stringa che conterrà gli elementi del livello
				// superiore
				String add = null;

				// se abbiamo trovato un oggetto di livello superiore
				// compatibile
				if (find) {
					// aggiungiamo tutto il livello superiore che abbiamo
					// costruito
					add = next_level.toString();
					// se contiene solo un elemento
					if (next_level.length() == 1) {
						// aggiungo solo l'oggetto di interesse
						add = next_level.getJSONObject(0).toString();
					}
					// se non abbiamo trovato un oggetto di livello superiore e
					// non è avvenuto un hard_break
				} else if (!hard_break) {
					if (DEBUG) {
						System.out.println(
								"non ho trovato niente di interessante a livello superiore, lo aggiorno aggiungendo ambiguità derivante da "
										+ category);
					}
					// eleviamo tutta la categoria superiore per fare spazio
					elevate_category(in_category);
					// inizializzo il vettore di ambiguità per il nuovo livello
					// superiore
					JSONArray add_ambiguity = new JSONArray();

					// aggiungo un oggetto per ogni relazione di input al
					// oggetto che vogliamo elevare
					for (int j = 0; j < in_array.length(); j++) {
						// ottengo la relazione
						JSONObject in_obj = in_array.getJSONObject(j);
						// inserisco l'oggetto che stiamo elevando
						in_obj.accumulate(category, obj);
						// metto il nuovo oggetto nel array del livello
						// superiore
						add_ambiguity.put(in_obj);
					}
					add = add_ambiguity.toString();

				}

				if (!hard_break) {
					if (DEBUG) {
						System.out.println("aggiorno le due memorie.");
					}
					workMemory.put(in_category, add);
					// inserisco l'oggetto che stiamo elevando nella memoria di
					// discorso
					addDiscourseMemory(val);
					// pulisco il livello category
					workMemory.remove(category);
				}
				// se non ha relazioni di input
			} else {
				if (DEBUG) {
					System.out
							.println("non esistono relazioni di input. la categoria è pronta ad uscire dalla memoria?");
				}
				// è pronto per uscire
				exit(obj);
				// lo aggiungo alla memoria di discorso
				addDiscourseMemory(obj.toString());
				// pulisco il livello category
				workMemory.remove(category);
			}
			if (DEEP_DEBUG) {
				printDiscourseMemory();
				printWorkMemory();
			}
		}

	}

	private void exit(JSONObject obj) {
		JSONObject complete = complete(obj);
		resultToken.add(complete.toString());
	}

	private JSONObject complete(JSONObject obj) {
		String category = obj.getString("category");
		if (classCompleteness.get(category)) {

			JSONArray rel_out = net.rel_out(obj);

			boolean complete = false;

			for (int i = 0; i < rel_out.length() && !complete; i++) {
				JSONObject out = rel_out.getJSONObject(i);

				String out_category = out.getString("category");

				if (obj.has(out_category)) {

					String str_val_category = obj.get(out_category).toString();

					JSONArray array_value = JSON_utils.convertJSONArray(str_val_category);

					/////// Prova
					obj.remove(out_category);

					for (int j = 0; j < array_value.length(); j++) {

						JSONObject value = array_value.getJSONObject(j);
						value = complete(value);
						obj.accumulate(out_category, value);
					}

					complete = true;
				}
			}

			if (!complete) {
				String array = rel_out.toString();
				String value = resolve_ambiguity(array, true);
				JSONObject child = new JSONObject(value);
				child = complete(child);
				obj.put(child.getString("category"), child);
			}
		}
		return obj;
	}

	private JSONObject compatibile(JSONObject obj_category, JSONObject obj) {
		JSONObject result = null;
		if (net.has_rel_out(obj_category)) {

			JSONArray rel_out = net.rel_out(obj_category);

			for (int i = 0; i < rel_out.length(); i++) {

				JSONObject out = rel_out.getJSONObject(i);

				String category_out = out.getString("category");
				String name_out = out.getString("name");

				boolean terminal = net.isTerminal(out);

				if (obj.has(category_out)) {
					String val_out = obj.get(category_out).toString();

					JSONArray array_out = JSON_utils.convertJSONArray(val_out);

					for (int j = 0; j < array_out.length(); j++) {

						JSONObject obj_out = array_out.getJSONObject(j);

						if (obj_out.getString("name").equals(name_out) || terminal) {

							if (obj_category.has(category_out)) {

								String val_category_out = obj_category.get(category_out).toString();

								JSONArray array_cateogry_out = JSON_utils.convertJSONArray(val_category_out);

								boolean find = false;

								for (int k = 0; k < array_cateogry_out.length(); k++) {

									JSONObject obj_category_out = array_cateogry_out.getJSONObject(k);

									if (obj_category_out.getString("name").equals(name_out) && !terminal) {

										JSONObject comp = compatibile(obj_category_out, obj_out);

										find = true;

										if (comp == null) {
											return null;
										} else {
											obj_category.remove(category_out);
											obj_category.accumulate(category_out, comp);
										}

									} else if (/*
												 * obj_category_out.getString(
												 * "name").equals(name_out) &&
												 */ terminal) {
										find = true;
										return null;
									}
								}

								if (!find) {
									obj_category.accumulate(category_out, obj_out);
									break;
								}
							} else {
								obj_category.accumulate(category_out, obj_out);
							}
						}
					}
				}
			}
			result = obj_category;
		}
		return result;
	}

	private String resolve_ambiguity(String val, boolean ask) {
		String result = null;
		JSONArray array = new JSONArray(val);
		boolean find = false;

		for (int i = 0; i < array.length() && !find; i++) {
			JSONObject obj = array.getJSONObject(i);
			String category = obj.getString("category");
			String name = obj.getString("name");

			if (obj.has("rel_in") && array.length() > 1) {
				String val_in = obj.get("rel_in").toString();
				if (val_in.charAt(0) != '[') {
					val_in = '[' + val_in + ']';
				}
				JSONArray rel_in = new JSONArray(val_in);
				boolean remove = true;

				for (int k = 0; k < rel_in.length(); k++) {
					JSONObject in = rel_in.getJSONObject(k);
					String req_category = in.getString("category");
					String req_name = in.getString("name");
					if (workMemory.containsKey(req_category)) {
						String val_req = workMemory.get(req_category);
						if (val_req.charAt(0) != '[') {
							JSONObject obj_req = new JSONObject(val_req);
							String obj_name = obj_req.getString("name");
							if (obj_name.equals(req_name)) {
								remove = false;
								break;
							}
						}
					} else {
						remove = false;
					}
				}

				if (remove) {
					array.remove(i--);
					continue;
				}
			}

			if (discourseMemory.containsKey(category)) {
				// guardo il discorso
				List<String> memory = discourseMemory.get(category);
				for (String element : memory) {
					if (element.charAt(0) == '[') {
						System.err.println("ambiguit� nella moemoria elevate category method");
					} else {
						JSONObject memory_obj = new JSONObject(element);
						String memory_name = memory_obj.getString("name");
						if (name.equals(memory_name)) {
							String merge = merge(memory_obj, obj);
							result = merge;
							find = true;
							break;
						}
					}
				}
			}
		}

		if (!find) {
			if (array.length() == 1) {
				result = array.getJSONObject(0).toString();
			} else if (ask) {
				System.out.println("" + array.length());
				System.out.println(array.toString());
				result = ask(val);
			} else {
				result = array.toString();
				if (DEBUG) {
					System.out.println("AMBIGUITY not resolved");
					System.out.println(val);
				}
			}
		}
		return result;
	}

	private String ask(String val) {
		String result = null;
		JSONArray array_ambiguity = new JSONArray(val);
		boolean go = false;
		while (!go) {
			System.out.println("non so a cosa ti riferisci, scegli tra queste alternative:");
			for (int i = 0; i < array_ambiguity.length(); i++) {
				JSONObject obj = array_ambiguity.getJSONObject(i);
				String category = obj.getString("category");
				String name = obj.getString("name");
				System.out.println((i + 1) + ": " + category + "(" + name + ")");
			}
			s = new Scanner(System.in);
			System.out.print(">>");
			String line = s.nextLine();
			Integer choice = -1;
			try {
				choice = Integer.valueOf(line);
				int n = choice - 1;
				if (n >= 0 && n < array_ambiguity.length()) {
					String disambiguate = array_ambiguity.getJSONObject(n).toString();
					result = disambiguate;
					addDiscourseMemory(disambiguate);
					go = true;
				} else {
					throw new Exception();
				}
			} catch (Exception ex) {
				System.out.print("scegli un alternativa valida.");
			}
		}
		return result;
	}

	private String merge(JSONObject memory_obj, JSONObject obj) {
		String result = null;
		boolean onlyOne = net.onlyOne(obj);
		if (net.has_rel_out(obj)) {
			JSONArray array_out = net.rel_out(obj);
			for (int i = 0; i < array_out.length(); i++) {
				JSONObject obj_out = array_out.getJSONObject(i);
				obj_out = new JSONObject(net.enrich(obj_out.toString()));
				// indica se l'oggetto è un terminale, cioè un nodo foglia che
				// non può essere duplicato nella categoria
				// un solo valore, un solo numero, ecc.
				boolean terminal = !obj_out.has("rel_out");
				String category_out = obj_out.getString("category");
				String name_out = obj_out.getString("name");

				if (obj.has(category_out) && memory_obj.has(category_out) && !terminal && !onlyOne) {

					String obj_cat = obj.get(category_out).toString();
					String memo_cat = memory_obj.get(category_out).toString();

					boolean find = false;
					JSONArray array_cat = JSON_utils.convertJSONArray(obj_cat);
					JSONArray memo_array_cat = JSON_utils.convertJSONArray(memo_cat);

					for (int j = 0; j < array_cat.length() && !find; j++) {
						JSONObject val_cat = array_cat.getJSONObject(j);
						String val_name = val_cat.getString("name");

						if (val_name.equals(name_out)) {
							if (net.incomplete(val_cat)) {
								memo_array_cat = net.remove_incomplete(memo_array_cat);
							}
							boolean addElement = true;
							for (int k = 0; k < memo_array_cat.length(); k++) {
								JSONObject memo_val_cat = memo_array_cat.getJSONObject(k);
								String memo_val_name = memo_val_cat.getString("name");
								if (memo_val_name.equals(val_name)) {
									memo_array_cat.remove(k);
									memo_array_cat.put(val_cat);
									find = true;
									addElement = false;
									break;
								}
							}

							if (addElement) {
								memo_array_cat.put(val_cat);
							}
						}
					}

					Object value = null;
					if (memo_array_cat.length() == 1) {
						value = memo_array_cat.get(0);
					} else {
						value = memo_array_cat;
					}
					memory_obj.put(category_out, value);

					if (find) {
						break;
					}
				} else if (obj.has(category_out) && memory_obj.has(category_out) && (terminal || onlyOne)) {
					memory_obj.remove(category_out);
					memory_obj.accumulate(category_out, obj.get(category_out));
					break;

				} else if (obj.has(category_out)) {
					String obj_cat = obj.get(category_out).toString();

					JSONArray array_cat = JSON_utils.convertJSONArray(obj_cat);

					for (int j = 0; j < array_cat.length(); j++) {
						JSONObject val_cat = array_cat.getJSONObject(j);
						String val_name = val_cat.getString("name");

						if (val_name.equals(name_out)) {
							memory_obj.accumulate(category_out, val_cat);
							break;
						}
					}
				}
			}
			result = memory_obj.toString();
		}
		return result;
	}

	private void addDiscourseMemory(String message) {
		JSONObject json = new JSONObject(message);
		String category = json.getString("category");
		if (discourseMemory.containsKey(category) && false) {
			List<String> list = discourseMemory.get(category);
			if (list.contains(message)) {
				list.remove(message);
			}
			list.add(0, message);
		} else {
			List<String> list = new ArrayList<>();
			list.add(0, message);
			discourseMemory.put(category, list);
		}
	}

	private void compose() {
		// compose last element
		List<String> elementClean = new ArrayList<>();
		for (int i = hierarchy.size() - 1; i >= 0; i--) {
			String category = hierarchy.get(i);
			clean(category);
			if (workMemory.containsKey(category)) {
				elevate_category(category);
			}
			elementClean.add(0, category);
		}
		Set<String> keySet = new HashSet<>(workMemory.keySet());
		for (String key : keySet) {
			if (DEBUG) {
				System.out.println("element " + key + " in workMemory: " + workMemory.get(key));
			}
			if (!elementClean.contains(key)) {
				if (workMemory.containsKey(key)) {
					elevate_category(key);
				}
			}
		}
	}

	private void clean(String category) {
		if (hierarchy.contains(category)) {
			int index = hierarchy.indexOf(category);
			for (int i = hierarchy.size() - 1; i > index; i--) {
				String last = hierarchy.get(i);
				elevate_category(last);
			}
		}
	}

	private void printDiscourseMemory() {
		System.out.println("------------------------------------------------------------------");
		System.out.println("|                        DISCOURSE MEMORY                        |");
		System.out.println("------------------------------------------------------------------");
		for (String key : discourseMemory.keySet()) {
			List<String> entry = discourseMemory.get(key);
			System.out.println("----------------------KEY:" + key + "----------------------");
			for (String e : entry) {
				JSONObject o = new JSONObject(e);
				System.out.println(o.toString(1));
			}
			System.out.println("--------------------END KEY:" + key + "--------------------");
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println("|                      END DISCOURSE MEMORY                      |");
		System.out.println("------------------------------------------------------------------");
	}

	private void printWorkMemory() {
		System.out.println("------------------------------------------------------------------");
		System.out.println("|                           WORK MEMORY                          |");
		System.out.println("------------------------------------------------------------------");
		for (String key : workMemory.keySet()) {
			String entry = workMemory.get(key);
			System.out.println("----------------------KEY:" + key + "----------------------");
			if (JSON_utils.isJSONObject(entry)) {
				JSONObject o = new JSONObject(entry);
				System.out.println(o.toString(1));
			} else {
				JSONArray a = JSON_utils.convertJSONArray(entry);
				System.out.println(a.toString(1));
			}
			System.out.println("--------------------END KEY:" + key + "--------------------");
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println("|                        END WORK MEMORY                         |");
		System.out.println("------------------------------------------------------------------");

	}
	// METODI CHE PARLANO CON LA RETE SEMANTICA

}
