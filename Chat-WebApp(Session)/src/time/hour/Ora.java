package time.hour;

import java.util.HashSet;

import time.Instance;
import time.hour.OraSynset;

public class Ora {

	static final boolean DEBUG = false;

	String input;
	// String[] preposizioni = { "all'", "l'", "le", "alle" };
	static final String[] ore = { "l'una", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove", "dieci",
			"undici", "dodici", "tredici", "quattordici", "quindici", "sedici", "diciassette", "diciotto", "diciannove",
			"venti", "ventuno", "ventidue", "ventitre", "mezzanotte" };
	static final String[] minuti_10 = { "dieci", "undici", "dodici", "tredici", "quattordici", "quindici", "sedici",
			"diciassette", "diciotto", "diciannove" };
	static final String[] minuti_base = { "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto", "nove" };
	static final String[] minuti_decine = { "venti", "trenta", "quaranta", "cinquata" };
	static final String[] quarti = { "quarto", "mezza" };
	static final String[] piu = { "e", "e un" };
	static final String[] meno = { "meno", "meno un" };

	private static HashSet<OraSynset> synset;

	public static void init() {
		synset = new HashSet<OraSynset>();
		for (int hh = 1; hh <= 24; hh++) {
			for (int mm = 0; mm < 60; mm++) {
				String v = ora_number(hh, mm, ":");
				OraSynset now = new OraSynset(v);
				now.add(v);
				now.add(ora_number_zeroMM(hh, mm, ":"));
				now.add(ora_number_zeroHH(hh, mm, ":"));
				
				now.add(ora_number(hh, mm, "."));
				now.add(ora_number_zeroMM(hh, mm, "."));
				now.add(ora_number_zeroHH(hh, mm, "."));

				
				now.add(ora_number(hh, mm, " e "));
				now.add(ora_number_zeroMM(hh, mm, " e "));
				now.add(ora_number_zeroHH(hh, mm, " e "));
				
				now.addAll(ora_text(hh, mm));
				now.addAll(ora_hibrid_hh(hh, mm));
				now.addAll(ora_hibrid_mm(hh, mm));
				synset.add(now);
				if (DEBUG)
					System.out.println(now);
			}

		}
	}

	public static Instance contains(String input) {
		Instance result = new Instance();
		input = input.toLowerCase();
		for (OraSynset x : synset) {
			Instance partial = x.contains(input);
			if (!result.contains()) {
				result = partial;
			} else if (partial.contains() && partial.term().length() > result.term().length()) {
				result = partial;
			}
		}
		return result;
	}
	//da vedere
	
	private static String ora_number(int hh, int mm, String sep) {
		String result = "";
		String dec_h = "";
		String dec_m = "";

		if (hh < 10 || hh == 24) {
			dec_h = "0";
		}
		if (mm < 10) {
			dec_m = "0";
		}
		
		result = dec_h + (hh % 24) + sep + dec_m + mm;
		return result;
	}
	
	private static String ora_number_zeroMM(int hh, int mm, String sep) {
		String result = "";
		String dec_m = "";

		if (mm < 10) {
			dec_m = "0";
		}
		
		result = (hh % 24) + sep + dec_m + mm;
		return result;
	}
	
	private static String ora_number_zeroHH(int hh, int mm, String sep) {
		String result = "";
		String dec_h = "";

		if (hh < 10 || hh == 24) {
			dec_h = "0";
		}
		
		result = dec_h + (hh % 24) + sep + mm;
		return result;
	}

