package eu.reply.DM.jUnit;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.reply.Config.Config;
import eu.reply.SN.Reader;
import eu.reply.SN.SemanticNet;
import eu.reply.Utils.JSON_utils;

@RunWith(Suite.class)
@SuiteClasses({ DM001.class, DM002.class, DM003.class, DM004.class, DM005.class, DM006.class, DM007.class, DM008.class,
		DM009.class, DM010.class, DM011.class, DM012.class, DM013.class, DM014.class, DM015.class, DM016.class,
		DM017.class, DM018.class, DM019.class, DM020.class, DM021.class, DM022.class, DM023.class, DM024.class,
		DM025.class, DM026.class, DM027.class, DM028.class, DM029.class, DM030.class, DM031.class, DM032.class,
		DM033.class, DM034.class, DM035.class, DM036.class, DM037.class, DM038.class, DM039.class, DM040.class,
		DM041.class, DM042.class, DM043.class, DM044.class, DM045.class, DM046.class, DM047.class, DM048.class,
		DM049.class, DM050.class, DM051.class, DM052.class, DM053.class, DM054.class, DM055.class, DM056.class,
		DM057.class, DM058.class, DM059.class, DM060.class, DM061.class, DM062.class, DM063.class, DM064.class,
		DM065.class, DM066.class, DM067.class, DM068.class, DM069.class, DM070.class, DM071.class, DM072.class,
		DM073.class, DM074.class, DM075.class, DM076.class, DM077.class, DM078.class, DM079.class, DM080.class,
		DM081.class, DM082.class, DM083.class, DM084.class, DM085.class, DM086.class, DM087.class, DM088.class,
		DM089.class, DM090.class, DM091.class, DM092.class, DM093.class, DM094.class, DM095.class, DM096.class,
		DM097.class, DM098.class, DM099.class, DM100.class, DM101.class, DM102.class, DM103.class, DM104.class,
		DM105.class, DM106.class, DM107.class, DM108.class, DM109.class, DM110.class, DM111.class, DM112.class,
		DM113.class, DM114.class, DM115.class, DM116.class, DM117.class, DM118.class, DM119.class, DM120.class,
		DM121.class, DM122.class, DM123.class, DM124.class, DM125.class, DM126.class, DM127.class, DM128.class,
		DM129.class, DM130.class, DM131.class, DM132.class, DM133.class, DM134.class, DM135.class, DM136.class,
		DM137.class, DM138.class, DM139.class, DM140.class, DM141.class, DM142.class, DM143.class, DM144.class,
		DM145.class, DM146.class, DM147.class, DM148.class, DM149.class, DM150.class, DM151.class, DM152.class,
		DM153.class, DM154.class, DM155.class, DM156.class, DM157.class, DM158.class, DM159.class, DM160.class,
		DM161.class, DM162.class, DM163.class, DM164.class, DM165.class, DM166.class, DM167.class, DM168.class,
		DM169.class, DM170.class, DM171.class, DM172.class, DM173.class, DM174.class, DM175.class, DM176.class,
		DM177.class, DM178.class, DM179.class, DM180.class, DM181.class, DM182.class, DM183.class, DM184.class,
		DM185.class, DM186.class, DM187.class, DM188.class, DM189.class, DM190.class, DM191.class, DM192.class,
		DM193.class, DM194.class, DM195.class, DM196.class, DM197.class, DM198.class, DM199.class, DM200.class,
		DM201.class, DM202.class, DM203.class, DM204.class, DM205.class, DM206.class, DM207.class, DM208.class,
		DM209.class, DM210.class, DM211.class, DM212.class, DM213.class, DM214.class, DM215.class, DM216.class,
		DM217.class, DM218.class, DM219.class, DM220.class, DM221.class, DM222.class, DM223.class, DM224.class,
		DM225.class, DM226.class, DM227.class, DM228.class, DM229.class, DM230.class, DM231.class, DM232.class,
		DM233.class, DM234.class, DM235.class, DM236.class, DM237.class, DM238.class, DM239.class, DM240.class,
		DM241.class, DM242.class, DM243.class, DM244.class, DM245.class, DM246.class, DM247.class, DM248.class,
		DM249.class, DM250.class, DM251.class, DM252.class, DM253.class, DM254.class, DM255.class, DM256.class,
		DM257.class, DM258.class, DM259.class, DM260.class, DM261.class, DM262.class, DM263.class, DM264.class,
		DM265.class, DM266.class, DM267.class, DM268.class, DM269.class, DM270.class, DM271.class, DM272.class,
		DM273.class, DM274.class, DM275.class, DM276.class, DM277.class, DM278.class, DM279.class, DM280.class,
		DM281.class, DM282.class, DM283.class, DM284.class, DM285.class, DM286.class, DM287.class, DM288.class,
		DM289.class, DM290.class, DM291.class, DM292.class, DM293.class, DM294.class, DM295.class, DM296.class,
		DM297.class, DM298.class, DM299.class, DM300.class, DM301.class, DM302.class, DM303.class, DM304.class,
		DM305.class, DM306.class, DM307.class, DM308.class, DM309.class, DM310.class, DM311.class, DM312.class,
		DM313.class, DM314.class})

