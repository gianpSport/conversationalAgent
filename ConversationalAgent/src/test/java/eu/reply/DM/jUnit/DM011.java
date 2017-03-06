package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM011 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="avrei bisogno di capsule con tostatura scura e intensità superiore a 7.";
	String result_message="{\"result\":[{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza divino\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":[{\"name\":\"roasting\",\"category\":\"property\",\"value\":{\"name\":\"black\",\"category\":\"value\",\"token\":\"scura\"},\"token\":\"tostatura\"},{\"number\":{\"name\":\"integer\",\"category\":\"number\",\"token\":7},\"mod\":{\"name\":\"more\",\"category\":\"mod\",\"token\":\"superiore\"},\"name\":\"intensity\",\"category\":\"property\",\"token\":\"intensità\"}],\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avrei bisogno\"}}";
	String real_result="{\"result\":[{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza divino\"}]}";
	
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
