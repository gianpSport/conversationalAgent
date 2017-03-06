package eu.reply.SN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.reply.Utils.JSON_utils;

public class SemanticNet {

	private String dump = "{\"name\":\"dump\",\"category\":\"value\"}";
	OntModel model;
	Map<String, Resource> cache;
	Map<Resource, List<Resource>> cache_in;
	Map<Resource, List<Resource>> cache_out;
	Map<String, Boolean> classCompleteness;
	Map<String, List<String>> template;
	Map<String, List<String>> complete;
	Map<String, Boolean> onlyOne;
	List<String> hierarchy;

	public SemanticNet(String url) {
		this.model = Reader.readModel(url);
		cache = new HashMap<>();
		cache_in = new HashMap<>();
		cache_out = new HashMap<>();
		template = new HashMap<>();
		complete = new HashMap<>();
		onlyOne=new HashMap<>();
		hierarchy = relationClass();
		classCompleteness = classCompleteness();
		dump = enrich(dump);
	}

	public SemanticNet(OntModel model) {
		this.model = model;
		cache = new HashMap<>();
		cache_in = new HashMap<>();
		cache_out = new HashMap<>();
		template = new HashMap<>();
		complete = new HashMap<>();
		onlyOne=new HashMap<>();
		hierarchy = relationClass();
		classCompleteness = classCompleteness();
		dump = enrich(dump);
	}

	public OntModel getModel() {
		return model;
	}

	public void printModel() {
		model.write(System.out);
	}

	public JSONArray rel_out(JSONObject obj) {
		JSONArray result = null;
		if (has_rel_out(obj)) {
			String rel_out = obj.get("rel_out").toString();
			if (JSON_utils.isJSONArray(rel_out)) {
				result = enrichJSONArray(new JSONArray(rel_out));
			} else {
				result = enrichJSONArray(new JSONArray('[' + rel_out + ']'));
			}
		} else {
			result = new JSONArray();
		}
		return result;
	}

	public JSONArray rel_in(JSONObject obj) {
		JSONArray result = null;
		if (has_rel_in(obj)) {
			String rel_in = obj.get("rel_in").toString();
			if (JSON_utils.isJSONArray(rel_in)) {
				result = enrichJSONArray(new JSONArray(rel_in));
			} else {
				result = enrichJSONArray(new JSONArray('[' + rel_in + ']'));
			}
		} else {
			result = new JSONArray();
		}
		return result;
	}

	public boolean has_rel_out(JSONObject obj) {
		return (obj).has("rel_out");
	}

	public boolean has_rel_in(JSONObject obj) {
		return (obj).has("rel_in");
	}

	public boolean isTerminal(JSONObject obj) {
		return !has_rel_out(obj);
	}

	public JSONArray enrichJSONArray(JSONArray array) {
		for (int i = 0; i < array.length(); i++) {
			array.put(i, new JSONObject(enrich(array.get(i).toString())));
		}
		return array;
	}

	public JSONObject cleanEnrich(JSONObject obj) {
		if (obj.has("rel_in")) {
			obj.remove("rel_in");
		}

		if (obj.has("rel_out")) {
			String out = obj.get("rel_out").toString();
			if (out.charAt(0) != '[') {
				out = '[' + out + ']';
			}
			JSONArray rel_out = new JSONArray(out);
			obj.remove("rel_out");
			for (int i = 0; i < rel_out.length(); i++) {
				JSONObject out_obj = rel_out.getJSONObject(i);
				String category = out_obj.getString("category");
				if (obj.has(category)) {
					if (obj.get(category) instanceof JSONObject) {
						cleanEnrich(obj.getJSONObject(category));
					} else if (obj.get(category) instanceof JSONArray) {
						JSONArray array = obj.getJSONArray(category);
						for (int k = 0; k < array.length(); k++) {
							cleanEnrich(array.getJSONObject(k));
						}
					}
				}
			}
		}
		return obj;
	}

