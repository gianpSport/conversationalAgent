package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM111 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="La capsula Cereja Passita può essere inserita in Lavazza minù?";
	String result_message="{\"result\":{\"name\":\"lavazza cereja passita\"},\"query\":{\"domain\":[{\"name\":\"capsule\",\"property\":[{\"name\":\"name capsule\",\"category\":\"property\",\"value\":{\"name\":\"lavazza cereja passita\",\"category\":\"value\",\"token\":\"cereja passita\"}},{\"name\":\"compatible\",\"category\":\"property\",\"token\":\"essere inserit\"}],\"category\":\"domain\",\"token\":\"capsula\"},{\"name\":\"machine\",\"property\":[{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"minù\",\"category\":\"value\",\"token\":\"lavazza minù\"}},{\"name\":\"brand\",\"category\":\"property\"}],\"category\":\"domain\"}],\"name\":\"search\",\"category\":\"action\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"può\"}}";
	String real_result="{\"result\":{\"name\":\"lavazza cereja passita\"}}";
	
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
