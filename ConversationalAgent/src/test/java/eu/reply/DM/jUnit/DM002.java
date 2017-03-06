package eu.reply.DM.jUnit;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM002 extends DM000{
	

	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="avete capsule a 5.99 euro?";
	String result_message="{\"result\":[{\"name\":\"lavazza qualità rossa\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza soave\"},{\"name\":\"lavazza intenso\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza dolce\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":{\"number\":{\"name\":\"float\",\"category\":\"number\",\"token\":5.99},\"unit\":{\"name\":\"euro\",\"category\":\"unit\",\"token\":\"euro\"},\"name\":\"price\",\"category\":\"property\"},\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avete\"}}";
	String real_result="{\"result\":[{\"name\":\"lavazza qualità rossa\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza soave\"},{\"name\":\"lavazza intenso\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza dolce\"}]}";
	
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
