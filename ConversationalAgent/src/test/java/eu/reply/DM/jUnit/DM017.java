package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM017 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="quante capsule ci sono in una scatola di capsule di intensità 9?";
	String result_message="{\"result\":[{\"name\":\"lavazza cereja passita\",\"pack\":{\"quantity\":12,\"price\":4.99}},{\"name\":\"lavazza tierra\",\"pack\":{\"quantity\":16,\"price\":6.49}}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":9},\"name\":\"intensity\",\"category\":\"property\",\"token\":\"intensità\"},{\"name\":\"quantity\",\"category\":\"property\",\"token\":\"scatola\"}],\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quante\"}}";
	String real_result="{\"result\":[{\"name\":\"lavazza cereja passita\",\"pack\":{\"quantity\":12,\"price\":4.99}},{\"name\":\"lavazza tierra\",\"pack\":{\"quantity\":16,\"price\":6.49}}]}";
	
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
