package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM067 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Mi date capsule con gusto intenso e corposo? ";
	String result_message="{\"result\":[{\"intensity\":10,\"composition\":\"mix\",\"name\":\"lavazza qualità rossa\"},{\"intensity\":7,\"composition\":\"arabic\",\"name\":\"lavazza selva alta\"},{\"intensity\":9,\"composition\":\"arabic\",\"name\":\"lavazza cereja passita\"},{\"intensity\":6,\"composition\":\"arabic\",\"name\":\"lavazza aromatico\"},{\"intensity\":7,\"composition\":\"arabic\",\"name\":\"lavazza ricco\"},{\"intensity\":9,\"composition\":\"arabic\",\"name\":\"lavazza tierra\"},{\"intensity\":8,\"composition\":\"arabic\",\"name\":\"lavazza magia\"},{\"intensity\":11,\"composition\":\"arabic\",\"name\":\"lavazza passionale\"},{\"intensity\":5,\"composition\":\"arabic\",\"name\":\"lavazza soave\"},{\"intensity\":13,\"composition\":\"mix\",\"name\":\"lavazza intenso\"},{\"intensity\":7,\"composition\":\"arabic\",\"name\":\"lavazza deck cremoso\"},{\"intensity\":8,\"composition\":\"arabic\",\"name\":\"lavazza delizioso\"},{\"intensity\":11,\"composition\":\"mix\",\"name\":\"lavazza divino\"},{\"intensity\":6,\"composition\":\"arabic\",\"name\":\"lavazza dolce\"},{\"intensity\":-1,\"composition\":\"ginseng\",\"name\":\"lavazza caffè ginseng\"},{\"intensity\":-1,\"composition\":\"barley\",\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"name\":\"composition\",\"category\":\"property\",\"token\":\"gusto\"},{\"name\":\"intensity\",\"category\":\"property\",\"token\":\"intenso\"}],\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"mi date\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"corposo\"}}";
	String real_result="{\"result\":{\"name\":\"lavazza intenso\"}}";
	
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
