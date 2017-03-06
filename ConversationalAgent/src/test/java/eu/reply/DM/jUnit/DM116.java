package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM116 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Salve, sono interessato alla macchinetta Fantasia. Posso avere le caratteristiche tecniche? ";
	String result_message="{\"query\":{\"name\":\"greetings\",\"category\":\"dialog\",\"token\":\"salve\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"sono\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"posso avere\"}}{\"result\":{\"programmable\":false,\"color\":[\"red\",\"white\",\"black\"],\"weight\":4.15,\"milkProgram\":true,\"cheap\":false,\"time ready\":35,\"thermoblock\":true,\"expensive\":true,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":false,\"light\":false,\"price\":179.9,\"name\":\"fantasia\",\"tank capacity\":1.2,\"autoOFF\":false,\"brand\":\"electrolux\"},\"query\":{\"domain\":{\"name\":\"machine\",\"property\":[{\"name\":\"name machine\",\"category\":\"property\",\"value\":{\"name\":\"fantasia\",\"category\":\"value\",\"token\":\"fantasia\"}},{\"name\":\"features\",\"category\":\"property\",\"token\":\"caratteristiche\"}],\"category\":\"domain\",\"token\":\"macchinetta\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"interessato\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"tecniche\"}}";
	String real_result="{\"result\":{\"programmable\":false,\"color\":[\"red\",\"white\",\"black\"],\"weight\":4.15,\"milkProgram\":true,\"cheap\":false,\"time ready\":35,\"thermoblock\":true,\"expensive\":true,\"heavy\":true,\"promo\":false,\"compatible\":\"lavazza\",\"fast\":false,\"slow\":false,\"light\":false,\"price\":179.9,\"name\":\"fantasia\",\"tank capacity\":1.2,\"autoOFF\":false,\"brand\":\"electrolux\"}}";
	
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
