package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM032 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="mi interessa la jolie quanto costa?";
	String result_message="{\"result\":{\"price\":79.9,\"name\":\"jolie\"},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"jolie\",\"category\":\"value\",\"token\":\"jolie\"}},{\"name\":\"price\",\"category\":\"property\",\"token\":\"costa\"}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"mi interessa\"}}";
	String real_result="{\"result\":{\"price\":79.9}}";
	
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
