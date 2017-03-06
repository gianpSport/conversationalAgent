package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM069 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Avete macchine da caffè in acciaio, rosse o blu? ";
	String result_message="{\"result\":{\"name\":\"espria plus\"},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"color\",\"category\":\"property\",\"value\":{\"name\":\"grey\",\"category\":\"value\",\"token\":\"acciaio\"}},\"category\":\"domain\",\"token\":\"macchine da caffè\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avete\"}}{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"jolie\"},{\"name\":\"minù\"},{\"name\":\"minù caffè latte\"},{\"name\":\"magia\"}],\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"color\",\"category\":\"property\",\"value\":{\"name\":\"red\",\"category\":\"value\",\"token\":\"ross\"}},\"category\":\"domain\",\"token\":\"macchine da caffè\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avete\"}}{\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"color\",\"category\":\"property\",\"value\":{\"name\":\"blue\",\"category\":\"value\",\"token\":\"blu\"}},\"category\":\"domain\",\"token\":\"macchine da caffè\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"avete\"}}";
	String real_result="{\"result\":{\"name\":\"espria plus\"}}";
	
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
