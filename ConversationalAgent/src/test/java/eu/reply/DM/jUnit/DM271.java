package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM271 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Quali sono le varietà di tostatura disponibili?";
	String result_message="{\"result\":[{\"roasting\":\"average\",\"name\":\"lavazza qualità rossa\"},{\"roasting\":\"average\",\"name\":\"lavazza selva alta\"},{\"roasting\":\"average\",\"name\":\"lavazza cereja passita\"},{\"roasting\":\"average\",\"name\":\"lavazza aromatico\"},{\"roasting\":\"black\",\"name\":\"lavazza ricco\"},{\"roasting\":\"average\",\"name\":\"lavazza tierra\"},{\"roasting\":\"average\",\"name\":\"lavazza magia\"},{\"roasting\":\"black\",\"name\":\"lavazza passionale\"},{\"roasting\":\"average\",\"name\":\"lavazza soave\"},{\"roasting\":\"average\",\"name\":\"lavazza intenso\"},{\"roasting\":\"average\",\"name\":\"lavazza deck cremoso\"},{\"roasting\":\"average\",\"name\":\"lavazza delizioso\"},{\"roasting\":\"black\",\"name\":\"lavazza divino\"},{\"roasting\":\"average\",\"name\":\"lavazza dolce\"},{\"roasting\":\"no tostato\",\"name\":\"lavazza caffè ginseng\"},{\"roasting\":\"no tostato\",\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":{\"name\":\"roasting\",\"category\":\"property\",\"token\":\"tostatura\"},\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quali\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"sono le varietà\"}}";
	String real_result="{\"result\":[{\"roasting\":\"average\"},{\"roasting\":\"black\"},{\"roasting\":\"no tostato\"}]}";
	
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
