package eu.reply.DM.jUnit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.reply.DM.DialogManager;

public class DM133 extends DM000{
	
	String url = DM_AllTests.URL;
	float threshold = DM_AllTests.threshold;
	DialogManager dm;
	String message="vorrei una macchina che oltre al caffè faccia il cappuccino, quale macchina mi consigli?";
	String result_message="{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"oltre\"}}{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"fantasia plus\"},{\"name\":\"jolie\"},{\"name\":\"minù\"},{\"name\":\"minù caffè latte\"},{\"name\":\"magia\"},{\"name\":\"magia plus\"},{\"name\":\"espria plus\"}],\"query\":{\"domain\":{\"name\":\"machine\",\"category\":\"domain\",\"token\":\"macchina\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"faccia\"}}{\"result\":[{\"name\":\"lavazza qualità rossa\"},{\"name\":\"lavazza selva alta\"},{\"name\":\"lavazza cereja passita\"},{\"name\":\"lavazza aromatico\"},{\"name\":\"lavazza ricco\"},{\"name\":\"lavazza tierra\"},{\"name\":\"lavazza magia\"},{\"name\":\"lavazza passionale\"},{\"name\":\"lavazza soave\"},{\"name\":\"lavazza intenso\"},{\"name\":\"lavazza deck cremoso\"},{\"name\":\"lavazza delizioso\"},{\"name\":\"lavazza divino\"},{\"name\":\"lavazza dolce\"},{\"name\":\"lavazza caffè ginseng\"},{\"name\":\"lavazza orzo\"}],\"query\":{\"domain\":{\"name\":\"capsule\",\"category\":\"domain\",\"token\":\"caffè\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"fantasia plus\"},{\"name\":\"minù caffè latte\"}],\"query\":{\"domain\":{\"name\":\"machine\",\"property\":{\"name\":\"milkProgram\",\"category\":\"property\",\"value\":{\"name\":\"true_milkProgram\",\"category\":\"value\",\"token\":\"cappuccin\"}},\"category\":\"domain\"},\"name\":\"search\",\"category\":\"action\",\"token\":\"vorrei\"}}{\"query\":{\"name\":\"text\",\"category\":\"extracted\",\"token\":\"consigli\"}}";
	String real_result="{\"result\":[{\"name\":\"fantasia\"},{\"name\":\"fantasia plus\"},{\"name\":\"minù caffè latte\"}]}";
	
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
