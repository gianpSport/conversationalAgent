package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM314 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Vorrei una panoramica sulla macchina da caffè espria plus";
	String result_message="{\"result\":{\"programmable\":false,\"color\":[\"grey\"],\"weight\":4.27,\"milkProgram\":false,\"cheap\":false,\"time ready\":40,\"thermoblock\":true,\"expensive\":false,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":false,\"light\":false,\"price\":119.9,\"name\":\"espria plus\",\"tank capacity\":0.8,\"autoOFF\":true,\"brand\":\"electrolux\"},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"features\",\"category\":\"property\",\"token\":\"panoramica\"},{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"espria plus\",\"category\":\"value\",\"token\":\"espria plus\"}}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}";
	String real_result="{\"result\":{\"programmable\":false,\"color\":[\"grey\"],\"weight\":4.27,\"milkProgram\":false,\"cheap\":false,\"time ready\":40,\"thermoblock\":true,\"expensive\":false,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":false,\"light\":false,\"price\":119.9,\"name\":\"espria plus\",\"tank capacity\":0.8,\"autoOFF\":true,\"brand\":\"electrolux\"}}";
	
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
		DM_AllTests.updateMesure(DM_AllTests.last_precision,DM_AllTests.last_recall,3);
	}

}