	public String enrich(String message) {
		if(JSON_utils.isJSONArray(message)){
			JSONArray ambiguos_message=JSON_utils.convertJSONArray(message);
			for(int i=0;i<ambiguos_message.length();i++){
				JSONObject part=ambiguos_message.getJSONObject(i);
				ambiguos_message.put(i, new JSONObject(enrich(part.toString())));
			}
			return ambiguos_message.toString();
		}else{
		JSONObject jsonMessage = new JSONObject(message);
		Resource res = getResource(jsonMessage);
		List<Resource> in_rel = in_Relation(res);
		List<Resource> out_rel = out_Relation(res);
		for (Resource in : in_rel) {
			String name = getName(in);
			String category = getCategory(in);
			JSONObject in_json = new JSONObject();
			in_json.accumulate("name", name);
			in_json.accumulate("category", category);
			jsonMessage.accumulate("rel_in", in_json);
		}
		for (Resource out : out_rel) {
			String name = getName(out);
			String category = getCategory(out);
			JSONObject out_json = new JSONObject();
			out_json.accumulate("name", name);
			out_json.accumulate("category", category);
			jsonMessage.accumulate("rel_out", out_json);
		}
		//jsonMessage=combine_dump(jsonMessage);
		return jsonMessage.toString();
		}
	}

	public JSONObject combine_dump(JSONObject obj) {
		if (has_rel_out(obj)) {
			JSONArray rel_out = rel_out(obj);
			// aggiungi dump se lo accettano.
			for (int i = 0; i < rel_out.length(); i++) {
				JSONObject out = rel_out.getJSONObject(i);
				if (out.getString("category").equals("value") && out.getString("name").equals("dump")) {
					obj.accumulate("value", new JSONObject(dump));
					break;
				} else if (!out.getString("category").equals("value")) {
					break;
				}
			}
		}
		return obj;
	}

	private String getName(Resource res) {
		String result = null;
		if (res.hasProperty(Vocabulary.name)) {
			result = ((Literal) res.getProperty(Vocabulary.name).getObject()).getString();
		}
		return result;
	}

	private String getCategory(Resource res) {
		String result = null;
		if (res.hasProperty(RDF.type)) {
			RDFNode ontClassNode = res.getProperty(RDF.type).getObject();
			Resource ontClass = model.getResource(ontClassNode.toString());
			result = getName(ontClass);
		}
		return result;
	}

	private Resource getResource(JSONObject message) {
		Resource result = null;
		JSONObject json = new JSONObject(message.toString());
		String category = json.getString("category");
		String semElement = json.getString("name");
		json.remove("value");

		if (!cache.containsKey(json.toString())) {
			StmtIterator iterClass = model
					.listStatements(new SimpleSelector(null, Vocabulary.name, category));
			while (iterClass.hasNext()) {
				Statement ontClassStm = iterClass.next();
				Resource ontClass = ontClassStm.getSubject();
				StmtIterator iterElement = model.listStatements(new SimpleSelector(null, RDF.type, ontClass));
				while (iterElement.hasNext()) {
					Statement elementStm = iterElement.next();
					Resource element = elementStm.getSubject();
					String elementName = ((Literal) element.getProperty(Vocabulary.name).getObject())
							.getString();
					JSONObject json_element = new JSONObject();
					json_element.put("category", category);
					json_element.put("name", elementName);
					if (elementName.equals(semElement)) {
						result = element;
					}
					cache.put(json_element.toString(), element);
					// if (DEBUG)
					// System.out.println("GET res of " + json_element + " ADD
					// IN CACHE");
				}
			}
		} else {
			result = cache.get(json.toString());
			// if (DEBUG)
			// System.out.println("GET res of " + json + " IN CACHE");
		}
		return result;
	}

	private List<Resource> in_Relation(Resource res) {
		List<Resource> result = null;
		if (cache_in.containsKey(res)) {
			result = cache_in.get(res);
		} else {
			result = new ArrayList<>();
			StmtIterator iter = model.listStatements(new SimpleSelector(null, null, res));
			while (iter.hasNext()) {
				Statement inStm = iter.next();
				Resource in_res = inStm.getSubject();
				for (Property p : Vocabulary.listProperty) {
					if (in_res.hasProperty(p)) {
						result.add(in_res);
						break;
					}
				}
			}
			cache_in.put(res, result);
		}
		return result;
	}

