package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM013 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="avete capsule con miscela arabic a meno di 7 euro con intensità superiore a 6?";
	String result_message="{\"result\":[{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza tierra\"},{\"name\":\"lavazza magia\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza dolce\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"name\":\"composition\",\"category\":\"property\",\"value\":{\"name\":\"arabic\",\"category\":\"value\",\"token\":\"arabic\"},\"token\":\"miscela\"},{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":7},\"unit\":{\"name\":\"euro\",\"category\":\"unit\",\"token\":\"euro\"},\"mod\":{\"name\":\"less\",\"category\":\"mod\",\"token\":\"meno\"},\"name\":\"price\",\"category\":\"property\"},{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":6},\"mod\":{\"name\":\"more\",\"category\":\"mod\",\"token\":\"superiore\"},\"name\":\"intensity\",\"category\":\"property\",\"token\":\"intensità\"}],\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avete\"}}";
	String real_result="{\"result\":[{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza tierra\"},{\"name\":\"lavazza magia\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza dolce\"}]}";
		
	@Before
	public void setUp() throws Exception {
		System.out.println("INIT "+message);
		dm=new DialogManager(DM_AllTests.net,threshold,DM_AllTests.read);
		DM_AllTests.last_precision=0.0d;
		DM_AllTests.last_recall=0.0d;
	}

	@Test
	public void testMessage() {
		String testResult=dm.message(message);
		System.out.println("TEST: "+testResult);
		System.out.println("RES:  "+result_message);
		assertTrue(message, result_message.equals(testResult));
	}
	
	@After
	public void after(){
		System.out.println("after execution");
		DM_AllTests.last_precision=DM_AllTests.calc_precision(result_message, real_result);
		DM_AllTests.last_recall=DM_AllTests.calc_recall(result_message, real_result);
		System.out.println("precision:"+DM_AllTests.last_precision);
		System.out.println("recall:"+DM_AllTests.last_recall);
		DM_AllTests.updateMesure(DM_AllTests.last_precision,DM_AllTests.last_recall,0);
	}

}