public class DM_AllTests {
		
	public static final String URL = Config.getPathSemanticNet();
	public static final float threshold = 0.9f;
	public static SemanticNet net = new SemanticNet(URL);
	public static JSONObject read = Reader.readNLU(net.getModel());
	private static double precision = 0.0d;
	private static double recall = 0.0d;
	public static double last_precision = 0.0d; // TP/(TP+FP)
	public static double last_recall = 0.0d; // TP/Pos
	private static int count = 0;
	public static final String euro_symbol = "€";
	public static final String euro_unicode = "\u20ac";
	
	public static HashMap<Integer,ArrayList<Long>> execution_time=new HashMap<>();
	
	private static double[] precision_arr = new double[4];
	private static double[] recall_arr = new double[4];
	private static int[] count_arr = new int[4];
	
	private static int[] time_arr=new int[4];
	private static int time=0;
	private static int last_class;
	
	public static long start;
	public static long stop;

	@BeforeClass
	public static void setUpClass() {
		last_precision = 0.0d;
		last_recall = 0.0d;
		System.out.println("init");
		
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("after");
		System.out.println("count: " + count);
		System.out.println("precision: " + precision / count);
		System.out.println("recall: " + recall / count);
		System.out.println("time: "+time/count);
		System.out.println("Class 1");
		System.out.println("count: " + count_arr[0]);
		System.out.println("precision: " + precision_arr[0] / count_arr[0]);
		System.out.println("recall: " + recall_arr[0] / count_arr[0]);
		System.out.println("time: "+time_arr[0]/count_arr[0]);
		System.out.println("Class 2");
		System.out.println("count: " + count_arr[1]);
		System.out.println("precision: " + precision_arr[1] / count_arr[1]);
		System.out.println("recall: " + recall_arr[1] / count_arr[1]);
		System.out.println("time: "+time_arr[1]/count_arr[1]);
		System.out.println("Class 10");
		System.out.println("count: " + count_arr[2]);
		System.out.println("precision: " + precision_arr[2] / count_arr[2]);
		System.out.println("recall: " + recall_arr[2] / count_arr[2]);
		System.out.println("time: "+time_arr[2]/count_arr[2]);
		System.out.println("Class 11");
		System.out.println("count: " + count_arr[3]);
		System.out.println("precision: " + precision_arr[3] / count_arr[3]);
		System.out.println("recall: " + recall_arr[3] / count_arr[3]);
		System.out.println("time: "+time_arr[3]/count_arr[3]);
	}

	public static double calc_precision(String result_message, String real_result) {
		System.out.println("-------------Calcolo Precision-------------");
		JSONObject ext_result = new JSONObject();
		result_message = result_message.replaceAll(euro_symbol, "Euro");
		while (!result_message.equals("")) {
			JSONObject first = new JSONObject(result_message);
			result_message = result_message.replace(first.toString(), "");
			System.out.println("rest:" + result_message);

			if (first.has("result")) {
				JSONArray first_result = JSON_utils.convertJSONArray(first.get("result").toString());
				for (int i = 0; i < first_result.length(); i++) {
					ext_result.accumulate("result", first_result.get(i));
				}
			}
		}

		System.out.println("Extracted result:" + ext_result.toString(4));
		JSONArray pos = new JSONArray();
		JSONObject result_real = new JSONObject(real_result);
		System.out.println("Real result:" + result_real.toString(4));
		if (result_real.has("result")) {
			pos = JSON_utils.convertJSONArray(result_real.get("result").toString());
		}

		JSONArray candidate = new JSONArray();
		if (ext_result.has("result")) {
			candidate = JSON_utils.convertJSONArray(ext_result.get("result").toString());
		}

		if (pos.length() == 0 && candidate.length() == 0) {
			return 1d;
		} else if (pos.length() != 0 && candidate.length() == 0) {
			return 0d;
		}

		int tpfp = candidate.length();
		int tp = 0;

		for (int i = 0; i < pos.length(); i++) {
			JSONObject pos_i = pos.getJSONObject(i);
			for (int j = 0; j < candidate.length(); j++) {
				JSONObject candidate_j = candidate.getJSONObject(j);
				if (pass(pos_i, candidate_j)) {
					tp++;
					break;
				}
			}
		}
		double precision = ((double) tp) / ((double) tpfp);
		System.out.println("precision=tp/(tp+fp)=" + tp + "/" + tpfp + "=" + precision);
		return precision;
	}

