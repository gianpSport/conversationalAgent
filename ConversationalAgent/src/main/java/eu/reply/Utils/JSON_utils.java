package eu.reply.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON_utils {

	public static JSONArray convertJSONArray(String val) {
		if (isJSONArray(val)) {
			return new JSONArray(val);
		} else {
			return (new JSONArray()).put(new JSONObject(val));
		}
	}

	public static boolean isJSONArray(String val) {
		return val.charAt(0) == '[';
	}

	public static boolean isJSONObject(String val) {
		JSONObject test = null;
		try {
			test = new JSONObject(val);
		} catch (Exception ex) {

		}
		return test != null;
	}

	public static JSONArray merge(JSONArray x, JSONArray y) {
		if (x == null && y == null) {
			return null;
		} else if (x != null && y == null) {
			return x;
		} else if (x == null && y != null) {
			return y;
		} else {
			for (int i = 0; i < y.length(); i++) {
				x.put(y.get(i));
			}
			return x;
		}
	}

}
