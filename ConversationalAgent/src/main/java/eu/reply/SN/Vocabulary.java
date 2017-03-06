package eu.reply.SN;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.SymmetricProperty;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.reply.Config.Config;

public class Vocabulary {
	
	private static String url_onto=Config.getPathOntology();

	private static OntModel m_model = ModelFactory.createOntologyModel();

	public static final String NS = "http://eu.reply.it/Lavazza#";

	// Priorità tra categorie semantiche a livello di Pattern Matching
	public static final Property salience = m_model.createProperty(NS + "salience");

	// Proprietà necessaria per codificare la stretta necessità di avere un
	// oggetto legato
	public static final Property completeness = m_model.createProperty(NS + "completeness");

	// Proprietà necessaria per la definizione di completezza
	public static final Property complete = m_model.createProperty(NS + "complete");

	// definizione della prorpietà che lega classi e istanze ai relativi
	// template
	public static final OntProperty template = m_model.createOntProperty(NS + "template");

	// definizione della proprietà che lega classi e istanze a un letterale che
	// rappresenta il nome
	public static final ObjectProperty name = m_model.createObjectProperty(NS + "name");

	// definizione di una proprietà necessaria per limitare il numero di
	// elementi legabili (1 Action -> 1 Domain)
	public static final ObjectProperty onlyOne = m_model.createObjectProperty(NS + "onlyOne");

	public static final OntClass actionClass = (OntClass) m_model.createClass(NS + "class/action")
			.addLiteral(salience, 100).addLiteral(name, "action").addLiteral(completeness, true)
			.addLiteral(onlyOne, true);

	public static final OntClass domainClass = (OntClass) m_model.createClass(NS + "class/domain")
			.addLiteral(salience, 90).addLiteral(name, "domain").addLiteral(completeness, false)
			.addLiteral(onlyOne, false);

	public static final OntClass propertyClass = (OntClass) m_model.createClass(NS + "class/property")
			.addLiteral(salience, 80).addLiteral(name, "property").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final OntClass modClass = (OntClass) m_model.createClass(NS + "class/mod").addLiteral(salience, 70)
			.addLiteral(name, "mod").addLiteral(completeness, false).addLiteral(onlyOne, false);

	public static final OntClass unitClass = (OntClass) m_model.createClass(NS + "class/unit").addLiteral(salience, 60)
			.addLiteral(name, "unit").addLiteral(completeness, false).addLiteral(onlyOne, true);

	public static final OntClass numberClass = (OntClass) m_model.createClass(NS + "class/number")
			.addLiteral(salience, 50).addLiteral(name, "number").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final OntClass valueClass = (OntClass) m_model.createClass(NS + "class/value")
			.addLiteral(salience, 40).addLiteral(name, "value").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final OntClass dialogClass = (OntClass) m_model.createClass(NS + "class/dialog")
			.addLiteral(salience, 20).addLiteral(name, "dialog").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	// solo perchè non considero attualmente la descrizione
	public static final OntClass extractedClass = (OntClass) m_model.createClass(NS + "class/extracted")
			.addLiteral(salience, -1).addLiteral(name, "extracted").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final OntClass noAnswer = (OntClass) m_model.createClass(NS + "class/noAnswer")
			.addLiteral(salience, -1).addLiteral(name, "noAnswer").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final OntClass confirmQuestion = (OntClass) m_model.createClass(NS + "class/noAnswer")
			.addLiteral(salience, -1).addLiteral(name, "confirmQuestion").addLiteral(completeness, false)
			.addLiteral(onlyOne, true);

	public static final ObjectProperty applicable = m_model.createObjectProperty(NS + "property/applicable");

	public static final ObjectProperty example = m_model.createObjectProperty(NS + "input/example");

	public static final ObjectProperty message_singular = m_model.createObjectProperty(NS + "output/message_singular");

	public static final ObjectProperty message_plural = m_model.createObjectProperty(NS + "output/message_plural");

	public static final ObjectProperty message_not = m_model.createObjectProperty(NS + "output/message_not");

	public static final ObjectProperty hasA = m_model.createObjectProperty(NS + "property/hasA");

	public static final ObjectProperty value = m_model.createObjectProperty(NS + "property/value");

	public static final ObjectProperty number = m_model.createObjectProperty(NS + "property/number");

	public static final ObjectProperty mod = m_model.createObjectProperty(NS + "property/mod");

	public static final ObjectProperty unit = m_model.createObjectProperty(NS + "property/unit");

	public static final SymmetricProperty compatible = m_model.createSymmetricProperty(NS + "property/compatible");

	public static final ObjectProperty relation = m_model.createObjectProperty(NS + "classRelation");

	public static final ObjectProperty[] listProperty = { applicable, hasA, value, number, mod, unit, compatible };

	public static final OntClass[] listClass = { dialogClass, actionClass, domainClass, propertyClass, valueClass,
			/* questionClass, */numberClass, unitClass, modClass, extractedClass };

	private static RDFList template_action = m_model.createList(new RDFNode[] { domainClass });
	private static RDFList template_domain = m_model.createList(new RDFNode[] { propertyClass });
	private static RDFList template_property = m_model
			.createList(new RDFNode[] { valueClass, modClass, numberClass, unitClass });
	private static RDFList template_value = m_model.createList(new RDFNode[] {});
	private static RDFList template_mod = m_model.createList(new RDFNode[] {});
	private static RDFList template_number = m_model.createList(new RDFNode[] {});
	private static RDFList template_unit = m_model.createList(new RDFNode[] {});

	private static RDFList complete_action = m_model.createList(new RDFNode[] { domainClass });
	private static RDFList complete_domain = m_model.createList(new RDFNode[] { propertyClass });
	private static RDFList complete_property = m_model.createList(new RDFNode[] { numberClass, valueClass });

	public static OntModel define() {
		name.addRange(RDFS.Literal);
		applicable.addDomain(actionClass);
		applicable.addRange(domainClass);
		example.addRange(RDFS.Literal);
		hasA.addDomain(domainClass);
		hasA.addRange(propertyClass);
		value.addDomain(propertyClass);
		value.addRange(valueClass);
		number.addDomain(propertyClass);
		number.addRange(numberClass);
		unit.addDomain(propertyClass);
		unit.addRange(unitClass);
		mod.addDomain(propertyClass);
		mod.addRange(modClass);

		/* Template definition */
		actionClass.addProperty(template, template_action);
		domainClass.addProperty(template, template_domain);
		propertyClass.addProperty(template, template_property);
		valueClass.addProperty(template, template_value);
		modClass.addProperty(template, template_mod);
		numberClass.addProperty(template, template_number);
		unitClass.addProperty(template, template_unit);

		/* Complete definition */
		actionClass.addProperty(complete, complete_action);
		domainClass.addProperty(complete, complete_domain);
		propertyClass.addProperty(complete, complete_property);

		m_model.add(actionClass, relation, domainClass).add(domainClass, relation, propertyClass)
				.add(propertyClass, relation, modClass).add(modClass, relation, unitClass)
				.add(unitClass, relation, numberClass).add(numberClass, relation, valueClass);
		
		try {
			m_model.write(new FileOutputStream(url_onto));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m_model;
	}
}
