package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM122 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="ciao potresti farmi una panoramica sulle macchine?";
	String result_message="{\"query\":{\"name\":\"greetings\",\"category\":\"dialog\",\"token\":\"ciao\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"potresti farmi\"}}{\"result\":[{\"name\":\"name machine\"},{\"name\":\"slow\"},{\"name\":\"brand\"},{\"name\":\"time ready\"},{\"name\":\"expensive\"},{\"name\":\"color\"},{\"name\":\"autoOFF\"},{\"name\":\"programmable\"},{\"name\":\"heavy\"},{\"name\":\"thermoblock\"},{\"name\":\"tank capacity\"},{\"name\":\"promo\"},{\"name\":\"light\"},{\"name\":\"price\"},{\"name\":\"milkProgram\"},{\"name\":\"cheap\"},{\"name\":\"weight\"},{\"name\":\"fast\"}],\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"features\",\"category\":\"property\",\"token\":\"panoramica\"},\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\"}}";
	String real_result="{\"result\":[{\"name\":\"name machine\"},{\"name\":\"slow\"},{\"name\":\"brand\"},{\"name\":\"time ready\"},{\"name\":\"expensive\"},{\"name\":\"color\"},{\"name\":\"autoOFF\"},{\"name\":\"programmable\"},{\"name\":\"heavy\"},{\"name\":\"thermoblock\"},{\"name\":\"tank capacity\"},{\"name\":\"promo\"},{\"name\":\"light\"},{\"name\":\"price\"},{\"name\":\"milkProgram\"},{\"name\":\"cheap\"},{\"name\":\"weight\"},{\"name\":\"fast\"}]}";
	
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
