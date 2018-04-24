package MService;

import java.util.ArrayList;
import java.util.HashSet;

enum RangeType{
	Catalogue,
	Interval;
}
public class ProfileParameter extends Parameter{
 
	//Attributes
	protected RangeType rangeType;
	protected ArrayList<String>  rangeValues;
	
	
	//Constructor
	public ProfileParameter(){
		this.rangeValues= new ArrayList<String>();
	}
	public ProfileParameter(Parameter param, RangeType rangetype){
		super(param);
		this.rangeType= rangetype;
		this.rangeValues= new ArrayList<String>();
	}
	
	//Getters 
	public RangeType getRangeType() {
		return rangeType;
	}
	public ArrayList<String> getRangeValues() {
		return rangeValues;
	}
	
	//Setters
	public void setRangeType(RangeType rangeType) {
		this.rangeType = rangeType;
	}
	
	public void setRangeValues(ArrayList<String> rangeValues) {
		this.rangeValues = rangeValues;
	}
	
	//methods
	public  void addValueToRange(String value) {
		this.rangeValues.add(value);
	}
}
