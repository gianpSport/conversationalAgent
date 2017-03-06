package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM021 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="quanto costa una scatola da 16 di lavazza magia?";
	String result_message="{\"result\":{\"name\":\"lavazza magia\",\"pack\":{\"quantity\":16,\"price\":6.49}},\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"name\":\"price\",\"category\":\"property\",\"token\":\"costa\"},{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":16},\"name\":\"quantity\",\"category\":\"property\",\"token\":\"scatola\"},{\"name\":\"name capsule\",\"category\":\"property\",\"value\":{\"name\":\"lavazza magia\",\"category\":\"value\",\"token\":\"lavazza magia\"}}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quanto\"}}";
	String real_result="{\"result\":{\"name\":\"lavazza magia\",\"pack\":{\"quantity\":16,\"price\":6.49}}}";
	
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
