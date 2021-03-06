package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM057 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Esistono diversi formati di capsule?";
	String result_message="{\"result\":[{\"name\":\"lavazza qualità rossa\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza selva alta\",\"pack\":{\"quantity\":12,\"price\":4.99}},{\"name\":\"lavazza cereja passita\",\"pack\":{\"quantity\":12,\"price\":4.99}},{\"name\":\"lavazza aromatico\",\"pack\":{\"quantity\":6,\"price\":2.49}},{\"name\":\"lavazza ricco\",\"pack\":{\"quantity\":6,\"price\":2.49}},{\"name\":\"lavazza tierra\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza magia\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza passionale\",\"pack\":[{\"quantity\":16,\"price\":5.99},{\"quantity\":36,\"price\":11.99}]},{\"name\":\"lavazza soave\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza intenso\",\"pack\":[{\"quantity\":16,\"price\":5.99},{\"quantity\":36,\"price\":11.99}]},{\"name\":\"lavazza deck cremoso\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza delizioso\",\"pack\":[{\"quantity\":16,\"price\":5.99},{\"quantity\":36,\"price\":11.99}]},{\"name\":\"lavazza divino\",\"pack\":{\"quantity\":16,\"price\":6.49}},{\"name\":\"lavazza dolce\",\"pack\":{\"quantity\":16,\"price\":5.99}},{\"name\":\"lavazza caffè ginseng\",\"pack\":{\"quantity\":12,\"price\":4.99}},{\"name\":\"lavazza orzo\",\"pack\":{\"quantity\":12,\"price\":4.49}}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":{\"name\":\"quantity\",\"category\":\"property\",\"token\":\"formati\"},\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"esistono\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"diversi\"}}";
	String real_result="{\"result\":[{\"pack\":{\"quantity\":12}},{\"pack\":{\"quantity\":16}},{\"pack\":{\"quantity\":6}},{\"pack\":{\"quantity\":36}}]}";
	
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