	private static HashSet<String> ora_text(int hh, int mm) {
		HashSet<String> result = new HashSet<String>();

		String text_orario = "";
		String text_orario_quarti = "";
		String text_orario_meno = "";

		if (mm == 0) {
			text_orario = ore[hh - 1];
		} else if (mm < 10) {
			text_orario = ore[hh - 1] + " " + piu[0] + " " + minuti_base[mm - 1];
		} else if (mm < 20) {
			text_orario = ore[hh - 1] + " " + piu[0] + " " + minuti_10[mm - 10];
			if (mm == 15) {
				text_orario_quarti = ore[hh - 1] + " " + piu[1] + " " + quarti[0];
			}
		} else if (mm < 30) {
			String base = minuti_decine[0];
			if (mm == 21) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 20) {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base;
			} else {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base + minuti_base[mm - 21];
			}
		} else if (mm < 40) {
			String base = minuti_decine[1];
			if (mm == 31) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 30) {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base;
				text_orario_quarti = ore[hh - 1] + " " + piu[0] + " " + quarti[1];
			} else {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base + minuti_base[mm - 31];
			}
		} else if (mm < 50) {
			String base = minuti_decine[2];
			if (mm == 41) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 40) {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base;
				text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + minuti_decine[0];
			} else {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base + minuti_base[mm - 41];
			}
			if (mm == 45) {
				text_orario_quarti = ore[hh % 24] + " " + meno[1] + " " + quarti[0];
			}
		} else if (mm < 60) {
			String base = minuti_decine[3];
			if (mm == 51) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 50) {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base;
				text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + minuti_10[0];
			} else {
				text_orario = ore[hh - 1] + " " + piu[0] + " " + base + minuti_base[mm - 51];
			}
			if (mm == 55) {
				text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + minuti_base[4];
			}
		}

		result.add(text_orario);
		if (!text_orario_meno.equals("")) {
			result.add(text_orario_meno);
		}

		if (!text_orario_quarti.equals("")) {
			result.add(text_orario_quarti);
		}
		return result;
	}

	private static HashSet<String> ora_hibrid_hh(int hh, int mm) {
		HashSet<String> result = new HashSet<String>();

		String shh = "";
		String shh2 = "";

		String text_orario = "";
		String text_orario_quarti = "";
		String text_orario_meno = "";
		String text_orario_zero = "";
		String text_orario_quarti_zero = "";
		String text_orario_zero2 = "";
		String text_orario_quarti_zero2 = "";
		
		if (hh < 10 || hh == 24) {
			shh = "0";
		}

		if (hh + 1 < 10 || hh + 1 == 24) {
			shh2 = "0";
		}

		if (mm == 0) {
			if(shh.equals("0")){
				text_orario_zero=""+(hh % 24);
			}
			text_orario = shh + (hh % 24);
		} else if (mm < 10) {
			if(shh.equals("0")){
				text_orario_zero =(hh % 24) + " " + piu[0] + " " + minuti_base[mm - 1];
			}
			text_orario = shh + (hh % 24) + " " + piu[0] + " " + minuti_base[mm - 1];
		} else if (mm < 20) {
			if(shh.equals("0")){
				text_orario_zero =(hh % 24) + " " + piu[0] + " " + minuti_10[mm - 10];
			}
			text_orario = shh + (hh % 24) + " " + piu[0] + " " + minuti_10[mm - 10];
			if (mm == 15) {
				if(shh.equals("0")){
					text_orario_quarti_zero =(hh % 24) + " " + piu[1] + " " + quarti[0];
				}
				text_orario_quarti = shh+(hh % 24) + " " + piu[1] + " " + quarti[0];
			}
		} else if (mm < 30) {
			String base = minuti_decine[0];
			if (mm == 21) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 20) {
				if(shh.equals("0")){
					text_orario_zero =(hh % 24) + " " + piu[0] + " " + base;
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base;
			} else {
				if(shh.equals("0")){
					text_orario_zero =(hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 21];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 21];
			}
		} else if (mm < 40) {
			String base = minuti_decine[1];
			if (mm == 31) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 30) {
				if(shh.equals("0")){
					text_orario_zero = (hh % 24) + " " + piu[0] + " " + base;
					text_orario_quarti_zero = (hh % 24) + " " + piu[0] + " " + quarti[1];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base;
				text_orario_quarti = shh + (hh % 24) + " " + piu[0] + " " + quarti[1];
			} else {
				if(shh.equals("0")){
					text_orario_zero =(hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 31];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 31];
			}
		} else if (mm < 50) {
			String base = minuti_decine[2];
			if (mm == 41) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 40) {
				if(shh.equals("0")){
					text_orario_zero = (hh % 24) + " " + piu[0] + " " + base;	
				}
				
				if(shh2.equals("0")){
					text_orario_zero2 = ((hh + 1) % 24) + " " + meno[0] + " " + minuti_decine[0];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base;
				text_orario_meno = shh2 + ((hh + 1) % 24) + " " + meno[0] + " " + minuti_decine[0];
			} else {
				if(shh.equals("0")){
					text_orario_zero = (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 41];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 41];
			}
			if (mm == 45) {
				if(shh2.equals("0")){
					text_orario_quarti_zero2 =((hh + 1) % 24) + " " + meno[1] + " " + quarti[0];
				}
				text_orario_quarti = shh2 + ((hh + 1) % 24) + " " + meno[1] + " " + quarti[0];
			}
		} else if (mm < 60) {
			String base = minuti_decine[3];
			if (mm == 51) {
				base = base.substring(0, base.length() - 1);
			}
			if (mm == 50) {
				if(shh.equals("0")){
					text_orario_zero = (hh % 24) + " " + piu[0] + " " + base;
				}
				if(shh2.equals("0")){
					text_orario_zero2 = ((hh + 1) % 24) + " " + meno[0] + " " + minuti_10[0];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base;
				text_orario_meno = shh2 + ((hh + 1) % 24) + " " + meno[0] + " " + minuti_10[0];
			} else {
				if(shh.equals("0")){
					text_orario_zero = (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 51];
				}
				text_orario = shh + (hh % 24) + " " + piu[0] + " " + base + minuti_base[mm - 51];
			}
			if (mm == 55) {
				if(shh2.equals("0")){
					text_orario_zero2 = shh2 + ((hh + 1) % 24) + " " + meno[0] + " " + minuti_base[4];
				}
				text_orario_meno = shh2 + ((hh + 1) % 24) + " " + meno[0] + " " + minuti_base[4];
			}
		}

		result.add(text_orario);
		if (!text_orario_meno.equals("")) {
			result.add(text_orario_meno);
		}

		if (!text_orario_quarti.equals("")) {
			result.add(text_orario_quarti);
		}
		
		if(!text_orario_zero.equals("")){
			result.add(text_orario_zero);
		}
		if(!text_orario_zero2.equals("")){
			result.add(text_orario_zero2);
		}
		if(!text_orario_quarti_zero.equals("")){
			result.add(text_orario_quarti_zero);
		}
		if(!text_orario_quarti_zero2.equals("")){
			result.add(text_orario_quarti_zero2);
		}
		return result;
	}

	private static HashSet<String> ora_hibrid_mm(int hh, int mm) {
		HashSet<String> result = new HashSet<String>();

		String smm = "";

		String text_orario = "";
		String text_orario_meno = "";
		String text_orario_zero="";
		
		if (mm < 10) {
			smm = "0";
		}

		if (mm == 40) {
			text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + 20;
		}
		if (mm == 45) {
			text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + 15;
		}
		if (mm == 50) {
			text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + 10;
		}
		if (mm == 55) {
			text_orario_meno = ore[hh % 24] + " " + meno[0] + " " + 5;
		}

		text_orario = ore[hh - 1] + " " + piu[0] + " " + smm + mm;
		
		text_orario_zero=ore[hh - 1] + " " + piu[0] + " " + mm;
		
		result.add(text_orario);
		if (!text_orario_meno.equals("")) {
			result.add(text_orario_meno);
		}
		if(smm.equals("0")){
			result.add(text_orario_zero);
		}
		return result;
	}
	
}
