package time;

public class Instance{
	boolean contains=false;
	String term=null;
	String value=null;
	
	public Instance(){}
	
	public Instance(String term,String value){
		contains=true;
		this.term=term;
		this.value=value;
	}
	
	public boolean contains(){
		return contains;
	}
	
	public String term(){
		return term;
	}
	
	public String value(){
		return value;
	}
}