	private List<Resource> out_Relation(Resource res) {
		List<Resource> result = null;
		if (cache_out.containsKey(res)) {
			result = cache_out.get(res);
		} else {
			result = new ArrayList<>();
			for (ObjectProperty p : Vocabulary.listProperty) {
				NodeIterator iter = model.listObjectsOfProperty(res, p);
				while (iter.hasNext()) {
					RDFNode value = iter.next();
					Resource outRes = model.getResource(value.toString());
					result.add(outRes);
				}
			}
			cache_out.put(res, result);
		}
		return result;
	}

	public String get_message_not(JSONObject obj) {
		String result = null;
		JSONObject json = new JSONObject();
		json.accumulate("name", obj.get("name"));
		json.accumulate("category", obj.get("category"));

		Resource res = getResource(json);
		NodeIterator itrNot = model.listObjectsOfProperty(res, Vocabulary.message_not);
		while (itrNot.hasNext()) {
			RDFNode NodeEx = itrNot.next();
			result = ((Literal) NodeEx).getString();
		}
		if (JSON_utils.isJSONObject(result)) {
			String field = (new JSONObject(result)).getString("value");
			result = obj.get(field).toString();
		}
		return result;
	}

	public String get_message_singular(JSONObject obj) {
		String result = null;
		JSONObject json = new JSONObject();
		json.accumulate("name", obj.get("name"));
		json.accumulate("category", obj.get("category"));

		Resource res = getResource(json);
		NodeIterator itrNot = model.listObjectsOfProperty(res, Vocabulary.message_singular);
		while (itrNot.hasNext()) {
			RDFNode NodeEx = itrNot.next();
			result = ((Literal) NodeEx).getString();
		}
		if (JSON_utils.isJSONObject(result)) {
			String field = (new JSONObject(result)).getString("value");
			result = obj.get(field).toString();
		}
		return result;
	}

	public String get_message_plural(JSONObject obj) {
		String result = null;
		JSONObject json = new JSONObject();
		json.accumulate("name", obj.get("name"));
		json.accumulate("category", obj.get("category"));

		Resource res = getResource(json);
		NodeIterator itrNot = model.listObjectsOfProperty(res, Vocabulary.message_plural);
		while (itrNot.hasNext()) {
			RDFNode NodeEx = itrNot.next();
			result = ((Literal) NodeEx).getString();
		}
		if (JSON_utils.isJSONObject(result)) {
			String field = (new JSONObject(result)).getString("value");
			result = obj.get(field).toString();
		}
		return result;
	}

