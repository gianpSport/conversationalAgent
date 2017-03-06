package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM051 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Quali sono i nuovi gusti speciali disponibili?";
	String result_message="{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"sono i nuovi\"}}{\"result\":[{\"composition\":\"mix\",\"name\":\"lavazza qualità rossa\"},{\"composition\":\"arabic\",\"name\":\"lavazza selva alta\"},{\"composition\":\"arabic\",\"name\":\"lavazza cereja passita\"},{\"composition\":\"arabic\",\"name\":\"lavazza aromatico\"},{\"composition\":\"arabic\",\"name\":\"lavazza ricco\"},{\"composition\":\"arabic\",\"name\":\"lavazza tierra\"},{\"composition\":\"arabic\",\"name\":\"lavazza magia\"},{\"composition\":\"arabic\",\"name\":\"lavazza passionale\"},{\"composition\":\"arabic\",\"name\":\"lavazza soave\"},{\"composition\":\"mix\",\"name\":\"lavazza intenso\"},{\"composition\":\"arabic\",\"name\":\"lavazza deck cremoso\"},{\"composition\":\"arabic\",\"name\":\"lavazza delizioso\"},{\"composition\":\"mix\",\"name\":\"lavazza divino\"},{\"composition\":\"arabic\",\"name\":\"lavazza dolce\"},{\"composition\":\"ginseng\",\"name\":\"lavazza caffè ginseng\"},{\"composition\":\"barley\",\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":{\"name\":\"composition\",\"category\":\"property\",\"token\":\"gusti\"},\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quali\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"speciali\"}}";
	String real_result="{\"result\":[{\"composition\":\"mix\"},{\"composition\":\"arabic\"},{\"composition\":\"ginseng\"},{\"composition\":\"barley\"}]}";
	
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
		DM_AllTests.updateMesure(DM_AllTests.last_precision,DM_AllTests.last_recall,1);
	}

}
