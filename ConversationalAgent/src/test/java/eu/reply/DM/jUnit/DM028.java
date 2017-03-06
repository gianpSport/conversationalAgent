package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM028 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="quanta acqua entra in magia?";
	String result_message="{\"result\":{\"name\":\"magia\",\"tank capacity\":0.85},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"tank capacity\",\"category\":\"property\",\"token\":\"acqua\"},{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"magia\",\"category\":\"value\",\"token\":\"magia\"}}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"quanta\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"entra\"}}";
	String real_result="{\"result\":{\"tank capacity\":0.85}}";
	
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
