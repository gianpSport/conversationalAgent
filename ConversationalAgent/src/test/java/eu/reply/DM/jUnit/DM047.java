package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM047 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="con quali marche di macchine da caffè sono compatibili le vostre capsule?";
	String result_message="{\"result\":[{\"name\":\"fantasia\",\"brand\":\"electrolux\"},{\"name\":\"fantasia plus\",\"brand\":\"electrolux\"},{\"name\":\"jolie\",\"brand\":\"lavazza\"},{\"name\":\"minù\",\"brand\":\"lavazza\"},{\"name\":\"minù caffè latte\",\"brand\":\"lavazza\"},{\"name\":\"magia\",\"brand\":\"electrolux\"},{\"name\":\"magia plus\",\"brand\":\"electrolux\"},{\"name\":\"espria plus\",\"brand\":\"electrolux\"}],\"query\":{\"domain\":[{\"name\":\"machine\",\"property\":[{\"name\":\"brand\",\"category\":\"property\",\"token\":\"marche\"},{\"name\":\"compatible\",\"category\":\"property\",\"token\":\"compatibili\"}],\"category\":\"domain\"},{\"name\":\"capsule\",\"property\":{\"name\":\"brand\",\"category\":\"property\"},\"category\":\"domain\",\"token\":\"capsule\"}],\"name\":\"search\",\"category\":\"action\",\"token\":\"quali\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"sono\"}}";
	String real_result="{\"result\":[{\"brand\":\"electrolux\"},{\"brand\":\"lavazza\"}]}";
	
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
