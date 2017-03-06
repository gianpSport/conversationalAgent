package time.duration;

import java.util.ArrayList;

public class Durata {
	static String[] secondi = { "secondi", "secondo","Secondi","Secondo" };
	static String[] minuti = { "minuti", "minuto","Minuto","Minuti" };
	static String[] ore = { "ore", "ora","Ore","Ora" };

	public static int decode(String input) throws DurationFormatException {
		int result = 0;
		int[] index = findMeter(input);
		if (index[0] == -1 && index[1] == -1 && index[2] == -1) {
			throw new DurationFormatException();
		} else {
			ArrayList<String> number = findNumber(input);
			if (number.size() == 0) {
				throw new DurationFormatException();
			} else {
				for (String n : number) {
					
					int start = input.indexOf(n);
					int last = start + n.length();
					
					String sub="";
					for(int l=0;l<n.length();l++){
						sub+="x";
					}
					input=input.replaceFirst(n,sub);
					
					int[] dist = { -1, -1, -1 };
					
					if (index[0] != -1) {
						dist[0] = Math.abs(index[0] - last);
					}
					if (index[1] != -1) {
						dist[1] = Math.abs(index[1] - last);
					}
					if (index[2] != -1) {
						dist[2] = Math.abs(index[2] - last);
					}
					
					if (dist[0] != -1 && dist[1] == -1 && dist[2] == -1) {
						if (result == -1)
							result = 0;
						result += Integer.valueOf(n);
					}else

					if (dist[0] == -1 && dist[1] != -1 && dist[2] == -1) {
						if (result == -1)
							result = 0;
						result += Integer.valueOf(n) * 60;
					}else

					if (dist[0] == -1 && dist[1] == -1 && dist[2] != -1) {
						if (result == -1)
							result = 0;
						result += Integer.valueOf(n) * 60 * 60;
					}else
					if (dist[0] != -1 && dist[1] != -1 && dist[2] == -1) {
						if (Math.min(dist[0], dist[1]) == dist[0]) {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n);
						} else {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60;
						}
					}else
					if (dist[0] != -1 && dist[1] == -1 && dist[2] != -1) {
						if (Math.min(dist[0], dist[2]) == dist[0]) {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n);
						} else {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60 * 60;
						}
					}else

					if (dist[0] == -1 && dist[1] != -1 && dist[2] != -1) {
						if (Math.min(dist[1], dist[2]) == dist[1]) {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60;
						} else {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60 * 60;
						}
					}else

					if (dist[0] != -1 && dist[1] != -1 && dist[2] != -1) {
						if (Math.min(Math.min(dist[0], dist[1]), dist[1]) == dist[0]) {
							result += Integer.valueOf(n);
						} else if (Math.min(Math.min(dist[0], dist[1]), dist[2]) == dist[1]) {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60;
						} else {
							if (result == -1)
								result = 0;
							result += Integer.valueOf(n) * 60 * 60;
						}
					}
				}
			}
		}
		return result;
	}

	public static ArrayList<String> findNumber(String input) {
		ArrayList<String> result = new ArrayList<String>();
		int last = -1;
		String partial = "";
		char[] array = input.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (Character.isDigit(array[i]) && last == -1) {
				partial += array[i];
				last = i;
			} else if (Character.isDigit(array[i]) && last == i - 1) {
				partial += array[i];
				last = i;
			} else if (Character.isDigit(array[i]) && last != i - 1) {
				result.add(partial);
				partial = "" + array[i];
				last = i;
			}else if(i==array.length-1 && !partial.equals("")){
				result.add(partial);
			}
		}
		return result;
	}

	public static int[] findMeter(String input) throws DurationFormatException {
		int[] meter = { -1, -1, -1 };
		for (int i_sec=0;i_sec<secondi.length;i_sec++) {
			String sec=secondi[i_sec];
			if (input.contains(sec)) {
				int start = input.indexOf(sec);
				if (meter[0] == -1) {
					meter[0] = start;
					String sub="";
					for(int l=0;l<sec.length();l++){
						sub+="x";
					}
					input=input.replaceFirst(sec, sub);
					i_sec--;
				} else {
					throw new DurationFormatException();
				}
			}
		}

		for (int i_min=0;i_min<minuti.length;i_min++) {
			String min=minuti[i_min];
			if (input.contains(min)) {
				int start = input.indexOf(min);
				if (meter[1] == -1) {
					meter[1] = start;
					String sub="";
					for(int l=0;l<min.length();l++){
						sub+="x";
					}
					input=input.replaceFirst(min, sub);
					i_min--;
				} else {
					throw new DurationFormatException();
				}
			}
		}

		for (int i_ora=0;i_ora<ore.length;i_ora++) {
			String o=ore[i_ora];
			if (input.contains(o)) {
				int start = input.indexOf(o);
				if (meter[2] == -1) {
					meter[2] = start;
					String sub="";
					for(int l=0;l<o.length();l++){
						sub+="x";
					}
					input=input.replaceFirst(o, sub);
					i_ora--;
				} else {
					throw new DurationFormatException();
				}
			}
		}
		return meter;
	}
}