	public List<String> relationClass() {
		List<String> result = new ArrayList<>();
		StmtIterator iter = model.listStatements(new Selector() {

			@Override
			public boolean test(Statement s) {
				Property p = s.getPredicate();
				// System.out.println(p+"=="+LavazzaVocabularyClasses.relation+"
				// ->"+p.equals(LavazzaVocabularyClasses.relation));
				return p.equals(Vocabulary.relation);
			}

			@Override
			public boolean isSimple() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Resource getSubject() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Property getPredicate() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RDFNode getObject() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		while (iter.hasNext()) {
			Statement stm = iter.next();
			Resource head = stm.getSubject();
			String head_name = getName(head);
			Resource tail = (Resource) stm.getObject();
			String tail_name = getName(tail);
			if (result.contains(head_name)) {
				int index = result.indexOf(head_name);
				result.add(index + 1, tail_name);
			} else if (result.contains(tail_name)) {
				int index = result.indexOf(tail_name);
				result.add(index, head_name);
			} else {
				result.add(head_name);
				result.add(tail_name);
			}
		}
		return result;

	}

	private Map<String, Boolean> classCompleteness() {
		Map<String, Boolean> map = new HashMap<>();
		StmtIterator iterClass = model.listStatements(new SimpleSelector(null, RDF.type, RDFS.Class));
		while (iterClass.hasNext()) {
			Statement stmClass = iterClass.next();
			Resource res = stmClass.getSubject();
			Statement resName = res.getProperty(Vocabulary.name);
			if (resName != null) {
				Statement stmCompleteness = res.getProperty(Vocabulary.completeness);
				Boolean completeness = ((Literal) stmCompleteness.getObject()).getBoolean();
				map.put(getName(res), completeness);
			}
		}
		return map;
	}

	public List<String> getHierarchy() {
		return hierarchy;
	}

	public Map<String, Boolean> getClassCompleteness() {
		return classCompleteness;
	}

//	public boolean incomplete_OLD(JSONObject obj) {
//		boolean result = true;
//		if (has_rel_out(obj)) {
//			String rel_out = obj.get("rel_out").toString();
//			JSONArray array_out = JSON_utils.convertJSONArray(rel_out);
//			for (int i = 0; i < array_out.length() && result; i++) {
//				JSONObject out = array_out.getJSONObject(i);
//				String category_out = out.getString("category");
//				result = !obj.has(category_out);
//			}
//		} else {
//			result = false;
//		}
//		return result;
//	}
	
	public boolean incomplete(JSONObject obj) {
		boolean result = true;
		String name=obj.getString("category");
		if (has_rel_out(obj)) {
			List<String> complete=getComplete(name);
			for (int i = 0; i < complete.size() && result; i++) {
				String category_out = complete.get(i);
				result = !obj.has(category_out);
			}
		} else {
			result = false;
		}
		return result;
	}

	public JSONArray remove_incomplete(JSONArray array) {
		JSONArray result = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			if (!incomplete(obj)) {
				result.put(obj);
			}
		}
		return result;
	}

	public List<String> getTemplate(String name) {
		List<String> result;
		if (template.containsKey(name)) {
			result = template.get(name);
		} else {
			result = new ArrayList<>();
			StmtIterator iterClass = model
					.listStatements(new SimpleSelector(null, Vocabulary.name, name));
			while (iterClass.hasNext()) {
				Statement stmClass = iterClass.next();
				Resource res = stmClass.getSubject();
				Statement listTemplate = res.getProperty(Vocabulary.template);
				if (listTemplate != null) {
					Resource list = (Resource) listTemplate.getObject();
					while (list != null && list.hasProperty(RDF.first)) {
						Resource first = (Resource) list.getProperty(RDF.first).getObject();
						result.add(getName(first));
						if (list.hasProperty(RDF.rest)) {
							list = (Resource) list.getProperty(RDF.rest).getObject();
						} else {
							list = null;
						}
					}
				}
			}
			template.put(name, result);
		}
		return result;
	}
	public List<String> getComplete(String name) {
		List<String> result;
		if (complete.containsKey(name)) {
			result = complete.get(name);
		} else {
			result = new ArrayList<>();
			StmtIterator iterClass = model
					.listStatements(new SimpleSelector(null, Vocabulary.name, name));
			while (iterClass.hasNext()) {
				Statement stmClass = iterClass.next();
				Resource res = stmClass.getSubject();
				Statement listcomplete = res.getProperty(Vocabulary.complete);
				if (listcomplete != null) {
					Resource list = (Resource) listcomplete.getObject();
					while (list != null && list.hasProperty(RDF.first)) {
						Resource first = (Resource) list.getProperty(RDF.first).getObject();
						result.add(getName(first));
						if (list.hasProperty(RDF.rest)) {
							list = (Resource) list.getProperty(RDF.rest).getObject();
						} else {
							list = null;
						}
					}
				}
			}
			complete.put(name, result);
		}
		return result;
	}

	public boolean onlyOne(JSONObject obj) {
		Boolean result;
		String name=obj.getString("category");
		if (onlyOne.containsKey(name)) {
			result = onlyOne.get(name);
		} else {
			result = false;
			StmtIterator iterClass = model
					.listStatements(new SimpleSelector(null, Vocabulary.name, name));
			while (iterClass.hasNext()) {
				Statement stmClass = iterClass.next();
				Resource res = stmClass.getSubject();
				Statement stmOnlyOne = res.getProperty(Vocabulary.onlyOne);
				Boolean onlyOne = ((Literal) stmOnlyOne.getObject()).getBoolean();
				this.onlyOne.put(name, onlyOne);
				result=onlyOne;
				break;
			}
		}
		return result;
	}

}
