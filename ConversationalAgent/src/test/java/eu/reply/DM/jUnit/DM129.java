package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM129 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Mi servirebbe una macchinetta con il vapore, quale mi consigli?";
	String result_message="{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"fantasia plus\"},{\"name\":\"minù caffè latte\"}],\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"milkProgram\",\"category\":\"property\",\"value\":{\"name\":\"true_milkProgram\",\"category\":\"value\",\"token\":\"vapore\"}},\"category\":\"domain\",\"token\":\"macchinetta\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"mi servirebbe\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"consigli\"}}";
	String real_result="{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"fantasia plus\"},{\"name\":\"minù caffè latte\"}]}";
	
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
