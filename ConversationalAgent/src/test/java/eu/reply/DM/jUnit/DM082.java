package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM082 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Quanto costa una confezione con 16 capsule?";
	String result_message="{\"result\":[{\"name\":\"lavazza qualità rossa\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza tierra\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza magia\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza passionale\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza soave\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza intenso\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza deck cremoso\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza delizioso\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza divino\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza dolce\",\"pack\":{\"quantity\":16,\"price\":5.99}}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"name\":\"price\",\"category\":\"property\",\"token\":\"costa\"},{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":16},\"name\":\"quantity\",\"category\":\"property\",\"token\":\"confezione\"}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quanto\"}}";
	String real_result="{\"result\":[{\"pack\":{\"price\":5.99}},{\"pack\":{\"price\":6.49}}]}";
	
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
