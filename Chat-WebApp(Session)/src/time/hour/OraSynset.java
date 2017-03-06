package time.hour;

import java.util.Collection;
import java.util.HashSet;

import time.Instance;

public class OraSynset {

	HashSet<String> set;
	String value;

	public OraSynset(String v) {
		value=v;
		set = new HashSet<String>();

	}
	
	public boolean add(String ora){
		return set.add(ora);
		
	}
	
	public boolean addAll(Collection<? extends String> x){
		return set.addAll(x);
	}
	
	public Instance contains(String input_text) {
		Instance result = new Instance();
		for (String ora : set) {
			if (input_text.contains(ora)) {
				if (result.contains() && ora.length() > result.term().length()) {
					result = new Instance(ora,value);
				}
				if (!result.contains()) {
					result = new Instance(ora,value);
				}
			}
		}
		return result;
	}
	
	public String toString(){
		String result="{value:"+value+"|";
		for(String x:set){
			result+=x+";";
		}
		result=result.substring(0,result.length()-1)+"}";
		return result;
	}

}
