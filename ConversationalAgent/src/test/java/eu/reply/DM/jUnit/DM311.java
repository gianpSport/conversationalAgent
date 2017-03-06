package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM311 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Vorrei una panoramica sulla macchina da caffè minù caffè latte";
	String result_message="{\"result\":{\"programmable\":false,\"color\":[\"red\",\"white\",\"cyan\"],\"weight\":4.2,\"milkProgram\":true,\"cheap\":false,\"time ready\":60,\"thermoblock\":true,\"expensive\":false,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":true,\"light\":false,\"price\":119.9,\"name\":\"minù caffè latte\",\"tank capacity\":0.5,\"autoOFF\":false,\"brand\":\"lavazza\"},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"features\",\"category\":\"property\",\"token\":\"panoramica\"},{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"minù caffè latte\",\"category\":\"value\",\"token\":\"minù caffè latte\"}}],\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}";
	String real_result="{\"result\":{\"programmable\":false,\"color\":[\"red\",\"white\",\"cyan\"],\"weight\":4.2,\"milkProgram\":true,\"cheap\":false,\"time ready\":60,\"thermoblock\":true,\"expensive\":false,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":true,\"light\":false,\"price\":119.9,\"name\":\"minù caffè latte\",\"tank capacity\":0.5,\"autoOFF\":false,\"brand\":\"lavazza\"}}";
	
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
