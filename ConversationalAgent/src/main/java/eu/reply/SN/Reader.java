package eu.reply.SN;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.reply.Config.Config;

public class Reader {

	public static void main(String[] args) {
		System.out.print(readNLU(readModel(Config.getPathSemanticNet())));
	}

	public static JSONObject readNLU(OntModel model) {
		JSONObject result=new JSONObject();
		
		Map<Resource, Integer> map = classQuery(model);
		for (Resource r : map.keySet()) {

			String Classname = resName(r);
			Integer salience = map.get(r);

			//System.out.println(Classname+" salience: "+map.get(r));
			JSONObject ontoClass = new JSONObject();
			ontoClass.accumulate("name", Classname);
			ontoClass.accumulate("salience", salience);

			List<Resource> elements = semElementQuery(model, r);
			for (Resource e : elements) {

				String Elementname = resName(e);

				 //System.out.println("		"+Elementname);

				JSONObject element = new JSONObject();
				element.accumulate("name", Elementname);

				List<String> examples = ExampleQuery(model, e);
				for (String ex : examples) {
					element.accumulate("examples", ex);
					//System.out.println("			"+ex);
				}

				ontoClass.accumulate("elements", element);
			}
			result.accumulate("result", ontoClass);
		}
		
		return result;

	}
	
	public static OntModel readModel(String url){
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(url);
		if (in == null) {
			throw new IllegalArgumentException("File: " + url + " not found");
		}

		// read the RDF/XML file
		model.read(in, Creator.format);
		return model;
	}

	private static String resName(Resource res) {
		String result = null;
		if (res.hasProperty(Vocabulary.name)) {
			result = ((Literal) res.getProperty(Vocabulary.name).getObject()).getString();
		}
		return result;
	}

	private static Map<Resource, Integer> classQuery(OntModel model) {
		Map<Resource, Integer> map = new HashMap<>();
		StmtIterator iterClass = model.listStatements(new SimpleSelector(null, RDF.type, RDFS.Class));
		while (iterClass.hasNext()) {
			Statement stmClass = iterClass.next();
			Resource res = stmClass.getSubject();
			Statement resName = res.getProperty(Vocabulary.name);
			if (resName != null) {
				Statement stmSalience = res.getProperty(Vocabulary.salience);
				Integer salience = ((Literal) stmSalience.getObject()).getInt();
				map.put(res, salience);
			}
		}
		return map;
	}

	private static List<Resource> semElementQuery(Model model, Resource c) {
		List<Resource> list = new ArrayList<>();
		StmtIterator iterElement = model.listStatements(new SimpleSelector(null, RDF.type, c));
		while (iterElement.hasNext()) {
			Statement stmElement = iterElement.next();
			Resource res = stmElement.getSubject();
			list.add(res);
		}
		return list;
	}

	private static List<String> ExampleQuery(Model model, Resource c) {
		List<String> list = new ArrayList<>();
		if (c.hasProperty(Vocabulary.example)) {
			NodeIterator itrExample = model.listObjectsOfProperty(c, Vocabulary.example);
			while (itrExample.hasNext()) {
				RDFNode NodeEx = itrExample.next();
				String ex = ((Literal) NodeEx).getString();
				list.add(ex);
			}
		}
		return list;
	}

}
