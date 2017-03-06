package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM035 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="vorrei delle capsule a meno di 5 euro compatibili con la jolie.";
	String result_message="{\"result\":[{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza caffè ginseng\"},{\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":[{\"name\":\"capsule\",\"property\":[{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":5},\"unit\":{\"name\":\"euro\",\"category\":\"unit\",\"token\":\"euro\"},\"mod\":{\"name\":\"less\",\"category\":\"mod\",\"token\":\"meno\"},\"name\":\"price\",\"category\":\"property\"},{\"name\":\"compatible\",\"category\":\"property\",\"token\":\"compatibili\"}],\"category\":\"domain\",\"token\":\"capsule\"},{\"name\":\"machine\",\"property\":[{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"jolie\",\"category\":\"value\",\"token\":\"jolie\"}},{\"name\":\"brand\",\"category\":\"property\"}],\"category\":\"domain\"}],\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}";
	String real_result="{\"result\":[{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza caffè ginseng\"},{\"name\":\"lavazza orzo\"}]}";
	
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
		DM_AllTests.updateMesure(DM_AllTests.last_precision,DM_AllTests.last_recall,2);
	}

}
