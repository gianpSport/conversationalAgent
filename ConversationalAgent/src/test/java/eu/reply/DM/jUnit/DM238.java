package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM238 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Fate ancora le capsule Crema Aroma?";
	String result_message="{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"fate ancora\"}}{\"result\":[{\"name\":\"lavazza qualità rossa\"},{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza tierra\"},{\"name\":\"lavazza magia\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza soave\"},{\"name\":\"lavazza intenso\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza divino\"},{\"name\":\"lavazza dolce\"},{\"name\":\"lavazza caffè ginseng\"},{\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"crema aroma\"}}";
	String real_result="{}";
	
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
