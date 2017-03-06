package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM130 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="Buondì, vorrei comprare delle capsule molto intense, quali mi consigliate? Grazie";
	String result_message="{\"query\":{\"name\":\"greetings\",\"category\":\"dialog\",\"token\":\"buondì\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"molto\"}}{\"query\":{\"name\":\"thanks\",\"category\":\"dialog\",\"token\":\"grazie\"}}{\"result\":[{\"intensity\":10,\"name\":\"lavazza qualità rossa\"},{\"intensity\":7,\"name\":\"lavazza selva alta\"},{\"intensity\":9,\"name\":\"lavazza cereja passita\"},{\"intensity\":6,\"name\":\"lavazza aromatico\"},{\"intensity\":7,\"name\":\"lavazza ricco\"},{\"intensity\":9,\"name\":\"lavazza tierra\"},{\"intensity\":8,\"name\":\"lavazza magia\"},{\"intensity\":11,\"name\":\"lavazza passionale\"},{\"intensity\":5,\"name\":\"lavazza soave\"},{\"intensity\":13,\"name\":\"lavazza intenso\"},{\"intensity\":7,\"name\":\"lavazza deck cremoso\"},{\"intensity\":8,\"name\":\"lavazza delizioso\"},{\"intensity\":11,\"name\":\"lavazza divino\"},{\"intensity\":6,\"name\":\"lavazza dolce\"},{\"intensity\":-1,\"name\":\"lavazza caffè ginseng\"},{\"intensity\":-1,\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"property\":{\"name\":\"intensity\",\"category\":\"property\",\"token\":\"intense\"},\"category\":\"domain\",\"token\":\"capsule\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei comprare\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"consigliate\"}}";
    String real_result="{\"result\":[{\"intensity\":10,\"name\":\"lavazza qualità rossa\"},{\"intensity\":11,\"name\":\"lavazza passionale\"},{\"intensity\":13,\"name\":\"lavazza intenso\"},{\"intensity\":11,\"name\":\"lavazza divino\"}]}";
	
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
