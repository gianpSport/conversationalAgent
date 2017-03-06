package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM068 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Vorrei una macchina da caffè leggera e grigia metallizzata ";
	String result_message="{\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"light\",\"category\":\"property\",\"value\":{\"name\":\"true_light\",\"category\":\"value\",\"token\":\"legger\"}},{\"name\":\"color\",\"category\":\"property\",\"value\":{\"name\":\"grey\",\"category\":\"value\",\"token\":\"grigi\"}}],\"category\":\"domain\",\"token\":\"macchina da caffè\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"metallizzata\"}}";
	String real_result="{}";
	
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