	public static double calc_recall(String result_message, String real_result) {
		System.out.println("-------------Calcolo Recall-------------");
		JSONObject ext_result = new JSONObject();
		result_message = result_message.replaceAll(euro_symbol, "Euro");
		while (!result_message.equals("")) {
			JSONObject first = new JSONObject(result_message);
			result_message = result_message.replace(first.toString(), "");
			if (first.has("result")) {
				JSONArray first_result = JSON_utils.convertJSONArray(first.get("result").toString());
				for (int i = 0; i < first_result.length(); i++) {
					ext_result.accumulate("result", first_result.get(i));
				}
			}
		}
		System.out.println("Extracted result:" + ext_result.toString(4));
		JSONArray pos = new JSONArray();
		JSONObject result_real = new JSONObject(real_result);
		System.out.println("Real result:" + result_real.toString(4));
		if (result_real.has("result")) {
			pos = JSON_utils.convertJSONArray(result_real.get("result").toString());
		}

		JSONArray candidate = new JSONArray();
		if (ext_result.has("result")) {
			candidate = JSON_utils.convertJSONArray(ext_result.get("result").toString());
		}

		if (pos.length() == 0 && candidate.length() == 0) {
			return 1d;
		} else if (pos.length() == 0) {
			return 0d;
		}

		int tot_pos = pos.length();
		int tp = 0;

		for (int i = 0; i < pos.length(); i++) {
			JSONObject pos_i = pos.getJSONObject(i);
			for (int j = 0; j < candidate.length(); j++) {
				JSONObject candidate_j = candidate.getJSONObject(j);
				if (pass(pos_i, candidate_j)) {
					tp++;
					break;
				}
			}
		}
		double recall = ((double) tp) / ((double) tot_pos);
		System.out.println("recall=tp/Pos=" + tp + "/" + tot_pos + "=" + recall);
		return recall;
	}

	private static boolean pass(JSONObject pos_i, JSONObject candidate_j) {
		boolean result = false;
		for (String key : pos_i.toMap().keySet()) {

			if (candidate_j.has(key)) {
				String val_key = candidate_j.get(key).toString();

				if (JSON_utils.isJSONObject(val_key)) {
					if (JSON_utils.isJSONObject(pos_i.get(key).toString())) {
						if (!pass(pos_i.getJSONObject(key), candidate_j.getJSONObject(key))) {
							result = false;
							break;
						} else {
							result = true;
						}
					} else {
						JSONArray pos_array = JSON_utils.convertJSONArray(pos_i.get(key).toString());
						boolean array_result = false;
						for (int i = 0; i < pos_array.length(); i++) {
							JSONObject pos_obj = new JSONObject();
							pos_obj.accumulate(key, pos_array.getJSONObject(i));
							array_result = array_result || pass(pos_obj, candidate_j);
						}
						result = array_result;
					}
				} else if (JSON_utils.isJSONArray(val_key) && val_key.charAt(1) == '{') {
					JSONArray array_key = JSON_utils.convertJSONArray(val_key);
					boolean array_result = false;
					for (int i = 0; i < array_key.length(); i++) {
						JSONObject candidate_obj = new JSONObject();
						candidate_obj.accumulate(key, array_key.getJSONObject(i));
						array_result = array_result
								|| pass(pos_i/* .getJSONObject(key) */, candidate_obj); // array_key.getJSONObject(i));
					}
					result = array_result;

				} else if (!candidate_j.get(key).toString().equals(val_key)) {
					result = false;
					break;
				} else {
					result = true;
				}
			} else {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static void updateMesure(double precisionTest, double recallTest,int phrase_class) {
		last_class=phrase_class;
		System.out.println("CLASS: "+phrase_class);
		count++;
		precision += precisionTest;
		recall += recallTest;
		
		count_arr[phrase_class]++;
		precision_arr[phrase_class] += precisionTest;
		recall_arr[phrase_class] += recallTest;
		
	}
	
	public static void timer(long timing){
		System.out.println("CLASS TIMER: "+last_class);
		time+=timing;
		time_arr[last_class]+=timing;
	}
}


